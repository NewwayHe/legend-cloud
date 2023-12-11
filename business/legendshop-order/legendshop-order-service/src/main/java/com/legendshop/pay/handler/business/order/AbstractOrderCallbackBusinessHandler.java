/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.business.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendParamEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.order.api.OrderApi;
import com.legendshop.order.api.OrderHistoryApi;
import com.legendshop.order.api.OrderItemApi;
import com.legendshop.order.api.PreSellOrderApi;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.OrderHistoryDTO;
import com.legendshop.order.enums.*;
import com.legendshop.pay.dto.PaySettlementDTO;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.dto.PaySettlementOrderDTO;
import com.legendshop.pay.enums.PaySettlementStateEnum;
import com.legendshop.pay.handler.business.CallbackBusinessHandler;
import com.legendshop.pay.mq.producer.PayProducerService;
import com.legendshop.pay.service.PaySettlementItemService;
import com.legendshop.pay.service.PaySettlementOrderService;
import com.legendshop.pay.service.PaySettlementService;
import com.legendshop.user.api.CustomerBillApi;
import com.legendshop.user.api.UserDetailApi;
import com.legendshop.user.dto.CustomerBillCreateDTO;
import com.legendshop.user.enums.CustomerBillTypeEnum;
import com.legendshop.user.enums.IdentityTypeEnum;
import com.legendshop.user.enums.ModeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Slf4j
public abstract class AbstractOrderCallbackBusinessHandler implements CallbackBusinessHandler {

	@Autowired
	protected OrderApi orderApi;

	@Autowired
	protected OrderItemApi orderItemApi;

	@Autowired
	protected SysParamsApi sysParamsApi;

	@Autowired
	protected OrderHistoryApi orderHistoryApi;

	@Autowired
	protected CustomerBillApi customerBillApi;

	@Autowired
	protected PaySettlementService paySettlementService;

	@Autowired
	protected PaySettlementItemService paySettlementItemService;

	@Autowired
	protected PaySettlementOrderService paySettlementOrderService;

	@Autowired
	protected PayProducerService payProducerService;

	@Autowired
	protected UserDetailApi userDetailApi;

	@Autowired
	protected PreSellOrderApi preSellOrderApi;

	@Autowired
	protected MessageApi messagePushClient;

	@Autowired
	protected RedisTemplate redisTemplate;

