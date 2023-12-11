/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.pay.dao.PayRefundSettlementDao;
import com.legendshop.pay.dto.*;
import com.legendshop.pay.entity.PayRefundSettlement;
import com.legendshop.pay.enums.PayRefundStateEnum;
import com.legendshop.pay.enums.PaySettlementStateEnum;
import com.legendshop.pay.handler.refund.RefundHandler;
import com.legendshop.pay.service.PaySettlementItemService;
import com.legendshop.pay.service.PaySettlementOrderService;
import com.legendshop.pay.service.PaySettlementService;
import com.legendshop.pay.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

	final List<RefundHandler> refundHandlerList;

	final PaySettlementService paySettlementService;

	final PayRefundSettlementDao payRefundSettlementDao;

	final PaySettlementItemService paySettlementItemService;

	final PaySettlementOrderService paySettlementOrderService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<RefundResultDTO> refund(RefundDTO refund) {
		String refundSn = refund.getRefundSn();
		String orderNumber = refund.getOrderNumber();
		BigDecimal refundAmount = refund.getRefundAmount();
		// 根据订单号获取支付单
		List<PaySettlementOrderDTO> settlementOrderList = this.paySettlementOrderService.querySnByOrderNumber(orderNumber);
		if (CollectionUtils.isEmpty(settlementOrderList)) {
			return R.fail("订单尚未支付，请勿退款！");
		}
		List<String> payNumberList = settlementOrderList.stream().map(PaySettlementOrderDTO::getPaySettlementSn).collect(Collectors.toList());
		// 获取支付单
		List<PaySettlementDTO> settlementList = this.paySettlementService.queryPaidBySnList(payNumberList);
		if (CollectionUtils.isEmpty(settlementList)) {
			return R.fail("退款失败，支付单不存在");
		}

		// 判断是否已经发起过退款
		List<PayRefundSettlement> refundSettlementList = this.payRefundSettlementDao.queryByRefundSn(refundSn);

		// 获取支付项
		List<PaySettlementItemDTO> settlementItemList = this.paySettlementItemService.queryBySnListAndState(payNumberList, PaySettlementStateEnum.PAID.getCode());

		if (!CollectionUtils.isEmpty(refundSettlementList)) {
			// 已经发起过，判断是否已经成功
			if (refundSettlementList.stream().allMatch(e -> PayRefundStateEnum.SUCCESS.getCode().equals(e.getState()))) {
				log.info("本次退款已经完成，请勿重新发起！");
				return R.ok();
			}
			// 将未成功的重新发起一次
			refundSettlementList = refundSettlementList.stream().filter(e -> !PayRefundStateEnum.SUCCESS.getCode().equals(e.getState())).collect(Collectors.toList());
		} else {
			// 计算每个项的退款金额
			refundSettlementList = calculateAmount(refundAmount, settlementItemList);
			refundSettlementList.forEach(e -> settlementList.stream().filter(s -> s.getPaySettlementSn().equals(e.getSettlementSn())).findFirst().ifPresent(o -> e.setPaySource(o.getPaySource())));

			refundSettlementList.forEach(e -> settlementItemList.stream().filter(item -> item.getPayTypeId().equals(e.getPayRefundType())).findFirst().ifPresent(o -> {
				o.setRefundAmount(Optional.ofNullable(o.getRefundAmount()).orElse(BigDecimal.ZERO).add(e.getRefundAmount()));
			}));

			for (PayRefundSettlement payRefundSettlement : refundSettlementList) {
				payRefundSettlement.setRefundSn(refundSn);
				payRefundSettlement.setPayRefundSn(RandomUtil.getRandomSn());
				payRefundSettlement.setOrderNumber(orderNumber);
			}

			this.payRefundSettlementDao.save(refundSettlementList);
		}

		R<List<PayRefundSettlement>> refundR = this.refund(refundSettlementList);
		if (!refundR.success()) {
			return R.fail(refundR.getMsg());
		}
		List<PayRefundSettlement> resultList = refundR.getData();
		// 更新支付单状态
		this.paySettlementItemService.update(settlementItemList);
		this.payRefundSettlementDao.update(resultList);
		// 通知支付单
		return R.ok();
	}

	@Override
	public R<List<PayRefundSettlement>> refund(List<PayRefundSettlement> refundSettlementList) {
		for (PayRefundSettlement refundSettlement : refundSettlementList) {
			R<Void> result = this.refund(refundSettlement);
			if (result.success()) {
				refundSettlement.setState(PayRefundStateEnum.SUCCESS.getCode());
				refundSettlement.setResultDesc("退款完成！");
			} else {
				refundSettlement.setState(PayRefundStateEnum.FAIL.getCode());
				refundSettlement.setResultDesc(result.getMsg());
				log.info("退款失败， {}", result.getMsg());
				return R.fail(result.getMsg());
			}
		}

		return R.ok(refundSettlementList);
	}

	/**
	 * 单项退款
	 */
	private R<Void> refund(PayRefundSettlement refundSettlement) {
		// 获取退款方式
		RefundHandler refundHandler;
		log.info("开始单项退款, 支付退款类型: {}, 支付来源: {}", refundSettlement.getPayRefundType(), refundSettlement.getPaySource());
		log.info("refundHandlerList  :  {}", JSONUtil.toJsonStr(refundHandlerList));

		Optional<RefundHandler> handlerOptional = refundHandlerList.stream().filter(e -> e.isSupport(refundSettlement.getPayRefundType(), refundSettlement.getPaySource())).findFirst();
		if (!handlerOptional.isPresent()) {
			return R.fail("没有可用的退款查询方式！");
		}
		refundHandler = handlerOptional.get();

		// 退款申请参数
		log.info("payNumber={},refundNo={},refundAmount={}", refundSettlement.getSettlementSn(), refundSettlement.getRefundSn(), refundSettlement.getRefundAmount());

		// 发起退款
		R<RefundResultItemDTO> itemResult = refundHandler.refund(new RefundRequestDTO(refundSettlement.getId(), refundSettlement.getSettlementSn(), refundSettlement.getRefundSn(), refundSettlement.getRefundAmount(), refundSettlement.getPayAmount()));

		// 退款结果处理
		if (null == itemResult.getSuccess() || !itemResult.getSuccess()) {
			log.error(" 支付退款 --> 支付单：{} {}-{} 退款失败, 失败原因：{}", refundSettlement.getSettlementSn(), refundSettlement.getPaySource(), refundSettlement.getPayRefundType(), itemResult.getMsg());

			return R.fail(itemResult.getMsg());
		}
		RefundResultItemDTO data = itemResult.getData();
		log.info(" 支付退款 --> 支付单：{} {}-{} 退款成功, 退款金额：{}", refundSettlement.getSettlementSn(), refundSettlement.getPaySource(), refundSettlement.getPayRefundType(), data.getAmount());
		return R.ok();
	}

	/**
	 * 计算每个项的退款金额
	 *
	 * @param refundAmount       the 退款金额
	 * @param settlementItemList the 本次退款支付单涉及的支付方式，必须都是已支付状态。
	 */
	private List<PayRefundSettlement> calculateAmount(BigDecimal refundAmount, List<PaySettlementItemDTO> settlementItemList) {
		Date now = new Date();

		// 根据订单的支付单，生成对应的退款支付单。
		List<PayRefundSettlement> refundSettlementList = new ArrayList<>(settlementItemList.size());
		settlementItemList.forEach(e -> {
			PayRefundSettlement refund = new PayRefundSettlement();
			refundSettlementList.add(refund);
			refund.setPayAmount(e.getAmount());
			refund.setRefundAmount(e.getAmount());
			refund.setSettlementSn(e.getPaySettlementSn());
			refund.setPayRefundType(e.getPayTypeId());
			refund.setState(PayRefundStateEnum.DEFAULT.getCode());
			refund.setResultDesc("尚未发起第三方退款！");
			refund.setCreateTime(now);
			refund.setUpdateTime(now);
		});

		// 获取订单的支付总金额
		BigDecimal payAmount = settlementItemList.stream().map(PaySettlementItemDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		// 因为现在退款是按比例退，每种支付方式会按比例退返用户。
		// 计算退款金额占订单金额的比例，
		BigDecimal refundRatio = BigDecimal.ZERO;
		if (payAmount.compareTo(BigDecimal.ZERO) > 0) {
			refundRatio = refundAmount.divide(payAmount, 6, RoundingMode.DOWN);
		}

		//如果是整单退款，则全部的支付方式都是退完。
		if (refundRatio.compareTo(BigDecimal.ONE) == 0) {
			return refundSettlementList;
		}

		// 每个支付项退款金额
		BigDecimal finalRefundRatio = refundRatio;
		refundSettlementList.forEach(e -> e.setRefundAmount(e.getPayAmount().multiply(finalRefundRatio).setScale(2, RoundingMode.DOWN)));

		//补差，如果是按比例计算出来退款金额和实际退款金额一致，则无需要补差，如果因为小数计算导致小数精度问题，则需要补差。
		BigDecimal tempRefundAmount = refundSettlementList.stream().map(PayRefundSettlement::getRefundAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (refundAmount.compareTo(tempRefundAmount) == 0) {
			return refundSettlementList;
		}

		// 有差值，计算差值此
		BigDecimal difference = refundAmount.subtract(tempRefundAmount);
		log.info("退款金额计算差值：{}", difference);

		// 计算每个支付单的剩余未退款金额
		settlementItemList.forEach(e -> refundSettlementList.stream().filter(i -> e.getPayTypeId().equals(i.getPayRefundType())).findFirst().ifPresent(o -> e.setRemainingAmount(e.getAmount().subtract(e.getRefundAmount().add(o.getRefundAmount())))));

		if (settlementItemList.stream().anyMatch(e -> e.getRemainingAmount().compareTo(BigDecimal.ZERO) < 0)) {
			log.error("退款金额计算错误 list : {}", JSON.toJSONString(settlementItemList));
			throw new BusinessException("退款金额计算错误！");
		}

		// 根据剩余金额排序
		settlementItemList.sort(Comparator.comparing(PaySettlementItemDTO::getRemainingAmount).reversed());

		// 将补扣金额分摊到支付项中，按照剩余金额优先排序
		for (PaySettlementItemDTO settlementItem : settlementItemList) {
			if (difference.compareTo(BigDecimal.ZERO) <= 0) {
				break;
			}
			Optional<PayRefundSettlement> optional = refundSettlementList.stream().filter(i -> settlementItem.getPayTypeId().equals(i.getPayRefundType())).findFirst();
			if (!optional.isPresent()) {
				continue;
			}
			PayRefundSettlement refundSettlement = optional.get();
			if (settlementItem.getRemainingAmount().compareTo(difference) >= 0) {
				refundSettlement.setRefundAmount(refundSettlement.getRefundAmount().add(difference));
				difference = BigDecimal.ZERO;
				settlementItem.setRemainingAmount(settlementItem.getRemainingAmount().subtract(difference));
			} else {
				refundSettlement.setRefundAmount(refundSettlement.getRefundAmount().add(settlementItem.getRemainingAmount()));
				difference = difference.subtract(settlementItem.getRemainingAmount());
				settlementItem.setRemainingAmount(BigDecimal.ZERO);
			}
		}

		return refundSettlementList;
	}
}
