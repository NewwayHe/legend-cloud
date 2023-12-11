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
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.order.bo.OrderBO;
import com.legendshop.order.bo.OrderShopMessageNoticeBO;
import com.legendshop.order.bo.OrderStatusNumBO;
import com.legendshop.order.bo.PaidOrderCountsBO;
import com.legendshop.order.dao.OrderDao;
import com.legendshop.order.dto.*;
import com.legendshop.order.entity.Order;
import com.legendshop.order.enums.OrderDeleteStatusEnum;
import com.legendshop.order.enums.OrderRefundReturnStatusEnum;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.excel.OrderExportDTO;
import com.legendshop.order.excel.ShopOrderBillOrderExportDTO;
import com.legendshop.order.excel.ShopOrderBillOrderIntegralExportDTO;
import com.legendshop.order.query.OrderSearchQuery;
import com.legendshop.order.query.ShopOrderBillOrderQuery;
import com.legendshop.user.dto.UserAddressDTO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 订单Dao
 *
 * @author legendshop
 */
@Repository
public class OrderDaoImpl extends GenericDaoImpl<Order, Long> implements OrderDao {

	@Override
	public int update(Order entity) {
		return super.updateProperties(entity);
	}

	@Override
	public int update(List<Order> entities) {
		int result = 0;
		for (Order entity : entities) {
			result += this.update(entity);
		}
		return result;
	}

	@Override
	public Order getByOrderNumber(String orderNumber) {
		return getByProperties(new EntityCriterion().eq("orderNumber", orderNumber));
	}

	@Override
	public Order getByLogisticsNumber(String logisticsNumber) {
		return get("SELECT lo.* FROM ls_order lo INNER JOIN ls_order_logistics lol ON lo.id = lol.order_id WHERE lol.shipment_number = ?", Order.class, logisticsNumber);
	}

	@Override
	public Order getByOrderNumberByUserId(String orderNumber, Long userId) {
		if (ObjectUtil.isEmpty(orderNumber) && ObjectUtil.isEmpty(userId)) {
			return null;
		}
		return getByProperties(new EntityCriterion().eq("orderNumber", orderNumber).eq("userId", userId));
	}

	@Override
	public Order getByOrderIdAndUserId(Long orderId, Long userId) {
		if (ObjectUtil.isEmpty(orderId) && ObjectUtil.isEmpty(userId)) {
			return null;
		}
		return getByProperties(new EntityCriterion().eq("id", orderId).eq("userId", userId));
	}


	@Override
	public List<Order> getOrderByOrderNumbersAndUserId(List<String> orderNumbers, Long userId, Integer status) {
		EntityCriterion entityCriterion = new EntityCriterion(true).eq("userId", userId);
		entityCriterion.in("orderNumber", orderNumbers);
		entityCriterion.eq("status", status);
		return queryByProperties(entityCriterion);
	}


	@Override
	public Order getByOrderNumberByShopId(String orderNumber, Long shopId) {
		if (ObjectUtil.isEmpty(orderNumber) && ObjectUtil.isEmpty(shopId)) {
			return null;
		}
		return getByProperties(new EntityCriterion().eq("orderNumber", orderNumber).eq("shopId", shopId));
	}


	@Override
	public int updateRefundState(Long OrderId, Integer refundState) {
		String sql = "UPDATE ls_order SET refund_status = ? WHERE id = ?";
		return this.update(sql, refundState, OrderId);
	}

	@Override
	public int shipOrder(Long orderId) {
		Date timeDate = new Date();
		String sql = "UPDATE ls_order SET update_time=?,delivery_time=?,status = ? WHERE id = ? and status=?";
		return this.update(sql, timeDate, timeDate, OrderStatusEnum.CONSIGNMENT.getValue(), orderId, OrderStatusEnum.WAIT_DELIVERY.getValue());
	}


	@Override
	public int deliverOrder(Long orderId) {
		Date timeDate = new Date();
		String sql = "UPDATE ls_order SET update_time=?,logistics_receiving_time=?,status = ? WHERE id = ? and status=?";
		return this.update(sql, timeDate, timeDate, OrderStatusEnum.TAKE_DELIVER.getValue(), orderId, OrderStatusEnum.CONSIGNMENT.getValue());
	}

	@Override
	public int confirmDeliverOrder(Long orderId) {
		Date timeDate = new Date();
		String sql = "UPDATE ls_order SET update_time=?,complete_time=?,status = ? WHERE id = ? and status>=? and status <> ?";
		return this.update(sql, timeDate, timeDate, OrderStatusEnum.SUCCESS.getValue(), orderId, OrderStatusEnum.CONSIGNMENT.getValue(), OrderStatusEnum.SUCCESS.getValue());
	}


