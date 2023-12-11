/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.order.bo.OrderBO;
import com.legendshop.order.bo.OrderShopMessageNoticeBO;
import com.legendshop.order.bo.OrderStatusNumBO;
import com.legendshop.order.bo.PaidOrderCountsBO;
import com.legendshop.order.dto.*;
import com.legendshop.order.entity.Order;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.excel.OrderExportDTO;
import com.legendshop.order.excel.ShopOrderBillOrderExportDTO;
import com.legendshop.order.excel.ShopOrderBillOrderIntegralExportDTO;
import com.legendshop.order.query.OrderSearchQuery;
import com.legendshop.order.query.ShopOrderBillOrderQuery;
import com.legendshop.user.dto.UserAddressDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单dao接口类
 *
 * @author legendshop
 */
public interface OrderDao extends GenericDao<Order, Long> {

	Order getByOrderNumber(String orderNumber);

	Order getByLogisticsNumber(String logisticsNumber);

	/**
	 * 通过订单编号和用户id获取订单
	 *
	 * @param orderNumber
	 * @param userId
	 * @return
	 */
	Order getByOrderNumberByUserId(String orderNumber, Long userId);

	/**
	 * 通过订单id和用户id获取订单
	 *
	 * @param orderId
	 * @param userId
	 * @return
	 */
	Order getByOrderIdAndUserId(Long orderId, Long userId);

	Order getByOrderNumberByShopId(String orderNumber, Long ShopId);


	int updateRefundState(Long OrderId, Integer refundState);

	/**
	 * 订单发货
	 *
	 * @param orderId
	 * @return
	 */
	int shipOrder(Long orderId);


	/**
	 * 订单已配送
	 *
	 * @param orderId 订单id
	 * @return
	 */
	int deliverOrder(Long orderId);

	/**
	 * 完成订单（确认收货）
	 *
	 * @param orderId 订单id
	 * @return
	 */
	int confirmDeliverOrder(Long orderId);


	Order getOrderByOrderItemId(Long orderItemId);


	/**
	 * 修改订单提醒发货状态
	 *
	 * @param orderNumber
	 * @param userId
	 * @return
	 */
	int remindDeliveryBySN(String orderNumber, Long userId);


	List<Order> getOrderByOrderNumbersAndUserId(List<String> orderNumbers, Long userId, Integer status);


	/**
	 * 查询退款的总红包金额
	 */
	BigDecimal getReturnRedPackOffPrice(List<String> orderNumbers);


	/**
	 * 获取订单及下面的订单项
	 *
	 * @param orderSearchQuery
	 * @return
	 */
	PageSupport<OrderBO> queryOrderWithItem(OrderSearchQuery orderSearchQuery);

	OrderBO getAdminOrderDetail(Long orderId);

	OrderBO getUserOrderDetail(Long orderId, Long userId);

	OrderBO getShopOrderDetail(Long orderId, Long shopId);

	/**
	 * 获取未结算而且已完成的订单
	 *
	 * @param status
	 * @param billFlag
	 * @return
	 */
	List<Order> getListByStatusAndBillFlag(Integer status, Integer billFlag, Date endDate);

	/**
	 * 更改订单为已结算，加上结算号
	 *
	 * @param ids
	 * @param billSn
	 */
	void updateBillStatusAndSn(List<Long> ids, String billSn);

	/**
	 * 取消订单
	 *
	 * @param id
	 * @return
	 */
	int cancelUnPayOrder(Long id);

	/**
	 * 批量取消订单
	 *
	 * @param ids
	 * @return
	 */
	int batchCancelUnPayOrder(List<Long> ids);

	/**
	 * 取消预售，未付尾款的订单
	 */
	int cancelPreSellOrder(List<Long> ids);

	/**
	 * 查询订单列表
	 *
	 * @param orderSearchQuery
	 * @return
	 */
	PageSupport<Order> queryOrderList(OrderSearchQuery orderSearchQuery);

	List<Order> queryByNumber(List<String> numberList);

	List<Long> queryCloseOrderIds(List<Long> orderIds);

	/**
	 * 获取订单里的spu数量
	 * 除去CLOSE状态
	 *
	 * @param userId
	 * @param activityId
	 * @param orderType
	 * @param status
	 * @return
	 */
	Long getSumProductQuantity(Long userId, Long activityId, String orderType, Integer status);

