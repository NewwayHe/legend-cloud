/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.business.order.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.api.PreSellOrderApi;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.OrderHistoryDTO;
import com.legendshop.order.dto.PreSellOrderDTO;
import com.legendshop.order.enums.OrderExceptionStatusEnum;
import com.legendshop.order.enums.OrderHistoryEnum;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.dto.PaySettlementOrderDTO;
import com.legendshop.pay.enums.PaySettlementStateEnum;
import com.legendshop.pay.handler.business.order.AbstractOrderCallbackBusinessHandler;
import com.legendshop.user.dto.CustomerBillCreateDTO;
import com.legendshop.user.enums.CustomerBillTypeEnum;
import com.legendshop.user.enums.IdentityTypeEnum;
import com.legendshop.user.enums.ModeTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
@Component("PRE_SALE_ORDER_FINAL_CALLBACK")
public class PreSellFinalCallbackBusinessHandler extends AbstractOrderCallbackBusinessHandler {

	final PreSellOrderApi preSellOrderApi;

	@Override
	public List<OrderDTO> orderHandler(List<OrderDTO> orderList) {
		// 尾款完成后直接进入代发货没有特殊处理
		return orderList;
	}

	@Override
	public void specialBusiness(List<OrderDTO> orderList) {
		List<Long> orderIds = orderList.stream().map(OrderDTO::getId).collect(Collectors.toList());
		R<List<PreSellOrderDTO>> preSellOrderResult = this.preSellOrderApi.queryByOrderIds(orderIds);
		List<PreSellOrderDTO> preSellOrderList = preSellOrderResult.getData();
		if (CollUtil.isEmpty(preSellOrderList)) {
			log.info("没有要处理的预售订单");
			return;
		}
		Date now = new Date();
		orderList.forEach(orderDTO -> {
			preSellOrderList.stream().filter(o -> ObjectUtil.equal(o.getPayPctType(), 1)).forEach(e -> {
				e.setPayFinalFlag(Boolean.TRUE);
				e.setPayFinalTime(now);
				String payTypeId = orderDTO.getPayTypeId();
				e.setFinalPayType(payTypeId);
				e.setActualAmount(e.getPreDepositPrice().add(e.getFinalPrice()).add(orderDTO.getFreightPrice()));
			});
		});
		this.preSellOrderApi.update(preSellOrderList);

		super.commissionHandler(orderList);

		log.info(" [ PreSell Order Deposit Callback ] SUCCESS 预售定金支付处理完成！");
	}

	@Override
	public void checkRepeatPay(String paySettlementSn, List<OrderDTO> orderDTOList) {

		// 第一种方式，又臭又长
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			// 保证在事务提交后调用
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					// 支付的订单编号
					List<String> orderNumbers = orderDTOList.stream().map(OrderDTO::getOrderNumber).distinct().collect(Collectors.toList());

					// 查询所有支付单, 预售尾款正常会有两笔支付单
					List<PaySettlementOrderDTO> paySettlementOrderDTOS = paySettlementOrderService.querySnByOrderNumber(orderNumbers);
					List<String> paySettlementSns = paySettlementOrderDTOS.stream().map(PaySettlementOrderDTO::getPaySettlementSn).distinct().collect(Collectors.toList());
					// 预售尾款只能通过支付单的金额来区分,好像不行,因为金额可能一样,但其实也没问题,直接判断支付金额和订单金额的关系
					List<PaySettlementItemDTO> paySettlementItemDTOS = paySettlementItemService.queryBySnList(paySettlementSns).stream().filter(a -> Objects.equals(a.getState(), PaySettlementStateEnum.PAID.getCode())).collect(Collectors.toList());
					BigDecimal allPayAmount = paySettlementItemDTOS.stream().map(PaySettlementItemDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
					if (allPayAmount.compareTo(orderDTOList.get(0).getActualTotalPrice()) > 0) {
						// 支付的金额大于订单金额,发生了重复支付
						orderDTOList.forEach(orderDTO -> orderDTO.setExceptionStatus(OrderExceptionStatusEnum.REPEAT_PAY.getValue()));
						orderApi.update(orderDTOList);
						log.info("重复支付检查完成,有重复支付发生!,orderNumbers:{}", orderNumbers.toString());
					}
					log.info("重复支付检查完成,没有重复支付发生,orderNumbers:{}", orderNumbers.toString());
				}
			});
		}


		// 第二种方式，依赖缓存
//		String key = orderDTOList.stream().sorted(Comparator.comparing(OrderDTO::getId)).map(OrderDTO -> OrderDTO.getId() + "").collect(Collectors.joining(",")) + "preFinal";
//		if (redisTemplate.hasKey(key)) {
//			orderDTOList.forEach(orderDTO -> orderDTO.setExceptionStatus(OrderExceptionStatusEnum.REPEAT_PAY.getValue()));
//			orderClient.update(orderDTOList);
//			log.info("重复支付检查完成,有重复支付发生!,orderNumbers:{}", key);
//			return;
//		}
//		redisTemplate.opsForValue().set(key, 1, 60, TimeUnit.SECONDS);
//		log.info("重复支付检查完成,没有重复支付发生,orderNumbers:{}", key);
	}

	@Override
	protected List<OrderHistoryDTO> convert2OrderHistoryDTO(List<OrderDTO> unpaidOrderList) {

		List<Long> orderIds = unpaidOrderList.stream().map(OrderDTO::getId).collect(Collectors.toList());
		R<List<PreSellOrderDTO>> preSellOrderResult = this.preSellOrderApi.queryByOrderIds(orderIds);
		List<PreSellOrderDTO> preSellOrderList = preSellOrderResult.getData();
		Map<Long, PreSellOrderDTO> preSellOrderMap = preSellOrderList.stream().collect(Collectors.toMap(PreSellOrderDTO::getOrderId, e -> e));

		List<OrderHistoryDTO> historyList = new ArrayList<>();
		for (OrderDTO orderDTO : unpaidOrderList) {

			PreSellOrderDTO preSellOrderDTO = preSellOrderMap.get(orderDTO.getId());

			// 更新订单日志
			OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO();
			orderHistoryDTO.setStatus(OrderHistoryEnum.ORDER_PAY.value());
			orderHistoryDTO.setOrderId(orderDTO.getId());
			orderHistoryDTO.setReason("订单: " + orderDTO.getOrderNumber() + "于" + DateUtil.formatDateTime(DateUtil.date()) + "支付：" + orderDTO.getActualTotalPrice());
			orderHistoryDTO.setCreateTime(DateUtil.date());
			historyList.add(orderHistoryDTO);
			// 记录用户账单
			this.customerBillApi.save(CustomerBillCreateDTO.builder()
					.mode(ModeTypeEnum.EXPENDITURE.value())
					.type(CustomerBillTypeEnum.GOODS.value())
					.ownerType(IdentityTypeEnum.USER.value())
					.ownerId(orderDTO.getUserId())
					.tradeExplain(orderDTO.getProductName())
					.amount(preSellOrderDTO.getFinalPrice().add(orderDTO.getFreightPrice()))
					.bizOrderNo(orderDTO.getOrderNumber())
					.innerPaymentNo(orderDTO.getPaySettlementSn())
					.relatedBizOrderNo(orderDTO.getOrderNumber())
					.payTypeId(orderDTO.getPayTypeId())
					.payTypeName(orderDTO.getPayTypeName())
					.delFlag(false)
					.status(1)
					.createTime(orderDTO.getPayTime())
					.remark("预售尾款支付").build()
			);
		}
		return historyList;
	}
}
