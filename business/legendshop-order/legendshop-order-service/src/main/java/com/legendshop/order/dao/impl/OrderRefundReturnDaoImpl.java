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
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import cn.legendshop.jpaplus.util.StringUtils;
import com.legendshop.order.bo.OrderRefundReturnBO;
import com.legendshop.order.dao.OrderRefundReturnDao;
import com.legendshop.order.dto.OrderCancelReasonDTO;
import com.legendshop.order.dto.OrderStatusNumDTO;
import com.legendshop.order.dto.ShopOrderBillRefundDTO;
import com.legendshop.order.entity.OrderRefundReturn;
import com.legendshop.order.enums.OrderRefundReturnStatusEnum;
import com.legendshop.order.enums.OrderRefundReturnTypeEnum;
import com.legendshop.order.enums.OrderRefundTypeEnum;
import com.legendshop.order.enums.RefundCustomStatusEnum;
import com.legendshop.order.excel.ShopOrderBillRefundExportDTO;
import com.legendshop.order.query.OrderCancelReasonQuery;
import com.legendshop.order.query.OrderRefundReturnBillQuery;
import com.legendshop.order.query.OrderRefundReturnQuery;
import com.legendshop.order.query.ShopOrderBillOrderQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cn.legendshop.jpaplus.criterion.MatchMode.ANYWHERE;

/**
 * 退款表Dao
 *
 * @author legendshop
 */
@Repository
public class OrderRefundReturnDaoImpl extends GenericDaoImpl<OrderRefundReturn, Long> implements OrderRefundReturnDao {

	/**
	 * 获得已经退款成功的订单项
	 */
	public List<OrderRefundReturn> getOrderRefundsSuccessBySettleSn(String settleSn) {
		return this.query("SELECT * FROM ls_order_refund_return r WHERE r.Order_settlement_sn = ? AND r.is_handle_success = 1", OrderRefundReturn.class, settleSn);
	}


	@Override
	public PageSupport<OrderRefundReturn> getByBillSnPage(OrderRefundReturnQuery orderRefundReturnQuery) {
		QueryMap map = new QueryMap();
		map.put("shopId", orderRefundReturnQuery.getShopId());
		map.put("applyStatus", OrderRefundReturnStatusEnum.APPLY_FINISH.value());
		map.put("billSn", orderRefundReturnQuery.getBillSn());
		map.like("orderNumber", orderRefundReturnQuery.getOrderNumber(), ANYWHERE);
		SimpleSqlQuery query = new SimpleSqlQuery(OrderRefundReturn.class);
		query.setSqlAndParameter("OrderRefundReturn.getRefundReturnOrder", map);
		return querySimplePage(query);
	}


	@Override
	public PageSupport<OrderRefundReturn> getPage(OrderRefundReturnQuery orderRefundReturnQuery) {
		CriteriaQuery cq = new CriteriaQuery(OrderRefundReturn.class, orderRefundReturnQuery.getPageSize(), orderRefundReturnQuery.getCurPage());
		//设置查询条件
		orderRefundReturnQuery.setApplyType(OrderRefundReturnTypeEnum.REFUND.value());
		setSearchCondition(cq, orderRefundReturnQuery);
		cq.addAscOrder("sellerState");
		cq.addDescOrder("applyTime");
		return queryPage(cq);
	}

	private void setSearchCondition(CriteriaQuery cq, OrderRefundReturnQuery orderRefundReturnQuery) {
		cq.eq("shopId", orderRefundReturnQuery.getShopId());
		cq.eq("applyType", orderRefundReturnQuery.getApplyType());
		switchCustomStatus(cq, orderRefundReturnQuery.getCustomStatus());
		if (ObjectUtil.isNotNull(orderRefundReturnQuery.getStartTime())) {
			cq.ge("applyTime", orderRefundReturnQuery.getStartTime());
		}
		if (ObjectUtil.isNotNull(orderRefundReturnQuery.getEndTime())) {
			cq.le("applyTime", orderRefundReturnQuery.getEndTime());
		}

		if (StrUtil.isNotBlank(orderRefundReturnQuery.getNumber())) {
			//订单编号
			if (OrderRefundReturnQuery.ORDER_NO.equals(orderRefundReturnQuery.getNumType())) {
				cq.like("orderNumber", orderRefundReturnQuery.getNumber());
				//退款编号
			} else if (OrderRefundReturnQuery.REFUND_NO.equals(orderRefundReturnQuery.getNumType())) {
				cq.like("refundSn", orderRefundReturnQuery.getNumber());
			}
		}
	}


