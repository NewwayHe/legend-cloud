/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.api.OrderRefundReturnApi;
import com.legendshop.order.enums.PayTypeEnum;
import com.legendshop.pay.dao.PayRefundSettlementDao;
import com.legendshop.pay.dto.PayRefundSettlementDTO;
import com.legendshop.pay.dto.RefundRequestDTO;
import com.legendshop.pay.entity.PayRefundSettlement;
import com.legendshop.pay.enums.PayRefundStateEnum;
import com.legendshop.pay.handler.refund.RefundHandler;
import com.legendshop.pay.service.PayRefundSettlementService;
import com.legendshop.pay.service.RefundService;
import com.legendshop.pay.service.convert.PayRefundSettlementConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * (PayRefundSettlement)表服务实现类
 *
 * @author legendshop
 * @since 2021-05-12 18:09:17
 */
@Slf4j
@Service
@AllArgsConstructor
public class PayRefundSettlementServiceImpl implements PayRefundSettlementService {

	private final RefundService refundService;
	private final List<RefundHandler> refundHandlerList;
	private final PayRefundSettlementConverter converter;
	private final PayRefundSettlementDao payRefundSettlementDao;
	private final OrderRefundReturnApi orderRefundReturnApi;

	@Override
	public Long save(PayRefundSettlementDTO dto) {
		return this.payRefundSettlementDao.save(this.converter.from(dto));
	}

	@Override
	public List<Long> save(List<PayRefundSettlementDTO> dtoList) {
		return this.payRefundSettlementDao.save(this.converter.from(dtoList));
	}

	@Override
	public List<PayRefundSettlementDTO> queryByRefundSn(String refundSn) {
		return this.converter.to(this.payRefundSettlementDao.queryByRefundSn(refundSn));
	}

	@Override
	public PayRefundSettlementDTO getByRefundSnAndType(String refundSn, String refundType) {
		return this.converter.to(this.payRefundSettlementDao.queryByRefundSnAndType(refundSn, refundType));
	}

	@Override
	public PayRefundSettlementDTO getByExternalRefundSn(String externalRefundSn) {
		return this.converter.to(this.payRefundSettlementDao.getByExternalRefundSn(externalRefundSn));
	}

	@Override
	public int update(List<PayRefundSettlementDTO> refundSettlementList) {
		return this.payRefundSettlementDao.update(this.converter.from(refundSettlementList));
	}

	@Override
	public int update(PayRefundSettlementDTO refundSettlement) {
		return this.payRefundSettlementDao.update(this.converter.from(refundSettlement));
	}

	@Override
	public R<Void> refreshQuery(String refundSn) {
		if (StringUtils.isBlank(refundSn)) {
			return R.fail("错误的退款单号");
		}
		List<PayRefundSettlement> refundSettlementList = this.payRefundSettlementDao.queryByRefundSn(refundSn);
		if (CollectionUtils.isEmpty(refundSettlementList)) {
			return R.fail("该记录尚未发起第三方退款申请！");
		}

		if (refundSettlementList.stream().anyMatch(e -> StringUtils.isBlank(e.getExternalRefundSn()))) {
			return R.fail("该请求尚未发起第三方退款申请！");
		}

		if (refundSettlementList.stream().anyMatch(e -> PayRefundStateEnum.SUCCESS.getCode().equals(e.getState()))) {
			return R.fail("该请求已完成全部退款！");
		}

		List<PayRefundSettlement> failList = refundSettlementList.stream().filter(e -> e.getState().equals(PayRefundStateEnum.FAIL.getCode())).collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(failList)) {
			StringBuilder errorMsg = new StringBuilder("部分退款失败：");
			failList.forEach(e -> errorMsg.append(PayTypeEnum.valueOf(e.getPayRefundType()).getValueName()).append("退款失败，失败原因：").append(e.getResultDesc()));
			return R.fail(errorMsg.toString());
		}

		// 发起了退款请求但没有回调成功的退款记录
		List<PayRefundSettlement> defaultList = refundSettlementList.stream().filter(e -> e.getState().equals(PayRefundStateEnum.DEFAULT.getCode()) && StringUtils.isNotBlank(e.getExternalRefundSn())).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(defaultList)) {
			return R.fail("没有退款中需要查询的退款申请！");
		}

		for (PayRefundSettlement refundSettlement : defaultList) {

			// 获取退款查询处理类
			RefundHandler refundHandler;
			Optional<RefundHandler> handlerOptional = this.refundHandlerList.stream().filter(e -> e.isSupport(refundSettlement.getPayRefundType(), refundSettlement.getPaySource())).findFirst();
			if (!handlerOptional.isPresent()) {
				return R.fail("没有可用的退款查询方式！");
			}
			refundHandler = handlerOptional.get();
			// 查询退款状态
			R<Void> result = refundHandler.queryRefundResult(new RefundRequestDTO(refundSettlement.getId(), refundSettlement.getSettlementSn(), refundSettlement.getPayRefundSn(), refundSettlement.getRefundAmount(), BigDecimal.ZERO));
			// 退款成功
			if (result.success()) {
				refundSettlement.setState(PayRefundStateEnum.SUCCESS.getCode());
				continue;
			}
			// 退款异常
			log.warn("退款查询结果 支付单号：{}，退款单号：{}，退款类型：{}，退款结果描述：{}", refundSettlement.getSettlementSn(), refundSettlement.getPayRefundSn(), refundSettlement.getPayRefundType(), result.getMsg());
			refundSettlement.setResultDesc(result.getMsg());
		}
		this.payRefundSettlementDao.update(defaultList);
		return R.ok();
	}

	@Override
	public R<Void> reApply(String refundSn) {
		if (StringUtils.isBlank(refundSn)) {
			return R.fail("错误的退款单号");
		}
		List<PayRefundSettlement> refundSettlementList = this.payRefundSettlementDao.queryByRefundSn(refundSn);
		if (CollectionUtils.isEmpty(refundSettlementList)) {
			return R.fail("该记录尚未发起第三方退款申请！");
		}

		List<PayRefundSettlement> unRefundSettlementList = refundSettlementList.stream().filter(e -> !PayRefundStateEnum.SUCCESS.getCode().equals(e.getState())).collect(Collectors.toList());
		R<List<PayRefundSettlement>> refund = refundService.refund(unRefundSettlementList);
		if (!refund.success()) {
			return R.fail(refund.getMsg());
		}
		List<PayRefundSettlement> refundResult = refund.getData();
		this.payRefundSettlementDao.update(refundResult);
		return R.ok();
	}

	@Override
	public R<Void> refreshRefundStatus(String refundSn) {
		List<PayRefundSettlement> settlementList = this.payRefundSettlementDao.queryByRefundSn(refundSn);
		// 是否全部退款成功
		if (settlementList.stream().anyMatch(e -> !PayRefundStateEnum.SUCCESS.getCode().equals(e.getState()))) {
			return R.ok();
		}
		return this.orderRefundReturnApi.refundCallback(refundSn);
	}

	@Override
	public PayRefundSettlementDTO getByPayRefundSn(String payRefundSn) {
		return converter.to(payRefundSettlementDao.getByPayRefundSn(payRefundSn));
	}
}
