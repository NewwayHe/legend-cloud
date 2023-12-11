/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import cn.legendshop.jpaplus.util.StringUtils;
import com.legendshop.order.dao.OrderItemDao;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.dto.QuotaOrderDTO;
import com.legendshop.order.dto.ShopOrderBillDistributionDTO;
import com.legendshop.order.entity.OrderItem;
import com.legendshop.order.enums.OrderRefundReturnStatusEnum;
import com.legendshop.order.enums.OrderRefundReturnTypeEnum;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.enums.PayTypeEnum;
import com.legendshop.order.excel.ShopOrderBillDistributionExportDTO;
import com.legendshop.order.query.OrderItemQuery;
import com.legendshop.order.query.ShopOrderBillOrderQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 订单项服务
 *
 * @author legendshop
 */
@Repository
public class OrderItemDaoImpl extends GenericDaoImpl<OrderItem, Long> implements OrderItemDao {

	@Override
	public int update(OrderItem entity) {
		return super.updateProperties(entity);
	}

	@Override
	public int update(List<OrderItem> entities) {
		int result = 0;
		for (OrderItem entity : entities) {
			result += this.update(entity);
		}
		return result;
	}

	@Override
	public List<OrderItem> getByOrderNumber(String orderNumber) {
		return this.queryByProperties(new EntityCriterion().eq("orderNumber", orderNumber));
	}

	@Override
	public List<OrderItem> getByOrderNumberList(List<String> orderNumberList) {
		if (CollectionUtils.isEmpty(orderNumberList)) {
			return new ArrayList<>();
		}
		return this.queryByProperties(new EntityCriterion().in("orderNumber", orderNumberList));
	}

	@Override
	public List<OrderItemDTO> getByOrderNumbers(List<String> orderNumbers) {
		if (CollUtil.isEmpty(orderNumbers)) {
			return Collections.emptyList();
		}

		LambdaEntityCriterion<OrderItemDTO> lambdaEntityCriterion = new LambdaEntityCriterion<>(OrderItemDTO.class);
		return queryDTOByProperties(lambdaEntityCriterion.in(OrderItemDTO::getOrderNumber, orderNumbers));
	}


	@Override
	public void updateRefundState(Long OrderItemId, Integer refundStatus) {
		this.update("UPDATE ls_order_item SET refund_status = ? WHERE id = ?", refundStatus, OrderItemId);
	}

	@Override
	public Long queryRefundItemCount(String orderNumber, Integer refundState) {
		String sql = "SELECT COUNT(id) FROM ls_order_item WHERE order_number = ? AND  refund_status = ? ";
		return this.getLongResult(sql, orderNumber, refundState);
	}

	@Override
	public void updateRefundInfoByOrderId(Long orderId, Long refundId) {
		//退款金额原封不动的set到订单项的退款金额
		String sql = "UPDATE ls_order_item SET refund_id = ?, refund_status = ?, refund_type = ?, refund_amount = actual_amount WHERE order_id = ?";
		this.update(sql, refundId, OrderRefundReturnStatusEnum.ORDER_REFUND_PROCESSING.value(), OrderRefundReturnTypeEnum.REFUND.value(), orderId);
	}

	@Override
	public void updateRefundStateByOrderNumber(String orderNumber, Integer refundState) {
		String sql = "UPDATE ls_order_item SET refund_status = ? WHERE order_number = ?";
		this.update(sql, refundState, orderNumber);
	}

	@Override
	public PageSupport<OrderItem> queryOrderItems(OrderItemQuery orderItemQuery) {
		CriteriaQuery query = new CriteriaQuery(OrderItem.class, orderItemQuery.getPageSize(), orderItemQuery.getCurPage());
		query.eq("userId", orderItemQuery.getUserId());
		return queryPage(query);
	}

	@Override
	public PageSupport<OrderItem> queryOrderItemsProd(OrderItemQuery orderItemQuery) {
		if (ObjectUtil.isEmpty(orderItemQuery.getUserId())) {
			return new PageSupport<>();
		}
		QueryMap queryMap = new QueryMap();
		queryMap.like("productName", orderItemQuery.getProductName());
		queryMap.put("userId", orderItemQuery.getUserId());
		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(OrderItem.class, orderItemQuery);
		sqlQuery.setSqlAndParameter("OrderItem.queryOrderItemsProd", queryMap);
		return querySimplePage(sqlQuery);
	}


