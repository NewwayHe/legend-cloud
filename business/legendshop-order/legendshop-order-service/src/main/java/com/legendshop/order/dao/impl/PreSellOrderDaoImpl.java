/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import cn.legendshop.jpaplus.util.StringUtils;
import com.legendshop.order.dao.PreSellOrderDao;
import com.legendshop.order.dto.PreSellOrderBillDTO;
import com.legendshop.order.dto.ShopOrderBillPreSellDTO;
import com.legendshop.order.entity.PreSellOrder;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.excel.ShopOrderBillPreSellExportDTO;
import com.legendshop.order.query.ShopOrderBillOrderQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 预售定点杆dao
 *
 * @author legendshop
 */
@Repository
public class PreSellOrderDaoImpl extends GenericDaoImpl<PreSellOrder, Long> implements PreSellOrderDao {

	@Override
	public PreSellOrder getById(Long id) {
		return super.getById(id);
	}

	@Override
	public int delete(PreSellOrder preSellOrder) {
		return super.delete(preSellOrder);
	}

	@Override
	public Long save(PreSellOrder preSellOrder) {
		return super.save(preSellOrder);
	}

	@Override
	public int update(PreSellOrder preSellOrder) {
		return super.update(preSellOrder);
	}


	@Override
	public List<PreSellOrder> queryByOrderIds(List<Long> orderIds) {
		if (CollectionUtils.isEmpty(orderIds)) {
			return new ArrayList<>();
		}
		return super.queryByProperties(new EntityCriterion().in("orderId", orderIds));
	}

	@Override
	public List<PreSellOrder> queryUnpaidBalanceOrderByDate(Date date) {
		String sql = "SELECT po.* FROM ls_order AS o INNER JOIN ls_pre_sell_order AS po ON o.id = po.order_id WHERE o.`status` != ? AND po.pay_deposit_flag = TRUE AND po.pay_final_flag = FALSE AND po.final_m_end < ?";
		return super.query(sql, PreSellOrder.class, OrderStatusEnum.CLOSE.getValue(), date);
	}

	@Override
	public PreSellOrder getByOrderId(Long orderId) {
		return super.getByProperties(new EntityCriterion().eq("orderId", orderId));
	}

	/**
	 * 查询当前期 所有已支付定金未支付尾款但尾款支付时间已过且已关闭的订单
	 */
	@Override
	public List<PreSellOrderBillDTO> getBillUnPayFinalPreSellOrder(Date endDate, Integer orderStatus) {

		String sql = "SELECT s.shop_id,ps.order_id,ps.pre_deposit_price FROM ls_pre_sell_order ps LEFT JOIN ls_order s ON ps.order_id = s.id WHERE 1=1 AND\n" +
				" s.bill_flag = 0 AND ps.pay_pct_type = 1 AND ps.pay_deposit_flag = 1 AND ps.pay_final_flag = 0 AND s.status = ? AND ps.final_m_end < ?";
		return this.query(sql, PreSellOrderBillDTO.class, orderStatus, endDate);
	}

	@Override
	public PageSupport<ShopOrderBillPreSellDTO> getShopOrderBillPreSellPage(ShopOrderBillOrderQuery query) {
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


		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(ShopOrderBillPreSellDTO.class, query);
		sqlQuery.setSqlAndParameter("PreSellOrder.getShopOrderBillPreSellPage", queryMap);
		return querySimplePage(sqlQuery);
	}

	@Override
	public List<ShopOrderBillPreSellExportDTO> exportShopOrderBillPreSellPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("billSn", shopOrderBillOrderQuery.getSn());
		queryMap.put("shopId", shopOrderBillOrderQuery.getShopId());
		queryMap.put("orderNumber", shopOrderBillOrderQuery.getOrderNumber());
		queryMap.put("orderEndTime", shopOrderBillOrderQuery.getOrderEndTime());
		queryMap.put("orderStartTime", shopOrderBillOrderQuery.getOrderStartTime());
		queryMap.put("payEndTime", shopOrderBillOrderQuery.getPayEndTime());
		queryMap.put("payStartTime", shopOrderBillOrderQuery.getPayStartTime());

		SQLOperation operation = this.getSQLAndParams("PreSellOrder.getShopOrderBillPreSellPage", queryMap);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ShopOrderBillPreSellExportDTO.class));
	}
}