	private void setSearchConditionByCenter(CriteriaQuery cq, OrderRefundReturnQuery orderRefundReturnQuery) {
		cq.eq("userId", orderRefundReturnQuery.getUserId());
		cq.eq("applyType", orderRefundReturnQuery.getApplyType());
		switchCustomStatus(cq, orderRefundReturnQuery.getCustomStatus());
		if (ObjectUtil.isNotNull(orderRefundReturnQuery.getStartTime())) {
			cq.ge("applyTime", orderRefundReturnQuery.getStartTime());
		}
		if (ObjectUtil.isNotNull(orderRefundReturnQuery.getEndTime())) {
			cq.le("applyTime", DateUtil.endOfDay(orderRefundReturnQuery.getEndTime()));
		}
		if (StrUtil.isNotBlank(orderRefundReturnQuery.getNumber())) {
			//订单编号
			if (OrderRefundReturnQuery.ORDER_NO.equals(orderRefundReturnQuery.getNumType())) {
				cq.like("orderNumber", orderRefundReturnQuery.getNumber());
				//退款编号
			} else if (OrderRefundReturnQuery.REFUND_NO.equals(orderRefundReturnQuery.getNumType())) {
				cq.like("refundSn", orderRefundReturnQuery.getNumber());
			}
		}
	}


	@Override
	public OrderRefundReturn getReturnByRefundSnByUserId(String refundSn, Long userId) {
		return this.getByProperties(new EntityCriterion().eq("refundSn", refundSn).eq("userId", userId));
	}


	/**
	 * 设置CustomStatus条件参数
	 *
	 * @param cq
	 * @param customStatus
	 * @return
	 */
	private CriteriaQuery switchCustomStatus(CriteriaQuery cq, Integer customStatus) {
		if (ObjectUtil.isNull(customStatus)) {
			return cq;
		}
		switch (customStatus) {
			//待审核
			case 1:
				cq.eq("sellerState", OrderRefundReturnStatusEnum.SELLER_WAIT_AUDIT.value());
				cq.eq("applyState", OrderRefundReturnStatusEnum.APPLY_WAIT_SELLER.value());
				break;
			//已撤销
			case 2:
				cq.eq("sellerState", OrderRefundReturnStatusEnum.SELLER_WAIT_AUDIT.value());
				cq.eq("applyState", OrderRefundReturnStatusEnum.UNDO_APPLY.value());
				break;
			//待买家退货
			case 3:
				cq.eq("sellerState", OrderRefundReturnStatusEnum.SELLER_AGREE.value());
				cq.eq("returnType", OrderRefundReturnTypeEnum.NEED_GOODS.value());
				cq.eq("goodsState", OrderRefundReturnStatusEnum.LOGISTICS_WAIT_DELIVER.value());
				break;
			//待收货
			case 4:
				cq.eq("sellerState", OrderRefundReturnStatusEnum.SELLER_AGREE.value());
				cq.eq("returnType", OrderRefundReturnTypeEnum.NEED_GOODS.value());
				cq.eq("goodsState", OrderRefundReturnStatusEnum.LOGISTICS_WAIT_RECEIVE.value());
				break;
			//未收到
			case 5:
				cq.eq("sellerState", OrderRefundReturnStatusEnum.SELLER_AGREE.value());
				cq.eq("returnType", OrderRefundReturnTypeEnum.NEED_GOODS.value());
				cq.eq("goodsState", OrderRefundReturnStatusEnum.LOGISTICS_UNRECEIVED.value());
				break;
			//已收到
			case 6:
				cq.eq("sellerState", OrderRefundReturnStatusEnum.SELLER_AGREE.value());
				cq.eq("returnType", OrderRefundReturnTypeEnum.NEED_GOODS.value());
				cq.eq("goodsState", OrderRefundReturnStatusEnum.LOGISTICS_RECEIVED.value());
				break;
			//弃货
			case 7:
				cq.eq("sellerState", OrderRefundReturnStatusEnum.SELLER_AGREE.value());
				cq.eq("returnType", OrderRefundReturnTypeEnum.NO_NEED_GOODS.value());
				break;
			//已同意
			case 8:
				cq.eq("sellerState", OrderRefundReturnStatusEnum.SELLER_AGREE.value());
				break;
			//不同意
			case 9:
				cq.eq("sellerState", OrderRefundReturnStatusEnum.SELLER_DISAGREE.value());
				break;
			default:
		}
		return cq;
	}