	/**
	 * 获取当天订单里的spu数量
	 * 除去CLOSE状态
	 *
	 * @param userId
	 * @param activityId
	 * @param orderType
	 * @param status
	 * @param createTime
	 * @return
	 */
	Long getSumProductQuantity(Long userId, Long activityId, String orderType, Integer status, Date createTime);


	/**
	 * 获取订单里的sku数量
	 * 除去CLOSE状态
	 *
	 * @param userId
	 * @param activityId
	 * @param skuId
	 * @param orderType
	 * @param status
	 * @return
	 */
	Long getSumSkuQuantity(Long userId, Long activityId, Long skuId, String orderType, Integer status);

	/**
	 * 获取当天sku下单数量
	 * 除去CLOSE状态
	 *
	 * @param userId
	 * @param activityId
	 * @param skuId
	 * @param orderType
	 * @param status
	 * @param createTime
	 * @return
	 */
	Long getSumSkuQuantity(Long userId, Long activityId, Long skuId, String orderType, Integer status, Date createTime);


	/**
	 * 获取某个营销活动的订单集合
	 *
	 * @param activityId
	 * @param orderType
	 * @return
	 */
	List<Order> getListOfActivity(Long activityId, String orderType);

	/**
	 * 获取已支付未申请退款的订单
	 *
	 * @param activityId
	 * @param orderType
	 * @return
	 */
	List<Order> getPayedAndNoRefundList(Long activityId, String orderType);

	List<UserOrderCountDTO> queryUserOrderCenter(Long userId);

	/**
	 * 营销活动（秒杀、拼团、团购）查询已下单(不含失效)的订单项
	 *
	 * @param searchQuery
	 * @return
	 */
	List<OrderItemDTO> queryActivityOrder(OrderSearchQuery searchQuery);

	/**
	 * 订单导出
	 *
	 * @param query
	 * @return
	 */
	List<OrderExportDTO> orderExport(OrderSearchQuery query);

	/**
	 * 商城结算订单查询
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	PageSupport<ShopOrderBillOrderDTO> getShopBillOrderPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	/**
	 * 商城结算订单查询导出
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	List<ShopOrderBillOrderExportDTO> exportShopOrderBillOrderPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	/**
	 * 获取累计消费金额、累计消费订单数量
	 *
	 * @param shopId
	 * @return
	 */
	OrderBusinessSumDTO getOrderSumByShopId(Long shopId);

	/**
	 * 获取累计退款金额
	 *
	 * @param shopId
	 * @return
	 */
	BigDecimal getRefundAmountByShopId(Long shopId);

	/**
	 * 更新发票标识
	 *
	 * @param shopId
	 * @param orderNumber
	 * @return
	 */
	int invoicing(Long shopId, String orderNumber);

	/**
	 * 批量开票
	 *
	 * @param shopId
	 * @param ids
	 * @return
	 */
	Boolean batchInvoicing(Long shopId, List<String> ids);

	/**
	 * 获取商家某个状态的订单列表
	 *
	 * @param orderNumbers
	 * @param shopId
	 * @param status
	 * @return
	 */
	List<Order> queryByNumberAndShopIdAndStatus(List<String> orderNumbers, Long shopId, Integer status);

	/**
	 * 获取平台订单数，除了售后成功的订单
	 *
	 * @param userId
	 * @return
	 */
	Long getPlatformOrderCountExceptRefundSuccess(Long userId);

	/**
	 * 获取店铺订单数，除了售后成功的订单
	 *
	 * @param userId
	 * @return
	 */
	List<ShopOrderCountDTO> getShopOrderCountExceptRefundSuccess(Long userId, List<Long> shopIds);

	/**
	 * 修改拼团下所有订单状态
	 *
	 * @param orderIds
	 * @param status
	 * @param originalStatus
	 */
	void updateStatusByOrderIds(List<Long> orderIds, Integer status, Integer originalStatus);

	/**
	 * 修改订单状态待收货
	 *
	 * @param orderId
	 * @param logisticsReceivingTime 签收时间
	 */
	Integer updateTakeDeliverStatus(Long orderId, Date logisticsReceivingTime);

	/**
	 * 统计用户已付款的订单
	 *
	 * @param userId
	 * @return
	 */
	UserOrderStatisticsDTO getPayedOrderStatisticsByUserId(Long userId);

	/**
	 * 统计用户已收货的订单
	 *
	 * @param userId
	 * @return
	 */
	UserOrderStatisticsDTO getReceivedOrderStatisticsByUserId(Long userId);

