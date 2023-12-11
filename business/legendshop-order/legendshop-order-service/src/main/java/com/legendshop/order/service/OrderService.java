/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.logistics.kuaidi100.response.SubscribePushParamResp;
import com.legendshop.order.bo.*;
import com.legendshop.order.dto.*;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.query.OrderSearchQuery;
import com.legendshop.pay.dto.PaymentSuccessDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单服务
 *
 * @author legendshop
 */
public interface OrderService {


	/**
	 * 保存订单
	 *
	 * @param orderDTO 订单DTO
	 * @return 订单ID
	 */
	Long save(OrderDTO orderDTO);

	/**
	 * 根据ID删除订单
	 *
	 * @param id 订单ID
	 */
	void deleteById(Long id);

	/**
	 * 更新订单
	 *
	 * @param orderDTO 订单DTO
	 */
	void update(OrderDTO orderDTO);

	/**
	 * 批量更新订单
	 *
	 * @param orderList 订单DTO列表
	 * @return 更新的订单数量
	 */
	int update(List<OrderDTO> orderList);

	/**
	 * 根据ID获取订单
	 *
	 * @param orderId 订单ID
	 * @return 订单DTO
	 */
	OrderDTO getById(Long orderId);

	/**
	 * 根据订单号查找订单
	 *
	 * @param orderNumber 订单号
	 * @return 订单DTO
	 */
	OrderDTO getByOrderNumber(String orderNumber);

	/**
	 * 生成ID
	 *
	 * @return ID
	 */
	Long creatId();

	/**
	 * 调整订单费用
	 *
	 * @param orderNumber 订单号
	 * @param freight     运费
	 * @param orderAmount 订单金额
	 * @param shopId      店铺ID
	 * @return 是否成功调整订单费用
	 */
	boolean updateOrderPrice(String orderNumber, BigDecimal freight, BigDecimal orderAmount, Long shopId);

	/**
	 * 提醒发货
	 *
	 * @param orderNumber 订单号
	 * @param shopId      店铺ID
	 * @param userId      用户ID
	 * @return 提醒发货结果
	 */
	int remindDeliveryBySn(String orderNumber, Long shopId, Long userId);

	/**
	 * 查询退款的总红包金额
	 *
	 * @param subNumbers 子订单号列表
	 * @return 退款的总红包金额
	 */
	BigDecimal getReturnRedPackOffPrice(List<String> subNumbers);

	/**
	 * 获取订单及其下订单项
	 *
	 * @param orderSearchQuery 订单查询条件
	 * @return 订单及订单项的分页结果
	 */
	PageSupport<OrderBO> queryOrderWithItem(OrderSearchQuery orderSearchQuery);


	/**
	 * 后台获取订单详情
	 *
	 * @param orderId 订单ID
	 * @return 订单详情
	 */
	OrderBO getAdminOrderDetail(Long orderId);

	/**
	 * 商家获取订单详情
	 *
	 * @param orderId 订单ID
	 * @param shopId  店铺ID
	 * @return 订单详情
	 */
	OrderBO getShopOrderDetail(Long orderId, Long shopId);

	/**
	 * 用户获取订单详情
	 *
	 * @param orderId 订单ID
	 * @param userId  用户ID
	 * @return 订单详情响应
	 */
	R<OrderBO> getUserOrderDetail(Long orderId, Long userId);

	/**
	 * 后台添加订单备注
	 *
	 * @param orderNumber 订单号
	 * @param remark      备注信息
	 * @return 是否成功添加备注
	 */
	boolean insertRemark(String orderNumber, String remark);

	/**
	 * 商家添加订单备注
	 *
	 * @param orderNumber 订单号
	 * @param remark      备注信息
	 * @param shopId      店铺ID
	 * @return 是否成功添加备注
	 */
	boolean insertRemark(String orderNumber, String remark, Long shopId);