	@Override
	public Order getOrderByOrderItemId(Long orderItemId) {
		String sql = "SELECT o.* FROM ls_order o, ls_order_item item WHERE item.id = ? AND o.order_number = item.order_number";
		return this.get(sql, Order.class, orderItemId);
	}

	@Override
	public int remindDeliveryBySN(String OrderNumber, Long userId) {
		String sql = "UPDATE ls_order SET remind_delivery_flag = ?, remind_delivery_time = ? WHERE Order_number = ? and user_id = ? ";
		return this.update(sql, Boolean.TRUE, new Date(), OrderNumber, userId);
	}


	@Override
	public BigDecimal getReturnRedPackOffPrice(List<String> orderNumbers) {
		if (CollUtil.isEmpty(orderNumbers)) {
			return BigDecimal.ZERO;
		}
		StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(platform_coupon_off_price),0) FROM ls_order_item WHERE refund_status = 2 AND order_number in (");

		for (int i = 0; i < orderNumbers.size() - 1; i++) {
			sql.append("?, ");
		}
		sql.append("? )");

		return get(sql.toString(), BigDecimal.class, orderNumbers.toArray());
	}


	@Override
	public PageSupport<OrderBO> queryOrderWithItem(OrderSearchQuery query) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(OrderBO.class, query.getPageSize(), query.getCurPage());
		QueryMap queryMap = new QueryMap();

		queryMap.put("userId", query.getUserId());
		queryMap.put("shopId", query.getShopId());
		//交易状态  0或者空默认查全部
		if (null != query.getStatus() && 0 != query.getStatus()) {
			queryMap.put("status", query.getStatus());
		}
		//订单类型
		queryMap.put("orderType", query.getOrderType());
		//售后状态
		queryMap.put("refundStatus", query.getRefundStatus());
		if (ObjectUtil.isNotEmpty(query.getStartDate())) {
			queryMap.put("startSubTime", query.getStartDate());
		}
		if (ObjectUtil.isNotEmpty(query.getEndDate())) {
			queryMap.put("endSubTime", DateUtil.endOfDay(query.getEndDate()));
		}
		if (ObjectUtil.isNotEmpty(query.getPayStartDate())) {
			queryMap.put("payStartSubTime", query.getPayStartDate());
		}
		if (ObjectUtil.isNotEmpty(query.getPayEndDate())) {
			queryMap.put("payEndSubTime", DateUtil.endOfDay(query.getPayEndDate()));
		}
		// 商品名称
		queryMap.like("productName", query.getProductName(), MatchMode.ANYWHERE);
		// 店铺名称
		queryMap.like("shopName", query.getShopName(), MatchMode.ANYWHERE);
		// 用户昵称
		queryMap.like("nickName", query.getNickName(), MatchMode.ANYWHERE);
		// 用户昵称
		queryMap.like("userMobile", query.getUserMobile(), MatchMode.ANYWHERE);
		//订单编号
		queryMap.like("orderNumber", query.getOrderNumber(), MatchMode.ANYWHERE);
		//收货人电话
		queryMap.like("receiverMobile", query.getReceiverMobile(), MatchMode.ANYWHERE);
		//收货人
		queryMap.like("receiverName", query.getReceiverName(), MatchMode.ANYWHERE);
		//支付状态
		queryMap.put("payedFlag", query.getPayedFlag());
		queryMap.like("shipmentNumber", query.getShipmentNumber(), MatchMode.ANYWHERE);

		queryMap.in("orderNumberList", query.getNumberList());
		//用户只能查询到没被删除的订单
		if (ObjectUtil.isNotNull(query.getUserId())) {
			queryMap.put("deleteStatus", OrderDeleteStatusEnum.NORMAL.value());
		}
		if (ObjectUtil.isNotEmpty(query.getIsMember())) {
			queryMap.put("isMember", query.getIsMember() ? " and ld.level_id IS not NULL " : " and ld.level_id IS NULL ");
		}
		queryMap.put("pointId", query.getPointId());
		simpleSqlQuery.setSqlAndParameter("Order.queryOrderWithItem", queryMap);
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public OrderBO getAdminOrderDetail(Long orderId) {
		return get(getSQL("Order.getAdminOrderDetail"), OrderBO.class, orderId);
	}

	@Override
	public OrderBO getUserOrderDetail(Long orderId, Long userId) {
		return get(getSQL("Order.getUserOrderDetail"), OrderBO.class, orderId, userId);
	}