	@Override
	public boolean updateOrderItemCommFlag(Integer status, Long orderItemId, Long userId) {
		if (status == 0) {
			return this.update("update ls_order_item set comm_flag = ? where id = ? AND user_id = ? and comm_flag = 1", status, orderItemId, userId) > 0;
		}
		return this.update("update ls_order_item set comm_flag = ? where id = ? AND user_id = ? and comm_flag = 0", status, orderItemId, userId) > 0;
	}

	@Override
	public List<OrderItem> getByOrderIds(List<Long> ids) {
		return queryByProperties(new EntityCriterion().in("orderId", ids.toArray()));
	}

	@Override
	public List<OrderItem> getByOrderId(Long id) {
		return queryByProperties(new EntityCriterion().eq("orderId", id));
	}

	@Override
	public List<OrderItem> getByOrderId(Long orderId, Long orderItemId, Long userId) {
		return queryByProperties(new EntityCriterion(true).eq("orderId", orderId).eq("id", orderItemId).eq("userId", userId));
	}

	@Override
	public OrderItem getFirstOrderItemByOrderId(Long orderId) {
		return this.get("select oi.product_name,oi.attribute,oi.pic,oi.id,oi.integral from ls_order_item oi where oi.order_id = ? limit 0,1", OrderItem.class, orderId);
	}

	@Override
	public void updateRefundInfoById(Long orderItemId, Long refundId, BigDecimal refundAmount, Integer refundType) {
		String sql = "UPDATE ls_order_item SET refund_id = ?, refund_status = ?, refund_type = ?, refund_amount = ? WHERE id = ?";
		this.update(sql, refundId, OrderRefundReturnStatusEnum.ORDER_REFUND_PROCESSING.value(), refundType, refundAmount, orderItemId);
	}

	@Override
	public OrderItem getOrderItemByOrderItemNumber(String orderItemNumber) {
		return this.getByProperties(new EntityCriterion().eq("orderItemNumber", orderItemNumber));
	}

	@Override
	public void updateByRefundId(Long refundId, Integer refundStatus) {
		String sql = "UPDATE ls_order_item SET refund_status = ? WHERE refund_id = ?";
		this.update(sql, refundStatus, refundId);
	}

	@Override
	public PageSupport<ShopOrderBillDistributionDTO> getShopOrderBillDistributionPage(ShopOrderBillOrderQuery query) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("billSn", query.getSn());
		queryMap.put("shopId", query.getShopId());
		queryMap.put("orderNumber", query.getOrderNumber());