	/**
	 * 商家取消订单
	 *
	 * @param orderNumbers 订单号列表
	 * @param reason       取消原因
	 * @param shopId       店铺ID
	 * @return 是否成功取消订单
	 */
	boolean cancelOrder(List<String> orderNumbers, String reason, Long shopId);

	/**
	 * 用户取消订单
	 *
	 * @param orderNumber 订单号
	 * @param userId      用户ID
	 * @return 取消订单响应
	 */
	R<Void> cancelOrder(String orderNumber, Long userId);

	/**
	 * 钱包退还
	 *
	 * @param orderId      订单ID
	 * @param isInitiative 是否主动退还
	 */
	void giveBackUserWallet(Long orderId, Boolean isInitiative);

	/**
	 * 回退库存
	 *
	 * @param orderDTO 订单DTO
	 */
	void returnStocks(OrderDTO orderDTO);

	/**
	 * 返还优惠券
	 *
	 * @param orderNumber
	 */
	void returnCoupon(String orderNumber);

	/**
	 * 确认发货（保存物流单号）
	 *
	 * @param orderNumber
	 * @param company
	 * @param logisticsNumber
	 * @param shopId
	 * @return
	 */
	R updateLogisticsNumber(String orderNumber, String company, String logisticsNumber, Long shopId);

	/**
	 * 用户确认收货
	 *
	 * @param orderId 订单ID
	 * @param userId  用户ID
	 * @return 无
	 */
	R<Void> confirmDeliver(Long orderId, Long userId);


	/**
	 * 用户签收后、物流接口回调修改订单状态为已签收
	 *
	 * @param logisticsInfoResponseParam
	 * @return
	 */
	boolean pollCallBack(SubscribePushParamResp logisticsInfoResponseParam);

	/**
	 * 获取未结算且已完成的订单列表
	 *
	 * @param status   订单状态
	 * @param billFlag 结算标志
	 * @param endDate  截止日期
	 * @return 符合条件的订单列表
	 */
	List<OrderDTO> getListByStatusAndBillFlag(Integer status, Integer billFlag, Date endDate);


	/**
	 * 更改订单为已结算，加上结算号
	 *
	 * @param ids
	 * @param billSn
	 */
	void updateBillStatusAndSn(List<Long> ids, String billSn);


	/**
	 * 查询订单列表
	 *
	 * @param orderSearchQuery
	 * @return
	 */
	PageSupport<OrderDTO> queryBillOrderList(OrderSearchQuery orderSearchQuery);

	/**
	 * 再来一单
	 *
	 * @param orderNumber 订单号
	 * @param userId      用户ID
	 * @param source      来源
	 */
	R addAnotherOrder(String orderNumber, Long userId, String source);

	/**
	 * 根据订单号集合获取订单列表
	 *
	 * @param orderNumberList 订单号列表
	 * @param userId          用户ID
	 * @param status          订单状态
	 * @return 符合条件的订单列表
	 */
	List<OrderDTO> getOrderByOrderNumbersAndUserId(List<String> orderNumberList, Long userId, Integer status);

	/**
	 * 通过订单号批量查询订单信息
	 *
	 * @param numberList 订单号列表
	 * @return 符合条件的订单信息列表
	 */
	List<OrderDTO> queryByNumber(List<String> numberList);

	/**
	 * 订单支付成功后更新SKU库存
	 *
	 * @param numberList 订单号列表
	 * @return 更新SKU库存响应
	 */
	R<Void> orderPaySuccessSkuStock(List<String> numberList);


	/**
	 * 获取订单里的商品数量
	 *
	 * @param userId
	 * @param activityId
	 * @param orderType
	 * @return
	 */
	Long getSumProductQuantity(Long userId, Long activityId, OrderTypeEnum orderType);


	/**
	 * 获取订单里的sku数量
	 *
	 * @param userId
	 * @param activityId
	 * @param skuId
	 * @param orderType
	 * @return
	 */
	Long getSumSkuQuantity(Long userId, Long activityId, Long skuId, OrderTypeEnum orderType);

