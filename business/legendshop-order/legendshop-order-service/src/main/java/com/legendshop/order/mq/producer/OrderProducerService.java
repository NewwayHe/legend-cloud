/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.mq.producer;

import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.entity.Order;
import com.legendshop.order.enums.OrderTypeEnum;

import java.util.Date;
import java.util.List;

/**
 * 订单相关的mq生产者
 *
 * @author legendshop
 */
public interface OrderProducerService {

	/**
	 * MQ发送延迟队列，到指定时间后系统默认确认收货
	 *
	 * @param orderId      订单号
	 * @param receivingDay
	 */
	void autoConfirmDelivery(Long orderId, Integer receivingDay);

	/**
	 * 下单保存订单历史
	 *
	 * @param orderId 订单ID
	 * @param userId  用户ID
	 */
	void saveOrderHistory(Long orderId, Long userId);


	/**
	 * 下单扣减库存 SubmitOrderShopDTO
	 *
	 * @param submitOrderShopDtoJsonStr
	 */
	void deductionStock(String submitOrderShopDtoJsonStr);


	/**
	 * 保存商品快照 List<OrderItemDTO>
	 *
	 * @param orderItemDtoListJsonStr
	 */
	void saveProductSnapshot(String orderItemDtoListJsonStr);


	/**
	 * MQ发送延迟队列，自动请取消超时未支付订单
	 *
	 * @param orderDTO  订单
	 * @param orderType 订单类型
	 */
	void autoCancelUnPayOrder(OrderDTO orderDTO, String orderType);

	/**
	 * MQ发送延迟队列，到指定时间后系统默认商家同意退款申请
	 *
	 * @param refundId 退款订单id
	 */
	void autoAgreeRefund(Long refundId);

	/**
	 * MQ发送延迟队列，到指定时间后系统默认平台同意退款申请
	 *
	 * @param refundId 退款订单id
	 */
	void autoAgreeAdminRefund(Long refundId, OrderTypeEnum orderTypeEnum);


	/**
	 * MQ发送延迟队列，自动取消售后订单
	 *
	 * @param refundId 退款订单id
	 */
	void autoCancelRefund(Long refundId);

	/**
	 * MQ发送延迟队列，商家超市为确认收货时自动确认收货
	 *
	 * @param refundId 退款订单id
	 */
	void autoRefundConfirmDelivery(Long refundId);

	/**
	 * 预售尾款未支付处理
	 *
	 * @param orderId
	 * @param delay
	 */
	void balanceHandler(Long orderId, Date delay);

	/**
	 * 发送用户购买力下单数据队列
	 *
	 * @param list
	 */
	void sendNewOrderMessage(List<Order> list);

	/**
	 * 发送用户购买力成交数据队列
	 *
	 * @param order
	 */
	void sendDealOrderMessage(Order order);


	/**
	 * 发起分账
	 *
	 * @param numberList 分账订单流水号
	 */
	void applyDivide(List<String> numberList);

	/**
	 * 处理待评价订单
	 *
	 * @param id
	 */
	void autoTreatComment(Long id, Integer commentValidDay);


}
