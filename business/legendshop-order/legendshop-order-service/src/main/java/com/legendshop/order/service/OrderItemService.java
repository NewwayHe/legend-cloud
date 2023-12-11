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
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.dto.QuotaOrderDTO;
import com.legendshop.order.query.OrderItemQuery;

import java.util.List;

/**
 * 订单项服务
 *
 * @author legendshop
 */
public interface OrderItemService {

	OrderItemDTO getById(Long id);

	List<OrderItemDTO> queryById(List<Long> ids);

	Boolean update(OrderItemDTO orderItem);

	int update(List<OrderItemDTO> orderItemDTOS);

	List<OrderItemDTO> getByOrderNumber(String orderNumber);


	/**
	 * 查询订单项列表
	 *
	 * @param orderItemQuery
	 * @return
	 */
	PageSupport<OrderItemDTO> queryOrderItems(OrderItemQuery orderItemQuery);


	/**
	 * 查询订单项商品列表
	 *
	 * @param orderItemQuery
	 * @return
	 */
	PageSupport<OrderItemDTO> queryOrderItemsProd(OrderItemQuery orderItemQuery);

	/**
	 * @param status      评价状态0未评价 1已评价
	 * @param orderItemId 订单ID
	 * @param userId      用户ID
	 * @return
	 */
	boolean updateOrderItemCommFlag(Integer status, Long orderItemId, Long userId);

	/**
	 * 批量保存订单项
	 *
	 * @param orderItemDTOS
	 * @return
	 */
	Boolean saveOrderItems(List<OrderItemDTO> orderItemDTOS);

	/**
	 * 订单项更新商品快照Id
	 */
	R<Void> updateProductSnapshot(String orderItemNumber, Long productSnapshotId);

	/**
	 * 通过用户id获取订单信息
	 *
	 * @param userId
	 * @return
	 */
	List<OrderItemDTO> queryByUserId(Long userId);

	/**
	 * 通过订单编号获取订单信息
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
	 * 根据订单号集合查询
	 *
	 * @param orderNumbers
	 * @return
	 */
	List<OrderItemDTO> queryByOrderNumbers(List<String> orderNumbers);

	/**
	 * 获取售后期结束未分账的订单
	 *
	 * @return
	 */
	List<OrderItemDTO> getExpiredDivideOrder();

	R<List<OrderItemDTO>> queryByNumberList(List<String> numberList);


	/**
	 * 获取购买次数
	 *
	 * @param quotaOrderDTO
	 */
	Integer getQuotaorderSUM(QuotaOrderDTO quotaOrderDTO);

	/**
	 * 查询订单项售后时间为空的订单项
	 *
	 * @return
	 */
	List<OrderItemDTO> queryReturnDeadlineIsNull();

}