	/**
	 * 获取对应状态的用户订单
	 *
	 * @param userId
	 * @param status
	 * @return
	 */
	List<Order> queryByUserIdAndStatus(Long userId, Integer status);

	/**
	 * 更新订单售后截止时间
	 *
	 * @param orderId
	 * @param finalReturnDeadline
	 * @return
	 */
	int updateFinalReturnDeadline(Long orderId, Date finalReturnDeadline);

	DistributorsOrderRecordDTO getDistributorsOrderRecordDTO(DistributorsOrderRecordDTO distributorsOrderRecordDTO);

	/**
	 * 商城结算积分订单查询导出
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	List<ShopOrderBillOrderIntegralExportDTO> exportShopOrderBillIntegralOrderPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	DistributionOrderRecordDTO getDistributionOrderById(Long userId);

	/**
	 * 根据订单状态统计数量
	 *
	 * @param shopId 商家id
	 * @param status 订单状态
	 * @return
	 */
	Integer getShopOrderCount(Long shopId, Integer status);

	/**
	 * 统计未评论数量
	 *
	 * @param shopId
	 * @param status
	 * @param commFlag
	 * @return
	 */
	Integer getShopOrderCommCount(Long shopId, Integer status, Integer commFlag);

	/**
	 * 统计未处理售后订单数量
	 *
	 * @param shopId
	 * @param refundStatus
	 * @return
	 */
	Integer getShopOrderRefundCount(Long shopId, Integer refundStatus);

	/**
	 * 获取商家支付订单
	 *
	 * @param shopId
	 * @param startDate
	 * @param endDate
	 * @return PaidOrderCountsBO
	 */
	PaidOrderCountsBO getShopPaidOrderCount(Long shopId, Date startDate, Date endDate);

	/**
	 * 获取被举报商品数量
	 *
	 * @param shopId
	 * @return
	 */
	Integer getShopAccusationProduct(Long shopId);

	/**
	 * 获取待开发票订单
	 *
	 * @param shopId
	 * @return
	 */
	Integer getShopOrderInvoiceCount(Long shopId);

	/**
	 * 获取首页销售额-数据
	 *
	 * @param shopId    商家id
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return List<PaidOrderCountsBO>-最近30天数据
	 */
	List<PaidOrderCountsBO> getShopSales(Long shopId, Date startDate, Date endDate);


	/**
	 * 查询所有有未发货或者未处理售后订单的商家
	 *
	 * @return
	 */
	List<OrderShopMessageNoticeBO> queryAllShopOrderNotice();


	/**
	 * 获取收货人详情
	 *
	 * @param orderId
	 * @return
	 */
	UserAddressDTO getAddressByOrderId(Long orderId);

	/**
	 * 获取完成订单的售后时间为空的订单列表
	 *
	 * @return
	 */
	List<OrderDTO> queryReturnDeadlineIsNull();

	/**
	 * 用户的订单类型数
	 *
	 * @param userId
	 * @return
	 */
	List<OrderStatusNumDTO> getOrderStatusNum(Long userId);

	/**
	 * 用户的订单待评价数
	 *
	 * @param userId
	 * @return
	 */
	OrderStatusNumDTO getUnCommCountNum(Long userId);

	/**
	 * 获取发票数据
	 *
	 * @param userId
	 * @return
	 */
	OrderStatusNumBO getUserOrderInvoiceCount(Long userId);

	/**
	 * 根据订单id和店铺id 查询订单
	 *
	 * @param orderId
	 * @param shopId
	 * @return
	 */
	Order getByOrderIdAndShopId(Long orderId, Long shopId);

	/**
	 * 根据订单id订单
	 *
	 * @param orderIds
	 * @return
	 */
	List<Order> getByOrderIdList(List<Long> orderIds);

	/**
	 * @param shopId     店铺id
	 * @param numberList 订单号集合
	 * @return
	 */
	List<Order> queryByNumberAndShopId(Long shopId, List<String> numberList);

	/**
	 * 查询待发货的订单
	 *
	 * @param shopId
	 * @param waitDelivery
	 * @return
	 */
	List<Order> queryByStatus(Long shopId, OrderStatusEnum waitDelivery);

	/**
	 * 查询待发货没有售后的订单
	 *
	 * @param shopId
	 * @return
	 */
	List<WaitDeliveryOrderDTO> queryByStatusAddress(Long shopId);

	/**
	 * 获取20个客服用的订单
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	List<OrderDTO> getCustomOrder(Long userId, Long shopId);

	List<OrderDTO> unReceiptOrder();
}