	@Override
	public OrderBO getShopOrderDetail(Long orderId, Long shopId) {
		return get(getSQL("Order.getShopOrderDetail"), OrderBO.class, orderId, shopId);
	}

	@Override
	public List<Order> getListByStatusAndBillFlag(Integer status, Integer billFlag, Date endDate) {
		List<Order> orders = queryByProperties(new EntityCriterion().eq("status", status).eq("billFlag", billFlag).notEq("refundStatus", "1").le("finalReturnDeadline", endDate).eq("payedFlag", true));
		return orders;
	}

	@Override
	public void updateBillStatusAndSn(List<Long> ids, String billSn) {
		List<Object[]> list = new ArrayList<>();
		ids.forEach(id -> {
			list.add(new Object[]{1, billSn, id});
		});
		String sql = "update ls_order set bill_flag=?,bill_sn=? where id =?";
		batchUpdate(sql, list);
	}

	@Override
	public int cancelUnPayOrder(Long orderId) {
		String sql = "UPDATE ls_order SET status = ?,update_time=? WHERE id = ? and status=?";
		return this.update(sql, OrderStatusEnum.CLOSE.getValue(), new Date(), orderId, OrderStatusEnum.UNPAID.getValue());
	}

	@Override
	public int batchCancelUnPayOrder(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return 0;
		}
		StringBuilder sql = new StringBuilder("UPDATE ls_order SET status = ?,update_time=? WHERE status=? and id IN(");
		List<Object> params = new ArrayList<>(ids.size() + 3);
		params.add(OrderStatusEnum.CLOSE.getValue());
		params.add(new Date());
		params.add(OrderStatusEnum.UNPAID.getValue());
		for (Long id : ids) {
			params.add(id);
			sql.append("?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(" ) ");
		return this.update(sql.toString(), params.toArray());
	}

