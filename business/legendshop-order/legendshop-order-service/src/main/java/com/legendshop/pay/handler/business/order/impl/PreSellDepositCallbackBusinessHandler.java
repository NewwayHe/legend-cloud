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
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.order.api.PreSellOrderApi;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.OrderHistoryDTO;
import com.legendshop.order.dto.PreSellOrderDTO;
import com.legendshop.order.enums.OrderExceptionStatusEnum;
import com.legendshop.order.enums.OrderHistoryEnum;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.pay.dto.PaySettlementDTO;
import com.legendshop.pay.dto.PaySettlementOrderDTO;
import com.legendshop.pay.handler.business.order.AbstractOrderCallbackBusinessHandler;
import com.legendshop.pay.mq.producer.PayProducerService;
import com.legendshop.product.enums.PreSellPayType;
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
@Component("PRE_SALE_ORDER_DEPOSIT_CALLBACK")
public class PreSellDepositCallbackBusinessHandler extends AbstractOrderCallbackBusinessHandler {

	final PreSellOrderApi preSellOrderApi;

	final PayProducerService payProducerService;

	@Override
	public List<OrderDTO> orderHandler(List<OrderDTO> orderList) {
		orderList = orderList.stream().filter(o -> !(!o.getPayedFlag() || (o.getPayedFlag() && o.getStatus().equals(OrderStatusEnum.PRESALE_DEPOSIT.getValue())))).collect(Collectors.toList());
		List<Long> orderIds = orderList.stream().map(OrderDTO::getId).collect(Collectors.toList());
		R<List<PreSellOrderDTO>> preSellResult = this.preSellOrderApi.queryByOrderIds(orderIds);
		if (!preSellResult.success()) {
			log.error("获取预售订单信息失败 ErrMsg：{}", preSellResult.getMsg());
			throw new BusinessException(preSellResult.getMsg());
		}
		List<PreSellOrderDTO> preSellOrderList = preSellResult.getData();
		orderList.forEach(order -> {
			Optional<PreSellOrderDTO> preSellOrderOptional = preSellOrderList.stream().filter(e -> e.getOrderId().equals(order.getId())).findFirst();
			if (!preSellOrderOptional.isPresent()) {
				throw new BusinessException("预售订单异常");
			}
			PreSellOrderDTO preSellOrderDTO = preSellOrderOptional.get();
			if (preSellOrderDTO.getPayPctType().equals(PreSellPayType.DEPOSIT.value())) {
				order.setStatus(OrderStatusEnum.PRESALE_DEPOSIT.getValue());
			} else if (preSellOrderDTO.getPayPctType().equals(PreSellPayType.FULL_AMOUNT.value())) {
				order.setStatus(OrderStatusEnum.WAIT_DELIVERY.getValue());
			}
		});
		return orderList;
	}

	@Override
	public void payedNotify(List<OrderDTO> orderList) {

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
		List<OrderDTO> distributionOrderList = new ArrayList<>();
		orderList.forEach(orderDTO -> {
			preSellOrderList.stream().filter(e -> ObjectUtil.equal(e.getOrderId(), orderDTO.getId())).forEach(e -> {
				e.setPayDepositFlag(Boolean.TRUE);
				e.setDepositPayTime(now);
				String payTypeId = orderDTO.getPayTypeId();
				e.setDepositPayType(payTypeId);

				BigDecimal actualAmount = e.getPreDepositPrice();
				// 如果是全款，则加上运费
				if (PreSellPayType.FULL_AMOUNT.value().equals(e.getPayPctType())) {
					// 全款预售需要计算佣金
					distributionOrderList.add(orderDTO);
					actualAmount = actualAmount.add(orderDTO.getFreightPrice());
				}
				e.setActualAmount(actualAmount);
			});
		});
		R<Integer> update = this.preSellOrderApi.update(preSellOrderList);

		Map<Long, PreSellOrderDTO> preSellOrderMap = preSellOrderList.stream().collect(Collectors.toMap(PreSellOrderDTO::getOrderId, e -> e));
		for (OrderDTO orderDTO : orderList) {
			// 如果预售订单付款成功，则发起mq延时处理尾款超时支付问题
			if (orderDTO.getStatus().equals(OrderStatusEnum.PRESALE_DEPOSIT.getValue())) {
				if (preSellOrderMap.containsKey(orderDTO.getId())) {
					payProducerService.handlePreSellFinalPaymentTimeOut(preSellOrderMap.get(orderDTO.getId()));
					payProducerService.handlePreSellFinalPaymentNotice(preSellOrderMap.get(orderDTO.getId()));
				}
			}
		}

		// 进行全款预售的其它处理
		orderList.stream().filter(e -> e.getStatus().equals(OrderStatusEnum.WAIT_DELIVERY.getValue())).collect(Collectors.toList());
		super.commissionHandler(distributionOrderList);
		log.info(" [ Pre Sell Order Deposit Callback ] SUCCESS 预售尾款支付处理完成！");
	}

