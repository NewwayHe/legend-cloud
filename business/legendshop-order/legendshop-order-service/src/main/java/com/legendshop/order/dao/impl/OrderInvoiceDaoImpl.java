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
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.order.dao.OrderInvoiceDao;
import com.legendshop.order.dto.OrderInvoiceDTO;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.entity.OrderInvoice;
import com.legendshop.order.excel.OrderInvoiceExportDTO;
import com.legendshop.order.query.OrderInvoiceQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单发票信息Dao.
 *
 * @author legendshop
 */
@Repository
public class OrderInvoiceDaoImpl extends GenericDaoImpl<OrderInvoice, Long> implements OrderInvoiceDao {

	@Override
	public PageSupport<OrderInvoiceDTO> queryPage(OrderInvoiceQuery orderInvoiceQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(OrderInvoiceDTO.class, orderInvoiceQuery.getPageSize(), orderInvoiceQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("shopId", orderInvoiceQuery.getShopId());
		map.like("orderNumber", orderInvoiceQuery.getOrderNumber(), MatchMode.ANYWHERE);
		map.put("hasInvoiceFlag", orderInvoiceQuery.getHasInvoiceFlag());
		map.put("type", orderInvoiceQuery.getType());
		map.like("company", orderInvoiceQuery.getCompany(), MatchMode.ANYWHERE);
		if (ObjectUtil.isNotEmpty(orderInvoiceQuery.getCreateTime())) {
			map.put("createTime", DateUtil.formatDate(DateUtil.parseDate(orderInvoiceQuery.getCreateTime())));
		}
		map.like("userName", orderInvoiceQuery.getUserName());
		query.setSqlAndParameter("OrderInvoice.queryInvoiceOrderPage", map);
		return querySimplePage(query);
	}

	@Override
	public List<OrderInvoiceExportDTO> orderInvoiceExport(OrderInvoiceQuery query) {

		QueryMap map = new QueryMap();
		map.put("shopId", query.getShopId());
		map.like("orderNumber", query.getOrderNumber(), MatchMode.ANYWHERE);
		map.put("hasInvoiceFlag", query.getHasInvoiceFlag());
		map.put("type", query.getType());
		map.like("company", query.getCompany(), MatchMode.ANYWHERE);
		if (ObjectUtil.isNotEmpty(query.getCreateTime())) {
			map.put("createTime", DateUtil.formatDate(DateUtil.parseDate(query.getCreateTime())));
		}
		map.like("userName", query.getUserName(), MatchMode.ANYWHERE);
		SQLOperation operation = this.getSQLAndParams("OrderInvoice.queryInvoiceOrder", map);
		return this.query(operation.getSql(), OrderInvoiceExportDTO.class, operation.getParams());
	}


	@Override
	public List<OrderItemDTO> queryUserInvoiceOrderPics() {
		return query(" SELECT loi.order_id,loi.pic FROM ls_order_item loi  LEFT JOIN ls_order lo ON loi.order_id=lo.id ", OrderItemDTO.class);
	}
}