	@Override
	public int cancelPreSellOrder(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return 0;
		}
		StringBuilder sql = new StringBuilder("UPDATE ls_order SET status = ?,update_time = ? WHERE id IN (");
		for (int i = 0; i < ids.size(); i++) {
			if (i != 0) {
				sql.append(", ");
			}
			sql.append("?");
		}
		sql.append(") AND payed_flag = FALSE");
		List<Object> params = new ArrayList<>(ids.size() + 3);
		params.add(OrderStatusEnum.CLOSE.getValue());
		params.add(new Date());
		params.addAll(ids);
		return this.update(sql.toString(), params.toArray());
	}


	@Override
	public PageSupport<Order> queryOrderList(OrderSearchQuery orderSearchQuery) {
		CriteriaQuery cq = new CriteriaQuery(Order.class, orderSearchQuery.getPageSize(), orderSearchQuery.getCurPage());
		cq.eq("shopId", orderSearchQuery.getShopId());
		cq.eq("billSn", orderSearchQuery.getBillSn());
		cq.like("orderNumber", orderSearchQuery.getOrderNumber(), MatchMode.ANYWHERE);
		cq.eq("deleteStatus", OrderDeleteStatusEnum.NORMAL.value());
		cq.gt("createTime", orderSearchQuery.getStartDate());
		cq.gt("payTime", orderSearchQuery.getPayStartDate());
		cq.addOrder("desc", "createTime");
		return queryPage(cq);
	}

	@Override
	public List<Order> queryByNumber(List<String> numberList) {
		return super.queryByProperties(new EntityCriterion().in("orderNumber", numberList));
	}

	@Override
	public List<Long> queryCloseOrderIds(List<Long> orderIds) {
		List<Object> param = new ArrayList<>();
		param.add(OrderStatusEnum.CLOSE.getValue());
		// 这里的sql最后需要加上一个空格
		StringBuilder sql = new StringBuilder("SELECT id FROM ls_order WHERE status = ? ");
		if (CollectionUtils.isEmpty(orderIds)) {
			return this.query(sql.toString(), Long.class, param.toArray());
		}
		sql.append("AND id IN (");
		for (int i = 0; i < orderIds.size(); i++) {
			if (i != 0) {
				sql.append(", ");
			}
			sql.append("?");
		}
		sql.append(")");
		param.addAll(orderIds);
		return this.query(sql.toString(), Long.class, param.toArray());
	}


	@Override
	public Long getSumProductQuantity(Long userId, Long activityId, String orderType, Integer status) {
		String sql = "SELECT SUM(product_quantity) FROM ls_order\n" +
				"WHERE user_id =? AND activity_id = ? " +
				"AND order_type = ? AND `status` !=?";
		return getLongResult(sql, userId, activityId, orderType, status);
	}

	@Override
	public Long getSumProductQuantity(Long userId, Long activityId, String orderType, Integer status, Date createTime) {
		String sql = "SELECT SUM(product_quantity) FROM ls_order\n" +
				"WHERE user_id =? AND activity_id = ? " +
				"AND order_type = ? AND `status` !=? and create_time >= ? and  create_time <= ?";
		DateTime begin = DateUtil.beginOfDay(createTime);
		DateTime end = DateUtil.endOfDay(createTime);
		return getLongResult(sql, userId, activityId, orderType, status, begin, end);
	}

	@Override
	public Long getSumSkuQuantity(Long userId, Long activityId, Long skuId, String orderType, Integer status) {
		String sql = "SELECT SUM(t.basket_count) FROM ls_order o INNER JOIN ls_order_item t on o.id = t.order_id\n" +
				"WHERE o.user_id =? AND o.activity_id = ? AND t.sku_id = ?\n" +
				"AND o.order_type = ? AND o.`status` !=?";

		return getLongResult(sql, userId, activityId, skuId, orderType, status);
	}

	@Override
	public Long getSumSkuQuantity(Long userId, Long activityId, Long skuId, String orderType, Integer status, Date createTime) {
		String sql = "SELECT SUM(t.basket_count) FROM ls_order o INNER JOIN ls_order_item t on o.id = t.order_id\n" +
				"WHERE o.user_id =? AND o.activity_id = ? AND t.sku_id = ?\n" +
				"AND o.order_type = ? AND o.`status` !=? and t.create_time >= ? and  t.create_time <= ?";
		DateTime begin = DateUtil.beginOfDay(createTime);
		DateTime end = DateUtil.endOfDay(createTime);
		return getLongResult(sql, userId, activityId, skuId, orderType, status, begin, end);
	}

	@Override
	public List<Order> getListOfActivity(Long activityId, String orderType) {
		return queryByProperties(new EntityCriterion().eq("activityId", activityId).eq("orderType", orderType));
	}

	@Override
	public List<Order> getPayedAndNoRefundList(Long activityId, String orderType) {
		return queryByProperties(new EntityCriterion().eq("activityId", activityId)
				.eq("orderType", orderType).eq("payedFlag", 1)
				.eq("refundStatus", OrderRefundReturnStatusEnum.ORDER_NO_REFUND.value()));
	}

	@Override
	public List<UserOrderCountDTO> queryUserOrderCenter(Long userId) {
		return super.query("SELECT COUNT(*) AS count , o.status AS status FROM ls_order AS o WHERE o.user_id = ? GROUP BY o.`status` ORDER BY o.`status` ASC ", UserOrderCountDTO.class, userId);
	}

	@Override
	public List<OrderItemDTO> queryActivityOrder(OrderSearchQuery searchQuery) {
		return query("select i.* from ls_order o ,ls_order_item i where i.order_id=o.id and o.order_type=? and o.activity_id=? and o.user_id=? and o.status>=?", OrderItemDTO.class, searchQuery.getOrderType(), searchQuery.getActivityId(), searchQuery.getUserId(), OrderStatusEnum.UNPAID.getValue());
	}

	@Override
	public List<OrderExportDTO> orderExport(OrderSearchQuery query) {

		StringBuilder sb = new StringBuilder();
		List<Object> obj = new ArrayList<Object>();
		String sql = getSQL("Order.getExportOrder");
		sb.append(sql);

		if (ObjectUtil.isNotNull(query.getUserId())) {
			sb.append(" AND o.user_id = ?");
			obj.add(query.getUserId());
		}
		if (ObjectUtil.isNotNull(query.getShopId())) {
			sb.append(" AND o.shop_id = ?");
			obj.add(query.getShopId());
		}
		// 交易状态  0或者空默认查全部
		if (null != query.getStatus() && 0 != query.getStatus()) {
			sb.append(" AND o.status = ?");
			obj.add(query.getStatus());
		}
		// 订单类型
		if (StrUtil.isNotBlank(query.getOrderType())) {
			sb.append(" AND o.order_type = ?");
			obj.add(query.getOrderType());
		}
		// 售后状态
		if (ObjectUtil.isNotEmpty(query.getRefundStatus())) {
			sb.append(" AND o.refund_status = ?");
			obj.add(query.getRefundStatus());
		}
		if (ObjectUtil.isNotEmpty(query.getRefundStatus())) {
			sb.append(" AND o.refund_status = ?");
			obj.add(query.getRefundStatus());
		}
		// 开始时间结束时间
		if (ObjectUtil.isNotEmpty(query.getStartDate())) {
			sb.append(" AND o.create_time >= ?");
			obj.add(query.getStartDate());
		}
		if (ObjectUtil.isNotEmpty(query.getEndDate())) {
			sb.append(" AND o.create_time <= ?");
			obj.add(DateUtil.endOfDay(query.getEndDate()));
		}
		// 商品名称
		if (StrUtil.isNotBlank(query.getProductName())) {
			sb.append(" AND o.product_name LIKE ?");
			obj.add("%" + query.getProductName().trim() + "%");
		}
		// 店铺名称
		if (StrUtil.isNotBlank(query.getShopName())) {
			sb.append(" AND o.shop_name LIKE ?");
			obj.add("%" + query.getShopName().trim() + "%");
		}
		// 用户昵称
		if (StrUtil.isNotBlank(query.getNickName())) {
			sb.append(" AND ud.nick_name LIKE ?");
			obj.add("%" + query.getNickName().trim() + "%");
		}
		// 用户手机号码
		if (StrUtil.isNotBlank(query.getUserMobile())) {
			sb.append(" AND ou.mobile = ?");
			obj.add(query.getUserMobile().trim());
		}
		//订单编号
		if (StrUtil.isNotBlank(query.getOrderNumber())) {
			sb.append(" AND o.order_number LIKE ?");
			obj.add("%" + query.getOrderNumber().trim() + "%");
		}
		//收货人电话
		if (StrUtil.isNotBlank(query.getReceiverMobile())) {
			sb.append(" AND addr.mobile = ?");
			obj.add(query.getReceiverMobile().trim());
		}
		//收货人
		if (StrUtil.isNotBlank(query.getReceiverName())) {
			sb.append(" AND addr.receiver LIKE ?");
			obj.add("%" + query.getReceiverName().trim() + "%");
		}
		//支付状态
		if (ObjectUtil.isNotNull(query.getPayedFlag())) {
			sb.append(" AND o.payed_flag = ?");
			obj.add(query.getPayedFlag());
		}
		//用户只能查询到没被删除的订单
		if (ObjectUtil.isNotNull(query.getUserId())) {
			sb.append(" AND o.delete_status = ?");
			obj.add(OrderDeleteStatusEnum.NORMAL.value());
		}

		sb.append(" ORDER BY o.create_time DESC,o.status ASC ");
		return this.query(sb.toString(), OrderExportDTO.class, obj.toArray());
	}

	@Override
	public PageSupport<ShopOrderBillOrderDTO> getShopBillOrderPage(ShopOrderBillOrderQuery query) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("orderBillSn", query.getSn());
		queryMap.put("shopId", query.getShopId());
		queryMap.put("orderType", query.getOType());
		if (ObjectUtil.isNotEmpty(query.getOrderEndTime())) {
			queryMap.put("createEndTime", DateUtil.endOfDay(DateUtil.parseDate(query.getOrderEndTime())));
		}
		if (ObjectUtil.isNotEmpty(query.getOrderStartTime())) {
			queryMap.put("createStartTime", DateUtil.beginOfDay(DateUtil.parseDate(query.getOrderStartTime())));
		}
		if (ObjectUtil.isNotEmpty(query.getPayEndTime())) {
			queryMap.put("payEndTime", DateUtil.endOfDay(DateUtil.parseDate(query.getPayEndTime())));
		}
		if (ObjectUtil.isNotEmpty(query.getPayStartTime())) {
			queryMap.put("payStartTime", DateUtil.beginOfDay(DateUtil.parseDate(query.getPayStartTime())));
		}
		queryMap.like("orderNumber", query.getOrderNumber(), MatchMode.ANYWHERE);
		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(ShopOrderBillOrderDTO.class, query);
		sqlQuery.setSqlAndParameter("Order.getShopBillOrderPage", queryMap);
		return querySimplePage(sqlQuery);
	}

	@Override
	public List<ShopOrderBillOrderExportDTO> exportShopOrderBillOrderPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		SQLOperation operation = getSqlOperation(shopOrderBillOrderQuery);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ShopOrderBillOrderExportDTO.class));
	}

	@Override
	public OrderBusinessSumDTO getOrderSumByShopId(Long shopId) {
		return get("select sum(if(refund_status <> ?,1,0)) As salesOrderCount, sum(actual_total_price) as salesAmount from ls_order where shop_id = ? and payed_flag=1", OrderBusinessSumDTO.class, OrderRefundReturnStatusEnum.ORDER_REFUND_FINISH.value(), shopId);
	}

	@Override
	public BigDecimal getRefundAmountByShopId(Long shopId) {
		return get("select sum(if(handle_success_status=1,refund_amount,0)) as refundAmount from ls_order_refund_return where shop_id = ?", BigDecimal.class, shopId);
	}

	@Override
	public int invoicing(Long shopId, String orderNumber) {
		String sql = "UPDATE ls_order SET has_invoice_flag = 1 WHERE shop_id = ? AND order_number = ?";
		return update(sql, shopId, orderNumber);
	}

	@Override
	public Boolean batchInvoicing(Long shopId, List<String> ids) {

		List<String> args = new ArrayList<>();
		StringBuilder sb = new StringBuilder("UPDATE ls_order SET has_invoice_flag = 1 WHERE shop_id = ? AND id in (");
		args.add(String.valueOf(shopId));
		for (String id : ids) {
			args.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return update(sb.toString(), args.toArray()) > 0;
	}

	@Override
	public List<Order> queryByNumberAndShopIdAndStatus(List<String> orderNumbers, Long shopId, Integer status) {
		EntityCriterion criterion = new EntityCriterion();
		criterion.in("orderNumber", orderNumbers.toArray());
		criterion.eq("shopId", shopId);
		criterion.eq("status", status);
		return queryByProperties(criterion);
	}

	@Override
	public Long getPlatformOrderCountExceptRefundSuccess(Long userId) {
		LambdaEntityCriterion<Order> criterion = new LambdaEntityCriterion<>(Order.class);
		criterion.eq(Order::getUserId, userId)
				.notEq(Order::getRefundStatus, OrderRefundReturnStatusEnum.ORDER_REFUND_FINISH.value());
		return getCount(criterion);
	}

	@Override
	public List<ShopOrderCountDTO> getShopOrderCountExceptRefundSuccess(Long userId, List<Long> shopIds) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("userId", userId);
		queryMap.in("shopId", shopIds);
		SQLOperation op = this.getSQLAndParams("Order.getShopOrderCountExceptRefundSuccess", queryMap);
		return query(op.getSql(), ShopOrderCountDTO.class, op.getParams());
	}

	@Override
	public void updateStatusByOrderIds(List<Long> orderIds, Integer status, Integer originalStatus) {
		if (CollUtil.isEmpty(orderIds)) {
			return;
		}
		String sql = "update ls_order set status =? where id =? and status =?";
		List<Object[]> args = new ArrayList<>();
		orderIds.forEach(id -> {
			args.add(new Object[]{status, id, originalStatus});
		});
		batchUpdate(sql, args);
	}

	@Override
	public Integer updateTakeDeliverStatus(Long orderId, Date logisticsReceivingTime) {
		if (null == orderId || null == logisticsReceivingTime) {
			return 0;
		}
		String sql = "update ls_order set status =?, logistics_receiving_time=? where id =? and status =?";
		return update(sql, OrderStatusEnum.TAKE_DELIVER.getValue(), logisticsReceivingTime, orderId, OrderStatusEnum.CONSIGNMENT.getValue());
	}


	@Override
	public UserOrderStatisticsDTO getPayedOrderStatisticsByUserId(Long userId) {
		String sql = "select count(*) as count,IFNULL(sum(IF(refund_status=?,1,0)),0) as refundCount, IFNULL(sum(actual_total_price),0) as totalAmount from ls_order where 1=1 and payed_flag = 1 and user_id = ? ";
		return get(sql, UserOrderStatisticsDTO.class, OrderRefundReturnStatusEnum.ORDER_REFUND_FINISH.value(), userId);
	}

	@Override
	public UserOrderStatisticsDTO getReceivedOrderStatisticsByUserId(Long userId) {
		String sql = "select count(*) as count,IFNULL(sum(IF(refund_status=?,1,0)),0) as refundCount, IFNULL(sum(actual_total_price),0) as totalAmount from ls_order where 1=1 and status = ? and user_id = ? ";
		return get(sql, UserOrderStatisticsDTO.class, OrderRefundReturnStatusEnum.ORDER_REFUND_FINISH.value(), OrderStatusEnum.SUCCESS.getValue(), userId);
	}

	@Override
	public List<Order> queryByUserIdAndStatus(Long userId, Integer status) {
		return queryByProperties(new EntityCriterion().eq("userId", userId).eq("status", status));
	}


	@Override
	public int updateFinalReturnDeadline(Long orderId, Date finalReturnDeadline) {
		String sql = "UPDATE ls_order SET final_return_deadline = ? WHERE id = ?";
		return update(sql, finalReturnDeadline, orderId);
	}

	@Override
	public DistributorsOrderRecordDTO getDistributorsOrderRecordDTO(DistributorsOrderRecordDTO dto) {
		QueryMap queryMap = new QueryMap();
		queryMap.in("userId", dto.getIdList());
		Date completeStartTime = dto.getCompleteStartTime();
		if (ObjectUtil.isNotEmpty(completeStartTime)) {
			queryMap.put("completeStartTime", completeStartTime);
		} else {
			queryMap.put("completeStartTime", DateUtil.date());
		}

		queryMap.put("completeEndTime", dto.getCompleteEndTime());
		SQLOperation operation = this.getSQLAndParams("Order.getAmountAndNumByUser", queryMap);
		DistributorsOrderRecordDTO distributorsOrderRecordDTO =
				this.get(operation.getSql(), DistributorsOrderRecordDTO.class, operation.getParams());

		return distributorsOrderRecordDTO;
	}

	@Override
	public List<ShopOrderBillOrderIntegralExportDTO> exportShopOrderBillIntegralOrderPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		SQLOperation operation = getSqlOperation(shopOrderBillOrderQuery);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ShopOrderBillOrderIntegralExportDTO.class));
	}

	@Override
	public DistributionOrderRecordDTO getDistributionOrderById(Long userId) {

//		return get("SELECT a.order_num all_order_num,sum( basket_count )  all_product_num,SUM( product_total_amount ) all_product_amount FROM ls_order_item ,(SELECT count(id) order_num FROM ls_order WHERE user_id = ?) a WHERE dist_flag = 1 AND user_id = ?",
//				DistributionOrderRecordDTO.class,userId,userId);
		return get("SELECT\n" +
				"\tcount( DISTINCT order_id ) all_order_num,\n" +
				"\tsum( basket_count ) all_product_num,\n" +
				"\tSUM( actual_amount ) all_product_amount \n" +
				"FROM\n" +
				"\tls_order_item \n" +
				"WHERE\n" +
				"\treturn_deadline <= ? \n" +
				"\tAND dist_flag = 1 \n" +
				"\tAND user_id = ? \n" +
				"\tAND refund_status =?", DistributionOrderRecordDTO.class, DateUtil.date(), userId, 0);
	}

	private SQLOperation getSqlOperation(ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("orderBillSn", shopOrderBillOrderQuery.getSn());
		queryMap.put("shopId", shopOrderBillOrderQuery.getShopId());
		queryMap.put("orderType", shopOrderBillOrderQuery.getOType());
		if (ObjectUtil.isNotEmpty(shopOrderBillOrderQuery.getOrderEndTime())) {
			queryMap.put("createEndTime", DateUtil.beginOfDay(DateUtil.parseDate(shopOrderBillOrderQuery.getOrderEndTime())));
		}
		if (ObjectUtil.isNotEmpty(shopOrderBillOrderQuery.getOrderStartTime())) {
			queryMap.put("createStartTime", DateUtil.beginOfDay(DateUtil.parseDate(shopOrderBillOrderQuery.getOrderStartTime())));
		}
		if (ObjectUtil.isNotEmpty(shopOrderBillOrderQuery.getPayEndTime())) {
			queryMap.put("payEndTime", DateUtil.beginOfDay(DateUtil.parseDate(shopOrderBillOrderQuery.getPayEndTime())));
		}
		if (ObjectUtil.isNotEmpty(shopOrderBillOrderQuery.getPayStartTime())) {
			queryMap.put("payStartTime", DateUtil.beginOfDay(DateUtil.parseDate(shopOrderBillOrderQuery.getPayStartTime())));
		}
		queryMap.put("orderNumber", shopOrderBillOrderQuery.getOrderNumber());
		return this.getSQLAndParams("Order.getShopBillOrderPage", queryMap);
	}

	@Override
	public Integer getShopOrderCount(Long shopId, Integer status) {
		return get(getSQL("Order.getShopOrderCount"), Integer.class, shopId, status);
	}

	@Override
	public Integer getShopOrderCommCount(Long shopId, Integer status, Integer commFlag) {
		return get(getSQL("Order.getShopOrderCommCount"), Integer.class, shopId, status, commFlag);
	}

	@Override
	public Integer getShopOrderRefundCount(Long shopId, Integer refundStatus) {
		return get(getSQL("Order.getShopOrderRefundCount"), Integer.class, OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value(), shopId, refundStatus);
	}

	@Override
	public PaidOrderCountsBO getShopPaidOrderCount(Long shopId, Date startDate, Date endDate) {
		QueryMap map = new QueryMap();
		map.put("shopId", shopId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("Order.getShopPaidOrderCount", map);

		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(PaidOrderCountsBO.class));
	}

	@Override
	public Integer getShopAccusationProduct(Long shopId) {
		return get(getSQL("Order.getShopAccusationProduct"), Integer.class, shopId);
	}

	@Override
	public Integer getShopOrderInvoiceCount(Long shopId) {
		return get(getSQL("Order.getShopOrderInvoiceCount"), Integer.class, shopId);
	}

	@Override
	public List<PaidOrderCountsBO> getShopSales(Long shopId, Date startDate, Date endDate) {

		QueryMap map = new QueryMap();
		map.put("shopId", shopId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		SQLOperation operation = this.getSQLAndParams("Order.getShopSales", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(PaidOrderCountsBO.class));
	}


	@Override
	public List<OrderShopMessageNoticeBO> queryAllShopOrderNotice() {
		return query(getSQL("Order.queryAllShopOrderNotice"), OrderShopMessageNoticeBO.class);

	}


	@Override
	public UserAddressDTO getAddressByOrderId(Long orderId) {
		return get(getSQL("Order.getAddressByOrderId"), UserAddressDTO.class, orderId);
	}

	@Override
	public List<OrderDTO> queryReturnDeadlineIsNull() {
		return query(" SELECT * FROM ls_order WHERE status = 20 AND final_return_deadline IS NULL;", OrderDTO.class);
	}

	@Override
	public List<OrderStatusNumDTO> getOrderStatusNum(Long userId) {
		String sql = "SELECT status , count(id) AS num FROM ls_order WHERE user_id =?  GROUP BY status ";
		return query(sql, OrderStatusNumDTO.class, userId);
	}

	@Override
	public OrderStatusNumDTO getUnCommCountNum(Long userId) {
		return get(" SELECT " +
				" oi.comm_flag AS `status` , count( oi.id ) AS num " +
				" FROM ls_order_item oi " +
				" LEFT JOIN ls_order o ON o.order_number = oi.order_number " +
				" WHERE 1 = 1 " +
				" AND oi.comm_flag = 0 " +
				" AND o.status =20  AND date_add( o.complete_time, INTERVAL o.comment_valid_day DAY )> ?" +
				" AND oi.user_id = ? ", OrderStatusNumDTO.class, DateUtil.date(), userId);
	}

	@Override
	public OrderStatusNumBO getUserOrderInvoiceCount(Long userId) {
		return get(getSQL("Order.getUserOrderInvoiceCount"), OrderStatusNumBO.class, userId, userId);
	}

	@Override
	public Order getByOrderIdAndShopId(Long orderId, Long shopId) {
		if (ObjectUtil.isEmpty(orderId) && ObjectUtil.isEmpty(shopId)) {
			return null;
		}
		return getByProperties(new EntityCriterion().eq("id", orderId).eq("shopId", shopId));
	}

	@Override
	public List<Order> getByOrderIdList(List<Long> orderId) {
		if (CollUtil.isEmpty(orderId)) {
			return Collections.emptyList();
		}
		LambdaEntityCriterion<Order> criterion = new LambdaEntityCriterion<>(Order.class);
		criterion.in(Order::getId, orderId);
		return queryByProperties(criterion);
	}

	@Override
	public List<Order> queryByNumberAndShopId(Long shopId, List<String> numberList) {
		if (CollUtil.isEmpty(numberList)) {
			return Collections.emptyList();
		}
		LambdaEntityCriterion<Order> criterion = new LambdaEntityCriterion<>(Order.class);
		criterion.in(Order::getOrderNumber, numberList);
		criterion.eq(Order::getShopId, shopId);
		return queryByProperties(criterion);
	}

	@Override
	public List<Order> queryByStatus(Long shopId, OrderStatusEnum status) {
		return super.queryByProperties(new EntityCriterion().eq("shopId", shopId).eq("status", status.getValue()));
	}

	@Override
	public List<WaitDeliveryOrderDTO> queryByStatusAddress(Long shopId) {
		return query(getSQL("Order.queryByStatusAddress"), WaitDeliveryOrderDTO.class, shopId);
	}

	@Override
	public List<OrderDTO> getCustomOrder(Long userId, Long shopId) {
		String sql = "select lo.id, lo.order_number, lo.status, lo.product_quantity, lo.create_time, lo.actual_total_price from ls_order lo where lo.user_id = ? and lo.shop_id = ? ORDER BY lo.create_time DESC LIMIT 0, 20";
		return query(sql, OrderDTO.class, userId, shopId);
	}

	@Override
	public List<OrderDTO> unReceiptOrder() {
		return query(getSQL("Order.unReceiptOrder"), OrderDTO.class);
	}
}