	@Override
	public void checkRepeatPay(String paySettlementSn, List<OrderDTO> orderDTOList) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			// 保证在事务提交后调用
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					// 支付的订单编号
					List<String> orderNumbers = orderDTOList.stream().map(OrderDTO::getOrderNumber).distinct().collect(Collectors.toList());

					// 查询所有支付单
					List<PaySettlementOrderDTO> paySettlementOrderDTOS = paySettlementOrderService.querySnByOrderNumber(orderNumbers);
					List<String> paySettlementSns = paySettlementOrderDTOS.stream().map(PaySettlementOrderDTO::getPaySettlementSn).distinct().collect(Collectors.toList());
//					if (paySettlementSns.size() == 1 && Objects.equals(paySettlementSns.get(0), paySettlementSn)) {
//						log.info("重复支付检查完成,没有重复支付发生,orderNumbers:{}", orderNumbers.toString());
//						return;
//					}
					List<String> repeatPaySnList = paySettlementSns.stream().filter(a -> !Objects.equals(a, paySettlementSn)).collect(Collectors.toList());
					if (repeatPaySnList.size() > 0) {
						List<PaySettlementDTO> paySettlementDTOS = paySettlementService.queryPaidBySnList(repeatPaySnList);
						if (CollUtil.isNotEmpty(paySettlementDTOS)) {
							// 有重复支付
							orderDTOList.forEach(orderDTO -> orderDTO.setExceptionStatus(OrderExceptionStatusEnum.REPEAT_PAY.getValue()));
							orderApi.update(orderDTOList);
							log.info("重复支付检查完成,有重复支付发生!,orderNumbers:{}", orderNumbers.toString());
						}
					}
					log.info("重复支付检查完成,没有重复支付发生,orderNumbers:{}", orderNumbers.toString());
				}
			});
		}

		// 第二种方式，依赖缓存
//		String key = orderDTOList.stream().sorted(Comparator.comparing(OrderDTO::getId)).map(OrderDTO -> OrderDTO.getId() + "").collect(Collectors.joining(",")) + "preDeposit";
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

		String remark = "预售定金支付";
		List<OrderHistoryDTO> historyList = new ArrayList<>();
		for (OrderDTO orderDTO : unpaidOrderList) {

			PreSellOrderDTO preSellOrderDTO = preSellOrderMap.get(orderDTO.getId());
			BigDecimal actualAmount = preSellOrderDTO.getPreDepositPrice();

			// 如果是全款，则加上运费
			if (PreSellPayType.FULL_AMOUNT.value().equals(preSellOrderDTO.getPayPctType())) {
				actualAmount = actualAmount.add(orderDTO.getFreightPrice());
				remark = "预售全额支付";
			}

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
					.amount(actualAmount)
					.bizOrderNo(orderDTO.getOrderNumber())
					.innerPaymentNo(orderDTO.getPaySettlementSn())
					.relatedBizOrderNo(orderDTO.getOrderNumber())
					.payTypeId(orderDTO.getPayTypeId())
					.payTypeName(orderDTO.getPayTypeName())
					.delFlag(false)
					.status(1)
					.createTime(orderDTO.getPayTime())
					.remark(remark).build()
			);
		}
		return historyList;
	}
}
