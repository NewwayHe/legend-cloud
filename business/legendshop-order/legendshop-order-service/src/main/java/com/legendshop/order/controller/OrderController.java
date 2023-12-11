/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller;

import cn.hutool.core.collection.CollUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.order.bo.OrderStatusNumBO;
import com.legendshop.order.dto.*;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
public class OrderController {

	private final OrderService orderService;

	/**
	 * 根据订单号集合查询订单
	 */
	@PostMapping("/queryByNumber")
	public R<List<OrderDTO>> queryByNumber(@RequestBody List<String> numberList) {
		return R.ok(orderService.queryByNumber(numberList));
	}

	@PutMapping("/update")
	public R<Integer> update(@RequestBody List<OrderDTO> orderList) {
		return R.ok(orderService.update(orderList));
	}

	@PostMapping("/orderPaySuccessSkuStock")
	public R<Void> orderPaySuccessSkuStock(@RequestBody List<String> numberList) {
		return orderService.orderPaySuccessSkuStock(numberList);
	}

	@PostMapping("/getOrderByOrderNumbersAndUserId")
	public R<List<OrderDTO>> getOrderByOrderNumbersAndUserId(@RequestBody List<String> orderNumberList, @RequestParam("userId") Long userId, @RequestParam("status") Integer status) {
		return R.ok(orderService.getOrderByOrderNumbersAndUserId(orderNumberList, userId, status));
	}

	@PostMapping("/getOrderByOrderNumbersAndStatus")
	public R<List<OrderDTO>> getOrderByOrderNumbersAndStatus(@RequestBody List<String> orderNumberList, @RequestParam("status") Integer status) {
		return R.ok(orderService.getOrderByOrderNumbersAndUserId(orderNumberList, null, status));
	}

	@PostMapping("/getListOfActivity")
	public R<List<OrderDTO>> getListOfActivity(@RequestParam("activityId") Long activityId,
											   @RequestParam("orderTypeEnum") OrderTypeEnum orderTypeEnum) {
		return R.ok(orderService.getListOfActivity(activityId, orderTypeEnum));
	}

	@PostMapping("/getPayedAndNoRefundList")
	public R<List<OrderDTO>> getPayedAndNoRefundList(Long activityId, OrderTypeEnum orderTypeEnum) {
		return R.ok(orderService.getPayedAndNoRefundList(activityId, orderTypeEnum));
	}

	@GetMapping("/getOrderByOrderItemId")
	public R<OrderDTO> getOrderByOrderItemId(@RequestParam("orderItemId") Long orderItemId) {
		return R.ok(orderService.getOrderByOrderItemId(orderItemId));
	}


	@GetMapping("/getPlatformOrderCountExceptRefundSuccess")
	public R<Long> getPlatformOrderCountExceptRefundSuccess() {
		Long userId = SecurityUtils.getUserId();
		return R.ok(orderService.getPlatformOrderCountExceptRefundSuccess(userId));
	}

	@PostMapping("/getShopOrderCountExceptRefundSuccess")
	public R<List<ShopOrderCountDTO>> getShopOrderCountExceptRefundSuccess(@RequestBody List<Long> shopIds) {
		if (CollUtil.isEmpty(shopIds)) {
			return R.ok(Collections.emptyList());
		}
		Long userId = SecurityUtils.getUserId();
		return R.ok(orderService.getShopOrderCountExceptRefundSuccess(userId, shopIds));
	}

	@PostMapping("/queryAllByIds")
	public R<List<OrderDTO>> queryAllByIds(@RequestBody List<Long> orderIds) {
		return R.ok(orderService.queryAllByIds(orderIds));
	}


	@PostMapping("/updateStatusByOrderIds")
	public R<Void> updateStatusByOrderIds(@RequestBody List<Long> orderIds, @RequestParam("status") Integer status, @RequestParam("originalStatus") Integer originalStatus) {
		orderService.updateStatusByOrderIds(orderIds, status, originalStatus);
		return R.ok();
	}

	/**
	 * 统计用户已付款的订单
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping("/getPayedOrderStatisticsByUserId")
	public R<UserOrderStatisticsDTO> getPayedOrderStatisticsByUserId(@RequestParam("userId") Long userId) {
		return orderService.getPayedOrderStatisticsByUserId(userId);
	}


	/**
	 * 统计用户已收货的订单
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping("/getReceivedOrderStatisticsByUserId")
	public R<UserOrderStatisticsDTO> getReceivedOrderStatisticsByUserId(@RequestParam("userId") Long userId) {
		return orderService.getReceivedOrderStatisticsByUserId(userId);
	}

	/**
	 * 统计用户已过售后期的订单
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping("/getReturnedOrderStatisticsByUserId")
	public R<UserOrderStatisticsDTO> getReturnedOrderStatisticsByUserId(@RequestParam("userId") Long userId) {
		return orderService.getReturnedOrderStatisticsByUserId(userId);
	}

	@PostMapping("/getOrderById")
	public R<OrderDTO> getOrderById(@RequestParam("orderId") Long orderId) {
		return R.ok(orderService.getById(orderId));
	}

	@PostMapping("/getOrderByOrderNumber")
	public R<OrderDTO> getOrderByOrderNumber(@RequestParam("orderNumber") String orderNumber) {
		return R.ok(orderService.getByOrderNumber(orderNumber));
	}

	@PostMapping("/getDistributorsOrderRecordDTO")
	public R<DistributorsOrderRecordDTO> getDistributorsOrderRecordDTO(@RequestBody DistributorsOrderRecordDTO distributorsOrderRecordDTO) {
		return orderService.getDistributorsOrderRecordDTO(distributorsOrderRecordDTO);
	}

	@PostMapping("/getDistributionOrderById")
	public R<DistributionOrderRecordDTO> getDistributionOrderById(@RequestParam("userId") Long userId) {

		return orderService.getDistributionOrderById(userId);
	}

	/**
	 * 用户的订单类型数
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping("/getOrderStatusNum")
	public R<OrderStatusNumBO> getOrderStatusNum(@RequestParam("userId") Long userId) {
		return orderService.getOrderStatusNum(userId);
	}

	@PostMapping("/getCustomOrder")
	public R<List<CustomOrderDTO>> getCustomOrder(@RequestParam(value = "userId") Long userId,
												  @RequestParam("shopId") Long shopId) {
		return orderService.getCustomOrder(userId, shopId);
	}


}
