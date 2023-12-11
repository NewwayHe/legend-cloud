/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.api.OrderApi;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.pay.dao.PaySettlementDao;
import com.legendshop.pay.dto.*;
import com.legendshop.pay.entity.PaySettlement;
import com.legendshop.pay.service.PaySettlementItemService;
import com.legendshop.pay.service.PaySettlementOrderService;
import com.legendshop.pay.service.PaySettlementService;
import com.legendshop.pay.service.convert.PaySettlementConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySettlementServiceImpl implements PaySettlementService {

	final PaySettlementConverter converter;

	final PaySettlementDao paySettlementDao;

	final PaySettlementItemService paySettlementItemService;

	final PaySettlementOrderService paySettlementOrderService;

	final OrderApi orderApi;

	@Override
	public List<PaySettlementDTO> queryPaidBySnList(List<String> snList) {
		return this.converter.to(this.paySettlementDao.queryPaidBySnList(snList));
	}

	@Override
	public PaySettlementDTO getPaidByOrderNumber(String orderNumber) {
		return this.converter.to(paySettlementDao.getPaidByOrderNumber(orderNumber));
	}

	@Override
	public List<PaySettlementDTO> getPaidByOrderNumberList(List<String> orderNumbers) {
		return this.converter.to(paySettlementDao.getPaidByOrderNumberList(orderNumbers));
	}

	@Override
	public PaySettlementDTO getBySn(String settlementSn) {
		return this.converter.to(this.paySettlementDao.getBySn(settlementSn));
	}

	@Override
	public int updateSettlement(PaySettlementDTO paySettlement) {
		if (null == paySettlement.getId()) {
			return 0;
		}

		PaySettlement settlement = this.converter.from(paySettlement);
		return this.paySettlementDao.update(settlement);
	}

	@Override
	public R<Void> checkRepeatPay(PayParamsDTO payParamsDTO) {

		List<String> businessOrderNumberList = payParamsDTO.getBusinessOrderNumberList();
		for (String orderNumber : businessOrderNumberList) {
			long result = paySettlementDao.checkRepeatPay(orderNumber, payParamsDTO.getSettlementType(), payParamsDTO.getUserId());
			if (result > 0) {
				return R.fail("请勿重复支付");
			}
		}
		return R.ok();
	}

	@Override
	public boolean saveSettlement(PaySettlementDTO settlementDTO) {
		Long result = paySettlementDao.save(converter.from(settlementDTO));
		return result > 0;
	}

	@Override
	public R<PaySettlementSuccessDTO> success(String settlementSn) {
		PaySettlementSuccessDTO settlementSuccessDTO = new PaySettlementSuccessDTO();
		PaySettlement settlement = this.paySettlementDao.getBySn(settlementSn);
		if (settlement == null) {
			return R.fail("错误的支付单！");
		}
		List<PaySettlementItemDTO> itemList = this.paySettlementItemService.queryBySn(settlementSn);
		List<PaySettlementOrderDTO> orderList = this.paySettlementOrderService.queryOrderBySn(settlementSn);
		settlementSuccessDTO.setSettlementSn(settlementSn);
		settlementSuccessDTO.setState(settlement.getState());
		settlementSuccessDTO.setSettlementItem(orderList);
		R<OrderDTO> orderByOrderNumber = orderApi.getOrderByOrderNumber(orderList.get(0).getOrderNumber());
		if (ObjectUtil.isNotEmpty(orderByOrderNumber.getData())) {
			OrderDTO order = orderByOrderNumber.getData();
			settlementSuccessDTO.setOrderId(order.getId());
		}
		settlementSuccessDTO.setAmount(itemList.stream().map(PaySettlementItemDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
		return R.ok(settlementSuccessDTO);
	}

	@Override
	public R<PaySettlementSuccessDTO> orderSuccess(List<String> orderNumberList) {
		List<PaySettlementOrderDTO> orderList = this.paySettlementOrderService.querySnByOrderNumber(orderNumberList);
		Map<String, List<PaySettlementOrderDTO>> paySettlementOrderMap = orderList.stream().collect(Collectors.groupingBy(PaySettlementOrderDTO::getPaySettlementSn));
		List<String> paySettlementSn = new ArrayList<>();
		paySettlementOrderMap.forEach((sn, paySettlementOrderList) -> {
			if (paySettlementOrderList.size() == orderNumberList.size()) {
				// 如果存在不存在的订单号时，则跳过
				if (paySettlementOrderList.stream().noneMatch(e -> orderNumberList.contains(e.getOrderNumber()))) {
					return;
				}

				paySettlementSn.add(sn);
			}
		});

		PaySettlementSuccessDTO settlementSuccessDTO = new PaySettlementSuccessDTO();
		settlementSuccessDTO.setState(0);
		if (CollUtil.isEmpty(paySettlementSn)) {
			return R.ok(settlementSuccessDTO);
		}

		// 获取已支付的支付单
		List<PaySettlement> paySettlementList = paySettlementDao.queryPaidBySnList(paySettlementSn);
		Optional<PaySettlement> optional = paySettlementList.stream().findFirst();
		if (!optional.isPresent()) {
			return R.ok(settlementSuccessDTO);
		}

		PaySettlement paySettlement = optional.get();
		List<PaySettlementItemDTO> itemList = this.paySettlementItemService.queryBySn(paySettlement.getPaySettlementSn());
		settlementSuccessDTO.setSettlementSn(paySettlement.getPaySettlementSn());
		settlementSuccessDTO.setState(paySettlement.getState());
		settlementSuccessDTO.setSettlementItem(paySettlementOrderMap.get(paySettlement.getPaySettlementSn()));
		settlementSuccessDTO.setAmount(itemList.stream().map(PaySettlementItemDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

		return R.ok(settlementSuccessDTO);
	}

	@Override
	public List<PaySettlement> queryPaySuccessfulButOrderUnPaid() {
		//查询支付成功但订单待付款的订单
		return paySettlementDao.queryPaySuccessfulButOrderUnPaid();
	}
}