	/**
	 * 获取当天订单里的spu数量
	 * 除去CLOSE状态
	 *
	 * @param userId
	 * @param activityId
	 * @param orderType
	 * @param createTime
	 * @return
	 */
	Long getSumProductQuantity(Long userId, Long activityId, OrderTypeEnum orderType, Date createTime);

	/**
	 * 获取当天sku下单数量
	 * 除去CLOSE状态
	 *
	 * @param userId
	 * @param activityId
	 * @param skuId
	 * @param orderType
	 * @param createTime
	 * @return
	 */
	Long getSumSkuQuantity(Long userId, Long activityId, Long skuId, OrderTypeEnum orderType, Date createTime);


	/**
	 * 获取某个营销活动的订单集合
	 *
	 * @param activityId
	 * @param orderTypeEnum
	 * @return
	 */
	List<OrderDTO> getListOfActivity(Long activityId, OrderTypeEnum orderTypeEnum);

	/**
	 * 获取营销活动已付款未申请退款的总订单数
	 *
	 * @param activityId
	 * @param orderTypeEnum
	 * @return
	 */
	List<OrderDTO> getPayedAndNoRefundList(Long activityId, OrderTypeEnum orderTypeEnum);

	/**
	 * 逻辑删除订单
	 *
	 * @param userId
	 * @param orderId
	 * @return
	 */
	boolean deleteOrder(Long userId, Long orderId);

	/**
	 * 获取订单中心
	 *
	 * @param userId
	 * @return
	 */
	R<List<UserOrderCountDTO>> queryUserOrderCenter(Long userId);

	/**
	 * 根据订单项ID 获取订单
	 *
	 * @param orderItemId
	 * @return
	 */
	OrderDTO getOrderByOrderItemId(Long orderItemId);

	/**
	 * 批量更新订单状态
	 *
	 * @param orderIds
	 * @param status
	 * @param originalStatus
	 */
	void updateStatusByOrderIds(List<Long> orderIds, Integer status, Integer originalStatus);


	R updateLogistics(String orderNumber, Long logisticsCompanyId, String logisticsNumber, Long shopId);

	List<OrderItemDTO> getAfterSalesList(Long orderId);

	/**
	 * 通过订单状态判断该订单是否允许订单项退货退款
	 *
	 * @param completeTime
	 * @param days         允许退换货时间
	 * @return
	 */
	boolean isAllowOrderReturn(Date completeTime, Integer days);

	/**
	 * 订单导出
	 *
	 * @param query
	 * @return
	 */
	R<Void> orderExport(OrderSearchQuery query);

	/**
	 * 获取累计消费金额、累计消费订单数量
	 *
	 * @param shopId
	 * @return
	 */
	R<OrderBusinessSumDTO> getOrderSumByShopId(Long shopId);


	/**
	 * 订单免付操作
	 */
	R<PaymentSuccessDTO> orderFree(List<String> orderNumberList, VisitSourceEnum sourceEnum);

	/**
	 * 开具发票
	 *
	 * @param shopId
	 * @param orderNumber
	 * @return
	 */
	R<Void> invoicing(Long shopId, String orderNumber);

	/**
	 * 批量开票
	 *
	 * @param shopId
	 * @param ids
	 * @return
	 */
	R<Void> batchInvoicing(Long shopId, List<String> ids);

	/**
	 * 获取平台订单数量，除了售后成功的订单
	 *
	 * @param userId
	 * @return
	 */
	Long getPlatformOrderCountExceptRefundSuccess(Long userId);

	/**
	 * 获取店铺订单数量，除了售后成功的订单
	 *
	 * @param userId
	 * @return
	 */
	List<ShopOrderCountDTO> getShopOrderCountExceptRefundSuccess(Long userId, List<Long> shopIds);


