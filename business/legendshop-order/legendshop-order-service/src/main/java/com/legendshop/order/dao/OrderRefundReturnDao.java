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
import com.legendshop.order.bo.OrderRefundReturnBO;
import com.legendshop.order.dto.OrderCancelReasonDTO;
import com.legendshop.order.dto.OrderStatusNumDTO;
import com.legendshop.order.dto.ShopOrderBillRefundDTO;
import com.legendshop.order.entity.OrderRefundReturn;
import com.legendshop.order.excel.ShopOrderBillRefundExportDTO;
import com.legendshop.order.query.OrderCancelReasonQuery;
import com.legendshop.order.query.OrderRefundReturnBillQuery;
import com.legendshop.order.query.OrderRefundReturnQuery;
import com.legendshop.order.query.ShopOrderBillOrderQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * 退款表Dao
 *
 * @author legendshop
 */
public interface OrderRefundReturnDao extends GenericDao<OrderRefundReturn, Long> {


	PageSupport<OrderRefundReturn> getByBillSnPage(OrderRefundReturnQuery orderRefundReturnQuery);

	PageSupport<OrderRefundReturn> getPage(OrderRefundReturnQuery orderRefundReturnQuery);


	/**
	 * 通过退款编号和用户Id获取退款订单
	 *
	 * @param refundSn
	 * @param userId
	 * @return
	 */
	OrderRefundReturn getReturnByRefundSnByUserId(String refundSn, Long userId);


	/**
	 * 获取未结算而且已完成的退款/退货单
	 *
	 * @return
	 */
	List<OrderRefundReturn> getListByStatusAndBillFlag(OrderRefundReturnBillQuery query);


	/**
	 * 更改退款/退货单为已结算，加上结算号
	 *
	 * @param ids
	 * @param billSn
	 */
	void updateBillStatusAndSn(List<Long> ids, String billSn);

	/**
	 * 通过物流单号获取售后订单
	 *
	 * @param logisticsNumber
	 * @return
	 */
	OrderRefundReturn getByLogisticsNumber(String logisticsNumber);

	/**
	 * 退款列表分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<OrderRefundReturnBO> page(OrderRefundReturnQuery query);

	/**
	 * 用户查看售后详情
	 *
	 * @param refundId
	 * @param userId
	 * @return
	 */
	OrderRefundReturnBO getUserRefundDetail(Long refundId, Long userId);

	/**
	 * 商家查看售后详情
	 *
	 * @param refundId
	 * @param shopId
	 * @return
	 */
	OrderRefundReturnBO getShopRefundDetail(Long refundId, Long shopId);

	/**
	 * 平台查看售后详情
	 *
	 * @param refundId
	 * @return
	 */
	OrderRefundReturnBO getAdminRefundDetail(Long refundId);

	/**
	 * 获取账单档期内的退款订单数据
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	PageSupport<ShopOrderBillRefundDTO> getShopOrderBillRefundPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	/**
	 * 获取账单档期内的退款订单数据导出
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	List<ShopOrderBillRefundExportDTO> exportShopOrderBillRefundPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	/**
	 * 获取用户累计退款金额
	 *
	 * @param userId
	 * @return
	 */
	BigDecimal getRefundAmountByUserId(Long userId);

	/**
	 * 通过订单编号计算退款金额
	 *
	 * @param orderNumbers
	 * @return
	 */
	BigDecimal getRefundAmountByOrderNumbers(List<String> orderNumbers);

	/**
	 * 根据退款单号获取退款信息
	 *
	 * @param refundSn
	 * @return
	 */
	OrderRefundReturn getByRefundSn(String refundSn);

	OrderRefundReturn getReturnByRefundSn(String refundSn);

	/**
	 * 查询商家所有未处理的售后单
	 *
	 * @return
	 */
	List<OrderRefundReturn> queryUntreatedOrderRefund();

	/**
	 * 根据订单id查询售后表
	 *
	 * @param orderId
	 * @return
	 */
	List<OrderRefundReturn> queryByOrderId(Long orderId);

	/**
	 * 退货物流更新为已收货
	 *
	 * @param refundId
	 * @return
	 */
	Integer updateLogisticsReceived(Long refundId);

	/**
	 * 退货物流更新为待收货
	 *
	 * @param refundId
	 * @return
	 */
	Integer updateLogisticsWaitReceive(Long refundId);

	/**
	 * 统计订单售后状态数
	 *
	 * @param userId
	 * @return
	 */
	List<OrderStatusNumDTO> getOrderStatusNum(Long userId);

	/**
	 * 商家申请订单列表
	 *
	 * @param query
	 * @return
	 */
	PageSupport<OrderCancelReasonDTO> pageCancelOrder(OrderCancelReasonQuery query);

	/**
	 * 用户退款退货 商家申请取消订单
	 *
	 * @param query
	 * @return
	 */
	PageSupport<OrderRefundReturnBO> pageOrderRefundUser(OrderRefundReturnQuery query);

	/**
	 * 根据订单id查询 退款单
	 */
	List<OrderRefundReturn> getByOrderId(List<Long> orderId);


	/**
	 * 根据退款退货id查询 退款单集合
	 */
	List<OrderRefundReturn> getByRefundId(List<Long> orderId);

	/**
	 * 售后原因
	 *
	 * @param shopId
	 */
	List<OrderRefundReturn> queryAfterSalesReason(Long shopId, Long applyType);
}