	@Override
	public void handler(PaySettlementDTO paySettlementDTO) {
		String paySettlementSn = paySettlementDTO.getPaySettlementSn();
		List<PaySettlementOrderDTO> settlementOrderList = this.paySettlementOrderService.queryOrderBySn(paySettlementSn);
		List<PaySettlementItemDTO> settlementItemList = this.paySettlementItemService.queryBySn(paySettlementSn);
		if (CollectionUtils.isEmpty(settlementOrderList)) {
			log.error("没有可以处理的订单！");
			return;
		}
		List<String> numberList = settlementOrderList.stream().map(PaySettlementOrderDTO::getOrderNumber).collect(Collectors.toList());

		log.info(" [ ORDER CALLBACK ] 开始处理支付订单, 订单号: {}", JSONUtil.toJsonStr(numberList));

		// 获取支付类型
		List<String> payTypeIdList = settlementItemList.stream().map(PaySettlementItemDTO::getPayTypeId).collect(Collectors.toList());
		List<PayTypeEnum> payTypeList = payTypeIdList.stream().map(PayTypeEnum::valueOf).collect(Collectors.toList());

		String payTypeName = payTypeList.stream().map(PayTypeEnum::getValueName).collect(Collectors.joining(","));
		String payTypeId = payTypeList.stream().map(PayTypeEnum::name).collect(Collectors.joining(","));

		// 设置订单状态
		Date now = new Date();
		R<List<OrderDTO>> orderResult = this.orderApi.queryByNumber(numberList);
		if (!orderResult.success()) {
			log.error(" [ CallbackPayHandler ] 订单查询失败！回调处理订单号：{}, ErrMsg：{}", JSONUtil.toJsonStr(numberList), orderResult.getMsg());
		}
		List<OrderDTO> orderList = orderResult.getData();

		orderList.forEach(order -> {
			order.setPayedFlag(Boolean.TRUE);
			order.setPayTime(now);
			order.setPayTypeId(payTypeId);
			order.setPayTypeName(payTypeName);
			order.setPaySettlementSn(paySettlementSn);
			order.setStatus(OrderStatusEnum.WAIT_DELIVERY.getValue());

		});
		// 处理支付回调后的订单状态
		orderList = this.orderHandler(orderList);
		if (CollectionUtils.isEmpty(orderList)) {
			log.error(" [ CallbackPayHandler ]  没有需要更新的订单！");
			return;
		}

		List<OrderDTO> unpaidOrderList = orderList.stream().filter(o -> !o.getPayedFlag() || (o.getPayedFlag() && !o.getStatus().equals(OrderStatusEnum.PRESALE_DEPOSIT.getValue()))).collect(Collectors.toList());
		List<String> unpaidNumberList = new ArrayList<>();
		if (CollUtil.isNotEmpty(unpaidOrderList)) {
			unpaidNumberList = unpaidOrderList.stream().map(OrderDTO::getOrderNumber).collect(Collectors.toList());
		}

		R<Integer> update = this.orderApi.update(orderList);
		if (!update.getSuccess()) {
			throw new BusinessException("订单状态设置失败");
		}
		log.info(" [ ORDER CALLBACK ] 订单状态更新！");
		// 获取订单商品,判断商品扣库存方式，进行库存扣减
		if (CollUtil.isNotEmpty(unpaidNumberList)) {
			this.orderApi.orderPaySuccessSkuStock(unpaidNumberList);
		}

		for (OrderDTO orderDTO : unpaidOrderList) {
			//mq更新用户购买力支付数据
			payProducerService.sendPayMessage(orderDTO);
		}

		this.orderHistoryApi.save(convert2OrderHistoryDTO(orderList));


		// 更新支付单
		paySettlementDTO.setState(PaySettlementStateEnum.PAID.getCode());
		paySettlementDTO.setUpdateTime(now);
		this.paySettlementService.updateSettlement(paySettlementDTO);

		// 预售定金订单不处理
		if (CollUtil.isNotEmpty(unpaidNumberList)) {
			//mq更新商品购买数
			payProducerService.updateBuys(JSONUtil.toJsonStr(unpaidNumberList));


			//通过用户id分组
			Map<Long, List<OrderDTO>> orderMap = unpaidOrderList.stream().collect(Collectors.groupingBy(OrderDTO::getUserId));
			//更新用户消费统计数据
			for (Long userId : orderMap.keySet()) {
				List<OrderDTO> dtoList = orderMap.get(userId);
				BigDecimal amount = dtoList.stream().map(OrderDTO::getActualTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
				String orderType = dtoList.get(0).getOrderType();
				if (OrderTypeEnum.INTEGRAL.getValue().equals(orderType)) {
					//积分商品不用付款
					userDetailApi.updateConsumptionStatistics(userId, BigDecimal.ZERO, dtoList.size());
				} else {
					userDetailApi.updateConsumptionStatistics(userId, amount, dtoList.size());
				}
			}
		}

		payedNotify(unpaidOrderList);

		checkRepeatPay(paySettlementSn, unpaidOrderList);

		log.info(" [ ORDER CALLBACK ] 支付单状态更新！");
		try {
			this.specialBusiness(orderList);
		} catch (Exception e) {

		}
	}

	public abstract List<OrderDTO> orderHandler(List<OrderDTO> orderList);

	public void payedNotify(List<OrderDTO> orderList) {
		for (OrderDTO orderDTO : orderList) {
			// 发送支付成功通知站内信给用户
			List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
			//替换参数内容
			MsgSendParamDTO msgSendParamDTO = new MsgSendParamDTO(MsgSendParamEnum.ORDER_NUMBER, orderDTO.getOrderNumber(), "black");
			MsgSendParamDTO first = new MsgSendParamDTO(MsgSendParamEnum.FIRST, "商品购买成功，请您注意物流信息，及时收取货物", "black");
			MsgSendParamDTO keyword1 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD1, orderDTO.getOrderNumber(), "black");
			MsgSendParamDTO keyword2 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD2, orderDTO.getProductName(), "black");
			MsgSendParamDTO keyword3 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD3, orderDTO.getActualTotalPrice().toString() + "元", "black");
			MsgSendParamDTO keyword4 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD4, DateUtil.formatDateTime(orderDTO.getPayTime()), "black");
			MsgSendParamDTO keyword5 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD5, orderDTO.getShopName(), "black");
			MsgSendParamDTO remark = new MsgSendParamDTO(MsgSendParamEnum.REMARK, "感谢您的购买，欢迎下次光临", "black");
			msgSendParamDTOS.add(msgSendParamDTO);
			msgSendParamDTOS.add(first);
			msgSendParamDTOS.add(keyword1);
			msgSendParamDTOS.add(keyword2);
			msgSendParamDTOS.add(keyword3);
			msgSendParamDTOS.add(keyword4);
			msgSendParamDTOS.add(keyword5);
			msgSendParamDTOS.add(remark);

			// 发送支付成功通知站内信给用户
			List<MsgSendParamDTO> urlParamList = new ArrayList<>();
			MsgSendParamDTO orderId = new MsgSendParamDTO(MsgSendParamEnum.ORDER_ID, String.valueOf(orderDTO.getId()), "black");
			urlParamList.add(orderId);

			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
					.setReceiveIdArr(new Long[]{orderDTO.getUserId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_TO_PAY)
					.setSysParamNameEnum(SysParamNameEnum.ORDER_PAY_SUCCESS_TO_USER)
					.setMsgSendParamDTOList(msgSendParamDTOS)
					.setDetailId(orderDTO.getId())
					.setUrlParamList(urlParamList)
			);
		}
	}

	public abstract void specialBusiness(List<OrderDTO> orderList);

	/**
	 * 佣金处理
	 *
	 * @param orderList
	 */
	protected R commissionHandler(List<OrderDTO> orderList) {
		log.info(" [ ORDER CALLBACK ] 开始计算佣金 ");

		return R.ok("获取平台分销信息失败");

	}

	/**
	 * 订单转换成订单历史
	 *
	 * @param unpaidOrderList
	 * @return
	 */
	protected List<OrderHistoryDTO> convert2OrderHistoryDTO(List<OrderDTO> unpaidOrderList) {
		List<OrderHistoryDTO> historyList = new ArrayList<>();
		for (OrderDTO orderDTO : unpaidOrderList) {
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
					.amount(orderDTO.getActualTotalPrice())
					.bizOrderNo(orderDTO.getOrderNumber())
					.innerPaymentNo(orderDTO.getPaySettlementSn())
					.relatedBizOrderNo(orderDTO.getOrderNumber())
					.payTypeId(orderDTO.getPayTypeId())
					.payTypeName(orderDTO.getPayTypeName())
					.delFlag(false)
					.status(1)
					.createTime(orderDTO.getPayTime())
					.remark("订单支付").build()
			);
		}
		return historyList;
	}

	public void checkRepeatPay(String paySettlementSn, List<OrderDTO> orderDTOList) {

		// 第一种方式
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
							return;
						}
					}
					log.info("重复支付检查完成,没有重复支付发生,orderNumbers:{}", orderNumbers.toString());
				}
			});
		}


		// 第二种方式
//		String key = orderDTOList.stream().sorted(Comparator.comparing(OrderDTO::getId)).map(OrderDTO -> OrderDTO.getId() + "").collect(Collectors.joining(","));
//		if (redisTemplate.hasKey(key)) {
//			orderDTOList.forEach(orderDTO -> orderDTO.setExceptionStatus(OrderExceptionStatusEnum.REPEAT_PAY.getValue()));
//			orderClient.update(orderDTOList);
//			log.info("重复支付检查完成,有重复支付发生!,orderNumbers:{}", key);
//			return;
//		}
//		redisTemplate.opsForValue().set(key,1,60, TimeUnit.SECONDS);
//		log.info("重复支付检查完成,没有重复支付发生,orderNumbers:{}", key);

	}
}