	/**
	 * 根据orderIds查询订单
	 *
	 * @param orderIds
	 * @return
	 */
	List<OrderDTO> queryAllByIds(List<Long> orderIds);


	/**
	 * 统计用户已付款的订单
	 *
	 * @param userId
	 * @return
	 */
	R<UserOrderStatisticsDTO> getPayedOrderStatisticsByUserId(Long userId);

	/**
	 * 统计用户已收货的订单
	 *
	 * @param userId
	 * @return
	 */
	R<UserOrderStatisticsDTO> getReceivedOrderStatisticsByUserId(Long userId);

	/**
	 * 统计用户已过售后期的订单
	 *
	 * @param userId
	 * @return
	 */
	R<UserOrderStatisticsDTO> getReturnedOrderStatisticsByUserId(Long userId);

	R<DistributorsOrderRecordDTO> getDistributorsOrderRecordDTO(DistributorsOrderRecordDTO distributorsOrderRecordDTO);

	R<DistributionOrderRecordDTO> getDistributionOrderById(Long userId);

	/**
	 * 商家首页订单信息数量统计(未付款、未发货、未签收、未评论、未处理售后)
	 *
	 * @param shopId
	 * @return
	 */
	OrderInfoCountsBO getShopOrderCount(Long shopId);

	/**
	 * 商家首页支付订单统计(今日、昨日、较昨日)
	 *
	 * @param shopId
	 * @return
	 */
	PaidOrderToDayBO getShopPaidOrder(Long shopId);

	/**
	 * 获取待处理事项
	 *
	 * @param shopId
	 * @return
	 */
	PendingMattersShopBO getPending(Long shopId);

	/**
	 * 获取首页销售额-数据
	 *
	 * @param shopId
	 * @return
	 */
	List<PaidOrderCountsBO> getShopSales(Long shopId);

	/**
	 * 查询所有待通知的商家（未处理订单数量和未处理售后数量）
	 *
	 * @return
	 */
	List<OrderShopMessageNoticeBO> queryAllShopOrderNotice();

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
	R<OrderStatusNumBO> getOrderStatusNum(Long userId);


	/**
	 * 批量发货
	 *
	 * @param shopId   the 商家Id
	 * @param shopName the 操作人
	 * @param file     上传文件
	 */
	R<Void> batchInsertLogistics(Long shopId, String shopName, MultipartFile file) throws IOException;

	/**
	 * 商家端修改物流公司
	 *
	 * @param id                 订单物流信息id
	 * @param orderNumber        订单号
	 * @param logisticsCompanyId 物流公司ID
	 * @param logisticsNumber    物流单号
	 * @param shopId             商家id
	 * @return
	 */
	R<String> updateLogisticsCompanyOrder(Long id, String orderNumber, Long logisticsCompanyId, String logisticsNumber, Long shopId);

	/**
	 * 用户端修改物流公司
	 *
	 * @param id                 订单物流信息id
	 * @param orderNumber        订单号
	 * @param logisticsCompanyId 物流公司ID
	 * @param logisticsNumber    物流单号
	 * @param userId             用户id
	 * @return
	 */
	R<String> updateLogisticsUser(Long refundId, Long id, String orderNumber, Long logisticsCompanyId, String logisticsNumber, Long userId);

	/**
	 * 获取客服用的订单列表，默认20个
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	R<List<CustomOrderDTO>> getCustomOrder(Long userId, Long shopId);


	List<OrderDTO> unReceiptOrder();

	/**
	 * 用户购买力数据表数据清洗定时任务处理
	 *
	 * @return
	 */
	R<Void> userPurchasingDataCleaningJobHandle();


	/**
	 * 通知商家未处理订单售后数量定时任务处理
	 *
	 * @return
	 */
	R<Void> shopMessageSendJobHandle();

	/**
	 * 订单自动收货补偿定时任务处理
	 *
	 * @return
	 */
	R<Void> autoConfirmReceiptJobHandle();

}
