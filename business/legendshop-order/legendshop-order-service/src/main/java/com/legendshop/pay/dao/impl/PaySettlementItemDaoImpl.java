/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.pay.dao.PaySettlementItemDao;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.entity.PaySettlementItem;
import com.legendshop.pay.query.PaySettlementQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class PaySettlementItemDaoImpl extends GenericDaoImpl<PaySettlementItem, Long> implements PaySettlementItemDao {

	@Override
	public List<PaySettlementItem> queryByPaySettlementSn(String paySettlementSn) {
		return queryByProperties(new EntityCriterion().eq("paySettlementSn", paySettlementSn));
	}

	@Override
	public List<PaySettlementItem> queryByPaySettlementSnList(List<String> paySettlementSnList) {
		if (CollectionUtils.isEmpty(paySettlementSnList)) {
			return new ArrayList<>();
		}
		return queryByProperties(new EntityCriterion().in("paySettlementSn", paySettlementSnList.toArray()));
	}

	@Override
	public List<PaySettlementItem> queryByPaySettlementSnListAndState(List<String> paySettlementSnList, Integer state) {
		if (CollectionUtils.isEmpty(paySettlementSnList)) {
			return new ArrayList<>();
		}
		return queryByProperties(new EntityCriterion().in("paySettlementSn", paySettlementSnList.toArray())
				.eq("state", state));
	}


	@Override
	public List<PaySettlementItemDTO> queryPaidByOrderNumber(String orderNumber) {
		String sql = "SELECT psi.* FROM ls_pay_settlement_item AS psi INNER JOIN ls_pay_settlement_order AS pso ON psi.pay_settlement_sn = pso.pay_settlement_sn WHERE pso.order_number = ? and psi.state = 1";
		return query(sql, PaySettlementItemDTO.class, orderNumber);
	}

	@Override
	public PageSupport<PaySettlementItemDTO> querySettlementExceptionList(PaySettlementQuery query) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(PaySettlementItemDTO.class, query.getPageSize(), query.getCurPage());

		QueryMap queryMap = new QueryMap();
		if (null != query.getStartTime()) {
			queryMap.put("startTime", DateUtil.beginOfDay(query.getStartTime()));
		}
		if (null != query.getEndTime()) {
			queryMap.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}

		simpleSqlQuery.setSqlAndParameter("PaySettlement.exceptionPage", queryMap);
		return super.querySimplePage(simpleSqlQuery);
	}
}
