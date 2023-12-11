/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import com.legendshop.user.bo.UserInvoiceBO;
import com.legendshop.user.dao.UserInvoiceDao;
import com.legendshop.user.dto.OrderItemInvoiceDTO;
import com.legendshop.user.dto.UserInvoiceDetailDTO;
import com.legendshop.user.entity.UserInvoice;
import com.legendshop.user.query.UserInvoiceQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 发票Dao实现
 *
 * @author legendshop
 */
@Repository
public class UserInvoiceDaoImpl extends GenericDaoImpl<UserInvoice, Long> implements UserInvoiceDao {


	@Override
	public int updateDefaultInvoice(Long invoiceId, Long userId) {
		//1.清除之前默认发票的默认状态
		update("update ls_user_invoice set common_invoice_flag = 0 where common_invoice_flag = 1 and user_id = ?", userId);

		// 2.更新新的发票为默认状态
		int result = update("update ls_user_invoice set common_invoice_flag =1 where id = ? and user_id = ?", invoiceId, userId);
		return result;
	}

	@Override
	public UserInvoice getDefaultInvoice(Long userId) {
		return get("SELECT id as id,user_id as userId,company as company,type as type,title_type as titleType,content as content,invoice_hum_number as invoiceHumNumber,register_addr as registerAddr,register_phone as registerPhone,deposit_bank as depositBank,bank_account_num as bankAccountNum FROM ls_user_invoice WHERE user_id=? AND common_invoice_flag=1 ORDER BY create_time", UserInvoice.class, userId);
	}

	@Override
	public int removeDefaultInvoiceStatus(Long userId) {
		return update("update ls_user_invoice set common_invoice_flag = 0 where user_id = ?", userId);
	}

	@Override
	public PageSupport<UserInvoice> queryPage(UserInvoiceQuery query) {

		CriteriaQuery cq = new CriteriaQuery(UserInvoice.class, query.getPageSize(), query.getCurPage());
		cq.eq("userId", query.getUserId());
		cq.eq("type", query.getType());
		cq.eq("titleType", query.getTitleType());
		cq.addDescOrder("commonInvoiceFlag");
		cq.addDescOrder("updateTime");
		cq.addDescOrder("createTime");
		return queryPage(cq);
	}

	@Override
	public UserInvoice getDefaultInvoiceForOrder(Long userId, List<String> invoiceTypeList) {
		EntityCriterion cq = new EntityCriterion();
		cq.eq("userId", userId).eq("commonInvoiceFlag", true).in("type", invoiceTypeList);
		return getByProperties(cq);
	}

	@Override
	public List<UserInvoice> getUserInvoiceForOrder(Long userId, List<String> invoiceTypeList) {
		EntityCriterion cq = new EntityCriterion();
		cq.eq("userId", userId).in("type", invoiceTypeList).addDescOrder("updateTime").addDescOrder("createTime");
		return queryByProperties(cq);
	}

	@Override
	public PageSupport<UserInvoiceBO> queryUserInvoiceOrderById(UserInvoiceQuery query) {
		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(UserInvoiceBO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("userId", query.getUserId());
		map.like("orderNumber", query.getOrderNumber(), MatchMode.ANYWHERE);
		map.put("hasInvoiceFlag", query.getHasInvoiceFlag());
		map.put("type", query.getType());
		map.put("titleType", query.getTitleType());

		sqlQuery.setSqlAndParameter("UserInvoice.queryUserInvoiceOrderById", map);
		return querySimplePage(sqlQuery);
	}

	@Override
	public List<String> queryUserInvoiceOrderPics(Long orderId) {
		return query("\tSELECT  loi.pic FROM ls_order_item loi\n" +
				"                LEFT JOIN ls_order lo ON loi.order_id=lo.id\n" +
				"                WHERE\n" +
				"\t            lo.id=?", String.class, orderId);
	}

	@Override
	public UserInvoiceDetailDTO getDetail(Long id) {
		QueryMap map = new QueryMap();
		map.put("id", id);
		SQLOperation operation = this.getSQLAndParams("UserInvoice.getDetail", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(UserInvoiceDetailDTO.class));
	}

	@Override
	public List<String> getAddress(List<Long> longList) {
		if (CollUtil.isEmpty(longList)) {
			return null;
		}
		StringBuffer sb = new StringBuffer("select  name " +
				"from ls_location  where id in (");
		for (Long id : longList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return query(sb.toString(), String.class, longList.toArray());
	}

	@Override
	public List<OrderItemInvoiceDTO> getOrderItem(Long id) {
		QueryMap map = new QueryMap();
		map.put("id", id);
		SQLOperation operation = this.getSQLAndParams("UserInvoice.getOrderItem", map);
		List<OrderItemInvoiceDTO> orderItem = query(operation.getSql(), OrderItemInvoiceDTO.class, operation.getParams());
		if (CollectionUtil.isNotEmpty(orderItem)) {
			return orderItem;
		}
		ArrayList<OrderItemInvoiceDTO> newOrderItem = new ArrayList<>();
		return newOrderItem;

	}


}
