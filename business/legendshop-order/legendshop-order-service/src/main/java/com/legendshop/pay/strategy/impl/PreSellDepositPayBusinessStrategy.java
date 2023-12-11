/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.strategy.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.legendshop.basic.dto.PayTypeDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.PreSellOrderDTO;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.enums.PayTypeEnum;
import com.legendshop.pay.bo.CreatePayBO;
import com.legendshop.pay.dto.CreatePayDTO;
import com.legendshop.pay.dto.PayParamsDTO;
import com.legendshop.pay.dto.PaymentFromDTO;
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.enums.UserWalletAmountTypeEnum;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.product.enums.PreSellPayType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单支付业务策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public class PreSellDepositPayBusinessStrategy extends AbstractPayBusinessStrategy {

	@Override
	public R<CreatePayBO> createPrepay(CreatePayDTO createPayDTO) {

		// 构建收银台参数
		CreatePayBO createPayBO = new CreatePayBO();
		List<String> businessOrderNumberList = createPayDTO.getBusinessOrderNumberList();

		// 获取商品订单
		R<List<OrderDTO>> orderListResult = orderApi.getOrderByOrderNumbersAndUserId(businessOrderNumberList, createPayDTO.getUserId(), OrderStatusEnum.UNPAID.getValue());
		List<OrderDTO> orderList = orderListResult.getData();
		if (CollectionUtil.isEmpty(orderList)) {
			return R.fail("订单不存在，请重新提交购买");
		}
		Date now = new Date();

		List<Long> orderIds = orderList.stream().map(OrderDTO::getId).collect(Collectors.toList());
		R<List<PreSellOrderDTO>> preSellOrderResult = preSellOrderApi.queryByOrderIds(orderIds);
		List<PreSellOrderDTO> preSellOrderList = preSellOrderResult.getData();
		if (CollUtil.isEmpty(preSellOrderList)) {
			return R.fail("预售订单不存在，请重新提交购买");
		}

		BigDecimal totalAmount = BigDecimal.ZERO;
		// 支付描述
		List<String> subjectList = new ArrayList<>();
		for (OrderDTO order : orderList) {
			Optional<PreSellOrderDTO> preSellOrderOptional = preSellOrderList.stream().filter(i -> i.getOrderId().equals(order.getId())).findFirst();
			if (!preSellOrderOptional.isPresent()) {
				return R.fail("订单数据缺失，支付失败，请重新提交购买");
			}
			PreSellOrderDTO preSellOrderDTO = preSellOrderOptional.get();
			if (!now.after(preSellOrderDTO.getPreSaleStart()) && !now.before(preSellOrderDTO.getPreSaleEnd())) {
				return R.fail("该订单：" + order.getOrderNumber() + "定金支付时间错误！");
			}
			totalAmount = totalAmount.add(preSellOrderDTO.getPreDepositPrice());
			if (PreSellPayType.FULL_AMOUNT.value().equals(preSellOrderDTO.getPayPctType())) {
				totalAmount = totalAmount.add(order.getFreightPrice());
			}
			subjectList.add(order.getProductName());
		}

		// 获取钱包抵扣金额
		BigDecimal walletAmount = BigDecimal.ZERO;
		List<UserWalletDetailsDTO> walletDetailsList = this.userWalletDetailsService.findDetailsByBusinessIds(orderIds, WalletBusinessTypeEnum.ORDER_DEDUCTION, UserWalletAmountTypeEnum.FROZEN_AMOUNT);
		if (!CollectionUtils.isEmpty(walletDetailsList)) {
			walletAmount = walletDetailsList.stream().map(UserWalletDetailsDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			log.info("钱包余额抵扣金额 walletAmount：{}", walletAmount);
		}

		if (BigDecimal.ZERO.compareTo(walletAmount) < 0) {
			totalAmount = totalAmount.subtract(walletAmount);
			log.info("抵扣后需要支付的金额为 totalAmount：{}", totalAmount);
		}

		if (BigDecimal.ZERO.compareTo(walletAmount) > 0) {
			totalAmount = BigDecimal.ZERO;
		}

		createPayBO.setSubjectList(subjectList);
		createPayBO.setBusinessOrderNumberList(businessOrderNumberList);
		createPayBO.setAmount(totalAmount);

		// 计算倒数结束时间，取最近到期的时间
		preSellOrderList.sort(Comparator.comparing(PreSellOrderDTO::getPreSaleEnd));
		Date countdownEndTime = preSellOrderList.get(0).getPreSaleEnd();
		createPayBO.setOrderCancelCountdownEndTime(countdownEndTime.getTime());

		// 获取已启用的支付方式
		List<PayTypeDTO> enabledPayType = sysParamsApi.getUseEnabledPayType().getData();
		createPayBO.setPayTypeList(enabledPayType);
		return R.ok(createPayBO);
	}

	@Override
	public R<PaymentFromDTO> initPaymentFrom(PayParamsDTO payParamsDTO) {

		Long userId = payParamsDTO.getUserId();
		BigDecimal totalAmount = BigDecimal.ZERO;
		StringBuilder subject = new StringBuilder();
		List<String> businessOrderNumberList = payParamsDTO.getBusinessOrderNumberList();
		R<List<OrderDTO>> orderListResult = this.orderApi.getOrderByOrderNumbersAndStatus(businessOrderNumberList, OrderStatusEnum.UNPAID.getValue());
		List<OrderDTO> orderList = orderListResult.getData();
		if (CollUtil.isEmpty(orderList)) {
			return R.fail("订单信息错误，请重新提交支付！");
		}
		if (orderList.stream().anyMatch(o -> !o.getUserId().equals(userId))) {
			if (!PayTypeEnum.YEEPAY_WX_PAY.name().equals(payParamsDTO.getPayTypeId())
					&& !VisitSourceEnum.MINI.equals(payParamsDTO.getVisitSource())) {
				return R.fail("订单信息错误，请重新提交支付！");
			}
		}

		List<Long> orderIds = orderList.stream().map(OrderDTO::getId).collect(Collectors.toList());
		R<List<PreSellOrderDTO>> preSellOrderListResult = this.preSellOrderApi.queryByOrderIds(orderIds);
		List<PreSellOrderDTO> preSellOrderList = preSellOrderListResult.getData();
		if (CollUtil.isEmpty(preSellOrderList)) {
			log.error("订单数据缺失，支付失败，请重新提交购买");
			return null;
		}
		for (OrderDTO orderDTO : orderList) {
			Optional<PreSellOrderDTO> preSellOrderOptional = preSellOrderList.stream().filter(po -> po.getOrderId().equals(orderDTO.getId())).findFirst();
			if (!preSellOrderOptional.isPresent()) {
				return R.fail("订单数据缺失，支付失败，请重新提交购买");
			}
			PreSellOrderDTO presellOrderDTO = preSellOrderOptional.get();
			totalAmount = totalAmount.add(presellOrderDTO.getPreDepositPrice());
			// 如果是全款，则加上运费
			if (PreSellPayType.FULL_AMOUNT.value().equals(presellOrderDTO.getPayPctType())) {
				totalAmount = totalAmount.add(orderDTO.getFreightPrice());
			}
			subject.append(orderDTO.getProductName());

		}
		String payDescription = subject.toString();
		if (payDescription.length() > 21) {
			payDescription = payDescription.substring(0, 20);
		}

		String paySettlementSn = RandomUtil.getRandomSn();
		PaymentFromDTO paymentFromDTO = new PaymentFromDTO();
		paymentFromDTO.setUserId(payParamsDTO.getUserId());
		paymentFromDTO.setBusinessOrderNumberList(payParamsDTO.getBusinessOrderNumberList());
		paymentFromDTO.setSettlementType(payParamsDTO.getSettlementType());
		paymentFromDTO.setNumber(paySettlementSn);
		paymentFromDTO.setAmount(totalAmount);
		paymentFromDTO.setPayDescription(payDescription);
		paymentFromDTO.setPayTypeId(payParamsDTO.getPayTypeId());
		paymentFromDTO.setVisitSource(payParamsDTO.getVisitSource());
		paymentFromDTO.setIp(payParamsDTO.getIp());
		paymentFromDTO.setUseWalletList(super.orderWalletDeduction(orderIds));
		return R.ok(paymentFromDTO);
	}


	@Override
	protected StringBuilder extendSubject(List<OrderDTO> orderList, StringBuilder subject) {
		return null;
	}
}