	@Override
	public List<OrderRefundReturn> getListByStatusAndBillFlag(OrderRefundReturnBillQuery query) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("status", query.getOrderStatus());
		queryMap.put("handleSuccess", query.getHandleSuccessStatus());
		queryMap.put("billFlag", query.getBillFlag());
		queryMap.put("applyStatus", query.getApplyStatus());
		queryMap.put("endDate", query.getEndDate());
		queryMap.in("ids", query.getOrderIds());
		SQLOperation operation = this.getSQLAndParams("OrderRefundReturn.getListByStatusAndBillFlag", queryMap);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(OrderRefundReturn.class));
	}

	@Override
	public void updateBillStatusAndSn(List<Long> ids, String billSn) {
		List<Object[]> list = new ArrayList<>();
		ids.forEach(id -> {
			list.add(new Object[]{1, billSn, id});
		});
		String sql = "update ls_order_refund_return set bill_flag=?,bill_sn=? where id =?";
		batchUpdate(sql, list);
	}

	@Override
	public OrderRefundReturn getByLogisticsNumber(String logisticsNumber) {
		return getByProperties(new EntityCriterion().eq("logisticsNumber", logisticsNumber));
	}

	@Override
	public PageSupport<OrderRefundReturnBO> page(OrderRefundReturnQuery query) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(OrderRefundReturnBO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("userId", query.getUserId());
		if (null != query.getOrderRefundStatus()) {
			if (1 == query.getOrderRefundStatus()) {
				map.put("applyStatus", " and r.apply_status <> -1 AND r.apply_status <> 3 AND r.apply_status <> -3 AND r.handle_success_status = 0 AND r.apply_status <> -2");
			} else if (2 == query.getOrderRefundStatus()) {
				map.put("applyStatus", " and (r.apply_status = -1 OR r.apply_status = 3 OR r.apply_status = -3 OR r.apply_status = -2)");
			}
		}

		map.put("shopId", query.getShopId());
		map.put("reason", query.getReason());
		map.put("applyType", query.getApplyType());
		map.like("userName", query.getUserName(), ANYWHERE);
		// 用户昵称
		map.like("nickName", query.getNickName(), MatchMode.ANYWHERE);
		//收货人电话
		map.like("receiverMobile", query.getReceiverMobile(), MatchMode.ANYWHERE);
		//收货人
		map.like("receiverName", query.getReceiverName(), MatchMode.ANYWHERE);
		map.like("shopName", query.getShopName(), ANYWHERE);
		map.like("productName", query.getProductName(), ANYWHERE);
		map.like("orderNumber", query.getOrderNumber(), ANYWHERE);
		map.like("refundSn", query.getRefundSn(), ANYWHERE);
		if (null != query.getStartTime() && null != query.getEndTime()) {
			map.put("startTime", DateUtil.beginOfDay(query.getStartTime()));
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		if (RefundCustomStatusEnum.AUDIT_WAIT_SELLER.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.apply_status = 1 and r.seller_status = 0");
		}
		if (RefundCustomStatusEnum.DISAGREE.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.apply_status = -3");
		}
		if (RefundCustomStatusEnum.APPLY_WAIT_ADMIN.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.apply_status = 2");
		}
		if (RefundCustomStatusEnum.WAIT_SHIP.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.seller_status = 1 and r.goods_status = 1");
		}
		if (RefundCustomStatusEnum.WAIT_RECEIVE.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.seller_status = 1 and (r.goods_status = 3 or r.goods_status = 5)");
		}
		if (RefundCustomStatusEnum.FINISH.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.apply_status = 3");
		}
		if (RefundCustomStatusEnum.UNDO.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and (r.apply_status = -1 OR r.apply_status = -2)");
		}
		if (ObjectUtil.isNotEmpty(query.getIsMember())) {
			map.put("isMember", query.getIsMember() ? " and ld.level_id IS not NULL " : " and ld.level_id IS NULL ");
		}
		simpleSqlQuery.setSqlAndParameter("OrderRefundReturn.page", map);
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public OrderRefundReturnBO getUserRefundDetail(Long refundId, Long userId) {
		String sql = "select DISTINCT r.*, r.pay_settlement_sn as paySerialNumber, o.order_type,o.freight_price, o.status as orderStatus, o.total_integral as totalIntegral,o.delivery_type, o.activity_id as activeId from ls_order_refund_return r INNER JOIN ls_order o ON o.id = r.order_id where r.id = ? and r.user_id = ?";
		return get(sql, OrderRefundReturnBO.class, refundId, userId);
	}

	@Override
	public OrderRefundReturnBO getShopRefundDetail(Long refundId, Long shopId) {
		String sql = "select DISTINCT r.*, r.pay_settlement_sn as paySerialNumber,o.order_type,o.freight_price, o.status as orderStatus, o.total_integral as totalIntegral,o.delivery_type, o.activity_id as activeId from ls_order_refund_return r INNER JOIN ls_order o ON o.id = r.order_id " +
				" where r.id = ? and r.shop_id = ?";
		return get(sql, OrderRefundReturnBO.class, refundId, shopId);
	}

	@Override
	public OrderRefundReturnBO getAdminRefundDetail(Long refundId) {
		String sql = "select DISTINCT r.*, r.pay_settlement_sn as paySerialNumber,ud.nick_name,user.mobile as userMobile,o.freight_price, o.status as orderStatus,o.order_type, o.total_integral as totalIntegral,o.delivery_type,o.activity_id as activeId from ls_order_refund_return r " +
				"INNER JOIN ls_order o ON o.id = r.order_id " +
				"INNER JOIN ls_user_detail ud ON ud.user_id = r.user_id INNER JOIN ls_ordinary_user user ON user.id = r.user_id where r.id = ?";
		return get(sql, OrderRefundReturnBO.class, refundId);
	}

	@Override
	public PageSupport<ShopOrderBillRefundDTO> getShopOrderBillRefundPage(ShopOrderBillOrderQuery query) {
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
			queryMap.put("applyEndTime", DateUtil.endOfDay(DateUtil.parseDate(query.getPayEndTime())));
		}
		if (StringUtils.isNotBlank(query.getPayStartTime())) {
			queryMap.put("applyStartTime", DateUtil.beginOfDay(DateUtil.parseDate(query.getPayStartTime())));
		}

		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(ShopOrderBillRefundDTO.class, query);
		sqlQuery.setSqlAndParameter("OrderRefundReturn.getShopOrderBillRefundPage", queryMap);
		return querySimplePage(sqlQuery);
	}

	@Override
	public List<ShopOrderBillRefundExportDTO> exportShopOrderBillRefundPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("billSn", shopOrderBillOrderQuery.getSn());
		queryMap.put("shopId", shopOrderBillOrderQuery.getShopId());
		queryMap.put("orderNumber", shopOrderBillOrderQuery.getOrderNumber());
		queryMap.put("orderEndTime", shopOrderBillOrderQuery.getOrderEndTime());
		queryMap.put("orderStartTime", shopOrderBillOrderQuery.getOrderStartTime());
		queryMap.put("applyEndTime", shopOrderBillOrderQuery.getPayEndTime());
		queryMap.put("applyStartTime", shopOrderBillOrderQuery.getPayStartTime());

		SQLOperation operation = this.getSQLAndParams("OrderRefundReturn.getShopOrderBillRefundPage", queryMap);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ShopOrderBillRefundExportDTO.class));
	}

	@Override
	public BigDecimal getRefundAmountByUserId(Long userId) {
		String sql = "SELECT IFNULL(sum(refund_amount),0) as refundAmount from ls_order_refund_return where apply_status =? and user_id =?";
		return get(sql, BigDecimal.class, OrderRefundReturnStatusEnum.APPLY_FINISH.value(), userId);
	}

	@Override
	public BigDecimal getRefundAmountByOrderNumbers(List<String> orderNumbers) {
		StringBuffer sb = new StringBuffer("SELECT IFNULL(sum(refund_amount),0) as refundAmount from ls_order_refund_return where apply_status =? and order_number in(");
		List<Object> args = new ArrayList<>();
		args.add(OrderRefundReturnStatusEnum.APPLY_FINISH.value());
		for (String orderNumber : orderNumbers) {
			sb.append("?,");
			args.add(orderNumber);
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return get(sb.toString(), BigDecimal.class, args.toArray());
	}

	@Override
	public OrderRefundReturn getByRefundSn(String refundSn) {
		if (StrUtil.isEmpty(refundSn)) {
			return null;
		}
		LambdaEntityCriterion<OrderRefundReturn> criterion = new LambdaEntityCriterion<>(OrderRefundReturn.class);
		criterion.eq(OrderRefundReturn::getRefundSn, refundSn);
		return getByProperties(criterion);
	}

	@Override
	public OrderRefundReturn getReturnByRefundSn(String refundSn) {
		return this.getByProperties(new EntityCriterion().eq("refundSn", refundSn));
	}

	@Override
	public List<OrderRefundReturn> queryByOrderId(Long orderId) {
		return queryByProperties(new EntityCriterion().eq("orderId", orderId));
	}

	@Override
	public List<OrderRefundReturn> queryUntreatedOrderRefund() {
		return queryByProperties(new EntityCriterion().eq("sellerStatus", 0).eq("applyStatus", 1));
	}

	@Override
	public Integer updateLogisticsReceived(Long refundId) {
		return update("update ls_order_refund_return set receive_time = ?, apply_status = ?, goods_status = ? where id = ? and goods_status in (?, ?)",
				DateUtil.date(), OrderRefundReturnStatusEnum.APPLY_WAIT_ADMIN.value(), OrderRefundReturnStatusEnum.LOGISTICS_RECEIVED.value(), refundId, OrderRefundReturnStatusEnum.LOGISTICS_RECEIVING.value(), OrderRefundReturnStatusEnum.LOGISTICS_WAIT_RECEIVE.value());
	}

	@Override
	public Integer updateLogisticsWaitReceive(Long refundId) {
		return update("update ls_order_refund_return set logistics_receiving_time = ?, goods_status = ? where id = ? and goods_status = ?",
				DateUtil.date(), OrderRefundReturnStatusEnum.LOGISTICS_WAIT_RECEIVE.value(), refundId, OrderRefundReturnStatusEnum.LOGISTICS_RECEIVING.value());
	}

	@Override
	public List<OrderStatusNumDTO> getOrderStatusNum(Long userId) {
		List<OrderStatusNumDTO> list = new ArrayList<OrderStatusNumDTO>();
		//售后处理中
		String sql0 = "SELECT handle_success_status AS `status` , count( id ) AS num \n" +
				" FROM ls_order_refund_return r \n" +
				" WHERE 1=1 \n" +
				" AND user_id = ? \n" +
				" AND r.apply_status <> -1 \n" +
				" AND r.apply_status <> -2 \n" +
				" AND r.apply_status <> -3 \n" +
				" AND r.apply_status <>  3 \n" +
				" AND r.handle_success_status = 0 ";
		OrderStatusNumDTO orderStatusNumDTO0 = get(sql0, OrderStatusNumDTO.class, userId);
		if (ObjectUtil.isNotEmpty(orderStatusNumDTO0)) {
			list.add(orderStatusNumDTO0);
		}
		//售后处理完成
		String sql1 = "SELECT handle_success_status AS `status` , count( id ) AS num \n" +
				" FROM ls_order_refund_return r \n" +
				" WHERE 1=1 \n" +
				" AND user_id = ? \n" +
				" AND r.handle_success_status <> 0 ";
		OrderStatusNumDTO orderStatusNumDTO1 = get(sql0, OrderStatusNumDTO.class, userId);
		if (ObjectUtil.isNotEmpty(orderStatusNumDTO1)) {
			list.add(orderStatusNumDTO1);
		}
		return list;
	}

	@Override
	public PageSupport<OrderCancelReasonDTO> pageCancelOrder(OrderCancelReasonQuery query) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(OrderCancelReasonDTO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();

		//店铺id
		map.put("shopId", query.getShopId());
		//商家
		map.put("refundSource", query.getRefundSource());
		//搜索商品名称
		map.like("productName", query.getProductName(), ANYWHERE);
		//搜索订单号
		map.like("orderNumber", query.getOrderNumber(), ANYWHERE);
		//搜索取消原因
		map.put("reason", query.getReason());
		//（1）待审核  （2）已拒绝 （3）已完成（-1）已取消
		map.put("applyStatus", query.getApplyStatus());

		//applyType=3 为商家申请取消订单
		map.put("applyType", query.getApplyType());
		if (null != query.getStartTime() && null != query.getEndTime()) {
			map.put("startTime", DateUtil.beginOfDay(query.getStartTime()));
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}

		simpleSqlQuery.setSqlAndParameter("OrderRefundReturn.papeCancelOrder", map);
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public PageSupport<OrderRefundReturnBO> pageOrderRefundUser(OrderRefundReturnQuery query) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(OrderRefundReturnBO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("userId", query.getUserId());
		if (null != query.getOrderRefundStatus()) {
			if (1 == query.getOrderRefundStatus()) {
				map.put("applyStatus", " and r.apply_status <> -1 AND r.apply_status <> 3 AND r.apply_status <> -3 AND r.handle_success_status = 0 AND r.apply_status <> -2");
			} else if (2 == query.getOrderRefundStatus()) {
				map.put("applyStatus", " and (r.apply_status = -1 OR r.apply_status = 3 OR r.apply_status = -3 OR r.apply_status = -2)");
			}
		}

		map.put("shopId", query.getShopId());
		map.put("applyType", query.getApplyType());

		if (OrderRefundTypeEnum.RETURN.getValue().equals(query.getApplyType())) {
			map.in("applyType", Arrays.asList(query.getApplyType()));
		}
		if (OrderRefundTypeEnum.REFUND.getValue().equals(query.getApplyType())) {
			map.in("applyType", Arrays.asList(query.getApplyType(), OrderRefundReturnTypeEnum.REFUND_CANEL.value()));
		}
		map.like("userName", query.getUserName(), ANYWHERE);
		// 用户昵称
		map.like("nickName", query.getNickName(), MatchMode.ANYWHERE);
		//收货人电话
		map.like("receiverMobile", query.getReceiverMobile(), MatchMode.ANYWHERE);
		//收货人
		map.like("receiverName", query.getReceiverName(), MatchMode.ANYWHERE);
		map.like("shopName", query.getShopName(), ANYWHERE);

		map.like("productName", query.getProductName(), ANYWHERE);
		map.like("orderNumber", query.getOrderNumber(), ANYWHERE);
		map.like("refundSn", query.getRefundSn(), ANYWHERE);
		if (null != query.getStartTime() && null != query.getEndTime()) {
			map.put("startTime", DateUtil.beginOfDay(query.getStartTime()));
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		if (RefundCustomStatusEnum.AUDIT_WAIT_SELLER.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.apply_status = 1 and r.seller_status = 0");
		}
		if (RefundCustomStatusEnum.DISAGREE.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.apply_status = -3");
		}
		if (RefundCustomStatusEnum.APPLY_WAIT_ADMIN.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.apply_status = 2");
		}
		if (RefundCustomStatusEnum.WAIT_SHIP.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.seller_status = 1 and r.goods_status = 1");
		}
		if (RefundCustomStatusEnum.WAIT_RECEIVE.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.seller_status = 1 and (r.goods_status = 3 or r.goods_status = 5)");
		}
		if (RefundCustomStatusEnum.FINISH.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and r.apply_status = 3");
		}
		if (RefundCustomStatusEnum.UNDO.getValue().equals(query.getCustomStatus())) {
			map.put("customStatus", " and (r.apply_status = -1 OR r.apply_status = -2)");
		}
		if (ObjectUtil.isNotEmpty(query.getIsMember())) {
			map.put("isMember", query.getIsMember() ? " and ld.level_id IS not NULL " : " and ld.level_id IS NULL ");
		}
		simpleSqlQuery.setSqlAndParameter("OrderRefundReturn.pageOrderRefundUser", map);
		return querySimplePage(simpleSqlQuery);
	}


	@Override
	public List<OrderRefundReturn> getByOrderId(List<Long> orderId) {
		if (CollUtil.isEmpty(orderId)) {
			return Collections.emptyList();
		}
		LambdaEntityCriterion<OrderRefundReturn> criterion = new LambdaEntityCriterion<>(OrderRefundReturn.class);
		criterion.in(OrderRefundReturn::getOrderId, orderId);
		return queryByProperties(criterion);
	}

	@Override
	public List<OrderRefundReturn> getByRefundId(List<Long> refundId) {
		if (CollUtil.isEmpty(refundId)) {
			return Collections.emptyList();
		}
		LambdaEntityCriterion<OrderRefundReturn> criterion = new LambdaEntityCriterion<>(OrderRefundReturn.class);
		criterion.in(OrderRefundReturn::getId, refundId);
		return queryByProperties(criterion);
	}

	@Override
	public List<OrderRefundReturn> queryAfterSalesReason(Long shopId, Long applyType) {
		QueryMap map = new QueryMap();
		map.put("shopId", shopId);
		map.put("applyType", applyType);
		SQLOperation operation = this.getSQLAndParams("OrderRefundReturn.queryAfterSalesReason", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(OrderRefundReturn.class));
	}
}