		if (StringUtils.isNotBlank(query.getOrderEndTime())) {
			queryMap.put("orderEndTime", DateUtil.endOfDay(DateUtil.parseDate(query.getOrderEndTime())));
		}
		if (StringUtils.isNotBlank(query.getOrderStartTime())) {
			queryMap.put("orderStartTime", DateUtil.beginOfDay(DateUtil.parseDate(query.getOrderStartTime())));
		}
		if (StringUtils.isNotBlank(query.getPayEndTime())) {
			queryMap.put("payEndTime", DateUtil.endOfDay(DateUtil.parseDate(query.getPayEndTime())));
		}
		if (StringUtils.isNotBlank(query.getPayStartTime())) {
			queryMap.put("payStartTime", DateUtil.beginOfDay(DateUtil.parseDate(query.getPayStartTime())));
		}


		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(ShopOrderBillDistributionDTO.class, query);
		sqlQuery.setSqlAndParameter("OrderItem.getShopOrderBillDistributionPage", queryMap);
		return querySimplePage(sqlQuery);
	}

	@Override
	public List<ShopOrderBillDistributionExportDTO> exportShopOrderBillDistributionPage(ShopOrderBillOrderQuery query) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("billSn", query.getSn());
		queryMap.put("shopId", query.getShopId());
		queryMap.put("orderNumber", query.getOrderNumber());
		if (StringUtils.isNotBlank(query.getOrderEndTime())) {
			queryMap.put("orderEndTime", DateUtil.endOfDay(DateUtil.parseDate(query.getOrderEndTime())));
		}
		if (StringUtils.isNotBlank(query.getOrderStartTime())) {
			queryMap.put("orderStartTime", DateUtil.beginOfDay(DateUtil.parseDate(query.getOrderStartTime())));
		}
		if (StringUtils.isNotBlank(query.getPayEndTime())) {
			queryMap.put("payEndTime", DateUtil.endOfDay(DateUtil.parseDate(query.getPayEndTime())));
		}
		if (StringUtils.isNotBlank(query.getPayStartTime())) {
			queryMap.put("payStartTime", DateUtil.beginOfDay(DateUtil.parseDate(query.getPayStartTime())));
		}

		SQLOperation operation = this.getSQLAndParams("OrderItem.getShopOrderBillDistributionPage", queryMap);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ShopOrderBillDistributionExportDTO.class));
	}

	@Override
	public List<OrderItemDTO> queryByUserId(Long userId) {
		LambdaEntityCriterion<OrderItemDTO> entityCriterion = new LambdaEntityCriterion<>(OrderItemDTO.class);
		entityCriterion.eq(OrderItemDTO::getUserId, userId);
		return queryDTOByProperties(entityCriterion);
	}

	@Override
	public List<OrderItemDTO> queryByOrderNumber(String orderNumber) {
		LambdaEntityCriterion<OrderItemDTO> entityCriterion = new LambdaEntityCriterion<>(OrderItemDTO.class);
		entityCriterion.eq(OrderItemDTO::getOrderNumber, orderNumber);
		return queryDTOByProperties(entityCriterion);
	}

	@Override
	public int updateCommissionSettleStatus(List<String> distributionOrderItemNumbers, Integer value) {
		if (CollUtil.isEmpty(distributionOrderItemNumbers)) {
			return 0;
		}
		StringBuilder sb = new StringBuilder("update ls_order_item set commission_settle_status = ?, update_time = NOW() where order_item_number in(");
		List<Object> list = new ArrayList<>();
		list.add(value);
		for (String id : distributionOrderItemNumbers) {
			list.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return update(sb.toString(), list.toArray());
	}


	@Override
	public List<OrderItem> queryByOrderNumbers(List<String> orderNumbers) {
		return queryByProperties(new EntityCriterion().in("orderNumber", orderNumbers.toArray()));
	}

	@Override
	public List<OrderItemDTO> getExpiredDivideOrder() {
		return query(getSQL("OrderItem.getExpiredDivideOrder"), OrderItemDTO.class, PayTypeEnum.YEEPAY_ALI_PAY.name(), PayTypeEnum.YEEPAY_WX_PAY.name(), OrderStatusEnum.SUCCESS.getValue(), OrderRefundReturnStatusEnum.ITEM_REFUND_PROCESSING.value());
	}

	@Override
	public List<OrderItem> queryByRefundId(Long refundId) {
		EntityCriterion criterion = new EntityCriterion();
		criterion.eq("refundId", refundId);
		return queryByProperties(criterion);
	}

	@Override
	public List<OrderItemDTO> queryDetailByOrderId(Long orderId) {
		return this.queryDetailByOrderId(Collections.singletonList(orderId));
	}

	@Override
	public List<OrderItemDTO> queryDetailByOrderId(List<Long> orderIds) {
		if (CollUtil.isEmpty(orderIds)) {
			return Collections.emptyList();
		}
		QueryMap queryMap = new QueryMap();
		queryMap.in("orderId", orderIds);
		SQLOperation operation = getSQLAndParams("OrderItem.queryDetailByOrderId", queryMap);
		return query(operation.getSql(), OrderItemDTO.class, operation.getParams());
	}

	@Override
	public List<OrderItem> queryByOrderItemNumber(List<String> orderItemNumbers) {
		if (CollUtil.isEmpty(orderItemNumbers)) {
			return Collections.emptyList();
		}
		return queryByProperties(new LambdaEntityCriterion<>(OrderItem.class).in(OrderItem::getOrderItemNumber, orderItemNumbers));
	}


	@Override
	public Integer getPayedQuptaOrderStatisticsByUserId(QuotaOrderDTO quotaOrderDTO, Date beginTime, Date endTime) {
		QueryMap map = new QueryMap();
		map.put("userId", quotaOrderDTO.getUserId());
		map.put("productId", quotaOrderDTO.getProductId());
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		map.put("quotaTime", quotaOrderDTO.getQuotaTime());

		SQLOperation operation = this.getSQLAndParams("OrderItem.quotaOrderSUM", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public List<OrderItemDTO> queryReturnDeadlineIsNull() {
		return query(" select loi.* from ls_order_item loi left join ls_order lo on loi.order_id = lo.id where lo.status = 20 and loi.return_deadline is null;", OrderItemDTO.class);
	}
}
