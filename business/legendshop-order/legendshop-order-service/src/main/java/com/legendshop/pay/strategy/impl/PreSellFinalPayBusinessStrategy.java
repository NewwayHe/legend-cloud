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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class PreSellFinalPayBusinessStrategy extends AbstractPayBusinessStrategy {

	@Override
	public R<CreatePayBO> createPrepay(CreatePayDTO createPayDTO) {

		// 构建收银台参数
		CreatePayBO createPayBO = new CreatePayBO();
		List<String> businessOrderNumberList = createPayDTO.getBusinessOrderNumberList();

		// 获取商品订单
		R<List<OrderDTO>> orderListResult = orderApi.getOrderByOrderNumbersAndUserId(businessOrderNumberList, createPayDTO.getUserId(), OrderStatusEnum.PRESALE_DEPOSIT.getValue());
		List<OrderDTO> orderList = orderListResult.getData();
		if (CollectionUtil.isEmpty(orderList)) {
			return R.fail("订单不存在，请重新提交购买");
		}
		Date now = new Date();
		List<Long> orderIds = orderList.stream().map(OrderDTO::getId).collect(Collectors.toList());
		R<List<PreSellOrderDTO>> preSellOrderListResult = preSellOrderApi.queryByOrderIds(orderIds);
		List<PreSellOrderDTO> preSellOrderList = preSellOrderListResult.getData();
		if (CollUtil.isEmpty(preSellOrderList)) {
			return R.fail("订单不存在，请重新提交购买");
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
			if (!now.after(preSellOrderDTO.getFinalMStart()) && !now.before(preSellOrderDTO.getFinalMEnd())) {
				return R.fail("该订单：" + order.getOrderNumber() + "尾款支付时间错误！");
			}
			totalAmount = totalAmount.add(preSellOrderDTO.getFinalPrice()).add(order.getFreightPrice());
			subjectList.add(order.getProductName());
		}

		createPayBO.setSubjectList(subjectList);
		createPayBO.setBusinessOrderNumberList(businessOrderNumberList);
		createPayBO.setAmount(totalAmount);

		// 计算倒数结束时间，取最近到期的时间
		preSellOrderList.sort(Comparator.comparing(PreSellOrderDTO::getFinalMEnd));
		Date countdownEndTime = preSellOrderList.get(0).getFinalMEnd();
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
		R<List<OrderDTO>> orderListResult = this.orderApi.getOrderByOrderNumbersAndStatus(businessOrderNumberList, OrderStatusEnum.PRESALE_DEPOSIT.getValue());
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
			totalAmount = totalAmount.add(presellOrderDTO.getFinalPrice()).add(orderDTO.getFreightPrice());
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
		return R.ok(paymentFromDTO);

	}

	@Override
	protected StringBuilder extendSubject(List<OrderDTO> orderList, StringBuilder subject) {
		return null;
	}
}



