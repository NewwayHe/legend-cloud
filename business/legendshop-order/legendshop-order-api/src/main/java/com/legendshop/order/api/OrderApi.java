/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.order.bo.OrderStatusNumBO;
import com.legendshop.order.dto.*;
import com.legendshop.order.enums.OrderTypeEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "orderApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface OrderApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;


	@PostMapping(PREFIX + "/queryByNumber")
	R<List<OrderDTO>> queryByNumber(@RequestBody List<String> numberList);


	@PutMapping(PREFIX + "/update")
	R<Integer> update(@RequestBody List<OrderDTO> orderList);


	@PostMapping(PREFIX + "/orderPaySuccessSkuStock")
	R<Void> orderPaySuccessSkuStock(@RequestBody List<String> numberList);


	@PostMapping(PREFIX + "/getOrderByOrderNumbersAndUserId")
	R<List<OrderDTO>> getOrderByOrderNumbersAndUserId(@RequestBody List<String> orderNumberList, @RequestParam("userId") Long userId, @RequestParam("status") Integer status);


	@PostMapping(PREFIX + "/getOrderByOrderNumbersAndStatus")
	R<List<OrderDTO>> getOrderByOrderNumbersAndStatus(@RequestBody List<String> orderNumberList, @RequestParam("status") Integer status);

	@PostMapping(PREFIX + "/getListOfActivity")
	R<List<OrderDTO>> getListOfActivity(@RequestParam("activityId") Long activityId, @RequestParam("orderTypeEnum") OrderTypeEnum orderTypeEnum);

	@PostMapping(PREFIX + "/getPayedAndNoRefundList")
	R<List<OrderDTO>> getPayedAndNoRefundList(@RequestParam("activityId") Long activityId,
											  @RequestParam("orderTypeEnum") OrderTypeEnum orderTypeEnum);

	@GetMapping(PREFIX + "/getOrderByOrderItemId")
	R<OrderDTO> getOrderByOrderItemId(@RequestParam("orderItemId") Long orderItemId);


	@GetMapping(PREFIX + "/getPlatformOrderCountExceptRefundSuccess")
	R<Long> getPlatformOrderCountExceptRefundSuccess();

	@PostMapping(PREFIX + "/getShopOrderCountExceptRefundSuccess")
	R<List<ShopOrderCountDTO>> getShopOrderCountExceptRefundSuccess(@RequestBody List<Long> shopIds);

	/**
	 * 根据orderIds查询订单
	 *
	 * @param orderIds
	 * @return
	 */
	@PostMapping(PREFIX + "/queryAllByIds")
	R<List<OrderDTO>> queryAllByIds(@RequestBody List<Long> orderIds);

	/**
	 * 批量更新订单状态
	 *
	 * @param orderIds
	 * @param status
	 * @param originalStatus
	 */
	@PostMapping(PREFIX + "/updateStatusByOrderIds")
	R<Void> updateStatusByOrderIds(@RequestBody List<Long> orderIds, @RequestParam("status") Integer status, @RequestParam("originalStatus") Integer originalStatus);

	/**
	 * 统计用户已付款的订单
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping(PREFIX + "/getPayedOrderStatisticsByUserId")
	R<UserOrderStatisticsDTO> getPayedOrderStatisticsByUserId(@RequestParam("userId") Long userId);


	/**
	 * 统计用户已收货的订单
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping(PREFIX + "/getReceivedOrderStatisticsByUserId")
	R<UserOrderStatisticsDTO> getReceivedOrderStatisticsByUserId(@RequestParam("userId") Long userId);

	/**
	 * 统计用户已过售后期的订单
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping(PREFIX + "/getReturnedOrderStatisticsByUserId")
	R<UserOrderStatisticsDTO> getReturnedOrderStatisticsByUserId(@RequestParam("userId") Long userId);

	/**
	 * 根据orderId查询订单
	 *
	 * @param orderId
	 * @return
	 */
	@PostMapping(PREFIX + "/getOrderById")
	R<OrderDTO> getOrderById(@RequestParam("orderId") Long orderId);

	/**
	 * 根据orderNumber查询订单
	 *
	 * @param orderNumber
	 * @return
	 */
	@PostMapping(PREFIX + "/getOrderByOrderNumber")
	R<OrderDTO> getOrderByOrderNumber(@RequestParam("orderNumber") String orderNumber);


	/**
	 * 根据 用户id 时间 查询 订单金额  订单次数
	 */
	@PostMapping(PREFIX + "/getDistributorsOrderRecordDTO")
	R<DistributorsOrderRecordDTO> getDistributorsOrderRecordDTO(@RequestBody DistributorsOrderRecordDTO distributorsOrderRecordDTO);

	@PostMapping(PREFIX + "/getDistributionOrderById")
	R<DistributionOrderRecordDTO> getDistributionOrderById(@RequestParam("userId") Long userId);

	/**
	 * 用户的订单类型数
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping(PREFIX + "/getOrderStatusNum")
	R<OrderStatusNumBO> getOrderStatusNum(@RequestParam("userId") Long userId);

	/**
	 * 获取客服用的订单列表，默认返回20个
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	@PostMapping(PREFIX + "/getCustomOrder")
	R<List<CustomOrderDTO>> getCustomOrder(@RequestParam(value = "userId") Long userId,
										   @RequestParam("shopId") Long shopId);

	/**
	 * 用户购买力数据表数据清洗定时任务处理
	 *
	 * @return
	 */
	@GetMapping(PREFIX + "/userPurchasingDataCleaningJobHandle")
	R<Void> userPurchasingDataCleaningJobHandle();


	/**
	 * 通知商家未处理订单售后数量定时任务处理
	 *
	 * @return
	 */
	@GetMapping(PREFIX + "/shopMessageSendJobHandle")
	R<Void> shopMessageSendJobHandle();

	/**
	 * 订单自动收货补偿定时任务处理
	 *
	 * @return
	 */
	@GetMapping(PREFIX + "/autoConfirmReceiptJobHandle")
	R<Void> autoConfirmReceiptJobHandle();

}
