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
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.dto.QuotaOrderDTO;
import com.legendshop.order.dto.ShopOrderBillDistributionDTO;
import com.legendshop.order.entity.OrderItem;
import com.legendshop.order.excel.ShopOrderBillDistributionExportDTO;
import com.legendshop.order.query.OrderItemQuery;
import com.legendshop.order.query.ShopOrderBillOrderQuery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单项Dao.
 *
 * @author legendshop
 */
public interface OrderItemDao extends GenericDao<OrderItem, Long> {


	List<OrderItem> getByOrderNumber(String orderNumber);

	List<OrderItem> getByOrderNumberList(List<String> orderNumberList);

	List<OrderItemDTO> getByOrderNumbers(List<String> orderNumbers);

	/**
	 * 更新订单项的退款,退货状态.
	 *
	 * @param orderItemId  the order item id
	 * @param refundStatus the refund state
	 */
	void updateRefundState(Long orderItemId, Integer refundStatus);

	/**
	 * 根据订单编号和退款状态查询订单的订单项数量.
	 *
	 * @param orderNumber 订单编号
	 * @param refundState 退款状态
	 * @return the long
	 */
	Long queryRefundItemCount(String orderNumber, Integer refundState);

	/**
	 * 根据订单流水号更新 订单下的所有订单项的退款的退款信息
	 * </br>用于订单退款申请时 , 更新订单项的信息,包括退款id,退款类型为"仅退款",退款金额等于订单项金额,退款状态为处理中.
	 *
	 * @param orderId  订单id
	 * @param refundId 退款ID
	 */
	void updateRefundInfoByOrderId(Long orderId, Long refundId);

	/**
	 * 根据订单流水号更新订单项的退款状态.
	 *
	 * @param orderNumber the Order number
	 * @param value       the value
	 */
	void updateRefundStateByOrderNumber(String orderNumber, Integer value);

	PageSupport<OrderItem> queryOrderItems(OrderItemQuery orderItemQuery);

	PageSupport<OrderItem> queryOrderItemsProd(OrderItemQuery orderItemQuery);

	/**
	 * 更新订单项 的评论状态
	 *
	 * @param orderItemId
	 * @param userId
	 * @param status      状态0未评价 1已评价
	 * @return
	 */
	boolean updateOrderItemCommFlag(Integer status, Long orderItemId, Long userId);

	/**
	 * 通过订单id列表获得它下面的订单项
	 *
	 * @param ids
	 * @return
	 */
	List<OrderItem> getByOrderIds(List<Long> ids);

	/**
	 * 通过订单id获得它下面的订单项
	 *
	 * @param id
	 * @return
	 */
	List<OrderItem> getByOrderId(Long id);

	/**
	 * 通过订单id获得它下面的订单项
	 *
	 * @param orderId
	 * @param orderItemId
	 * @param userId
	 * @return
	 */
	List<OrderItem> getByOrderId(Long orderId, Long orderItemId, Long userId);

	/**
	 * 通过订单id获得他下面的第一条订单项
	 *
	 * @param orderId
	 * @return
	 */
	OrderItem getFirstOrderItemByOrderId(Long orderId);

	/**
	 * 更新订单项的退款信息
	 *
	 * @param orderItemId
	 * @param refundId
	 * @param refundAmount
	 * @return
	 */
	void updateRefundInfoById(Long orderItemId, Long refundId, BigDecimal refundAmount, Integer refundType);

	/**
	 * 根据订单项号查询
	 *
	 * @param orderItemNumber
	 * @return
	 */
	OrderItem getOrderItemByOrderItemNumber(String orderItemNumber);

	/**
	 * 根据退款单号批量更新订单项的退款状态
	 *
	 * @param refundId
	 * @param refundStatus
	 */
	void updateByRefundId(Long refundId, Integer refundStatus);

	/**
	 * 账单结算分销佣金列表
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	PageSupport<ShopOrderBillDistributionDTO> getShopOrderBillDistributionPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	/**
	 * 账单结算分销佣金列表导出
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	List<ShopOrderBillDistributionExportDTO> exportShopOrderBillDistributionPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	/**
	 * 通过用户id获取订单信息
	 *
	 * @param userId
	 * @return
	 */
	List<OrderItemDTO> queryByUserId(Long userId);

	/**
	 * 通过订单id获取订单信息
	 *
	 * @param orderNumber
	 * @return
	 */
	List<OrderItemDTO> queryByOrderNumber(String orderNumber);

	/**
	 * 修改订单项结算记录
	 *
	 * @param distributionOrderItemNumbers
	 * @param value
	 * @return
	 */
	int updateCommissionSettleStatus(List<String> distributionOrderItemNumbers, Integer value);


	/**
	 * 通过订单编号获取订单项信息
	 *
	 * @param orderNumbers
	 * @return
	 */
	List<OrderItem> queryByOrderNumbers(List<String> orderNumbers);

	/**
	 * 获取售后期结束未分账的订单
	 *
	 * @return
	 */
	List<OrderItemDTO> getExpiredDivideOrder();

	/**
	 * 通过售后id获取订单项
	 *
	 * @param refundId
	 * @return
	 */
	List<OrderItem> queryByRefundId(Long refundId);

	/**
	 * 根据订单id查询订单项和关联商品状态
	 *
	 * @param orderId
	 * @return
	 */
	List<OrderItemDTO> queryDetailByOrderId(Long orderId);

	/**
	 * 根据订单id查询订单项和关联商品状态
	 *
	 * @param orderIds
	 * @return
	 */
	List<OrderItemDTO> queryDetailByOrderId(List<Long> orderIds);

	/**
	 * 根据订单项编号获取订单项信息
	 *
	 * @param orderItemNumbers
	 * @return
	 */
	List<OrderItem> queryByOrderItemNumber(List<String> orderItemNumbers);


	/**
	 * 获取最近一次消费订单
	 *
	 * @param
	 * @param
	 * @return
	 */
	Integer getPayedQuptaOrderStatisticsByUserId(QuotaOrderDTO quotaOrderDTO, Date beginTime, Date endTime);

	/**
	 * 查询订单项售后时间为空的列表
	 *
	 * @return
	 */
	List<OrderItemDTO> queryReturnDeadlineIsNull();
}
