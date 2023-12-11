/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.util.StringUtils;
import com.legendshop.shop.dao.PlateCapitalFlowDao;
import com.legendshop.shop.entity.PlateCapitalFlow;
import com.legendshop.shop.query.PlateCapitalFlowQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 平台资金流水(PlateCapitalFlow)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-09-18 17:26:09
 */
@Repository
public class PlateCapitalFlowDaoImpl extends GenericDaoImpl<PlateCapitalFlow, Long> implements PlateCapitalFlowDao {

	@Override
	public PageSupport<PlateCapitalFlow> queryPage(PlateCapitalFlowQuery plateCapitalFlowQuery) {
		CriteriaQuery cq = new CriteriaQuery(PlateCapitalFlow.class, plateCapitalFlowQuery.getPageSize(), plateCapitalFlowQuery.getCurPage());
		cq.eq("flowType", plateCapitalFlowQuery.getFlowType());
		cq.eq("dealType", plateCapitalFlowQuery.getDealType());
		if (StringUtils.isNotBlank(plateCapitalFlowQuery.getStartDate())) {
			cq.gt("recDate", DateUtil.beginOfDay(DateUtil.parseDate(plateCapitalFlowQuery.getStartDate())));
		}
		if (StringUtils.isNotBlank(plateCapitalFlowQuery.getEndDate())) {
			cq.lt("recDate", DateUtil.endOfDay(DateUtil.parseDate(plateCapitalFlowQuery.getEndDate())));
		}
		cq.addDescOrder("recDate");
		return queryPage(cq);
	}


	@Override
	public List<PlateCapitalFlow> queryList(PlateCapitalFlowQuery q) {
		EntityCriterion cq = new EntityCriterion();
		if (StrUtil.isNotBlank(q.getFlowType())) {
			cq.eq("flowType", q.getFlowType());
		}
		if (StrUtil.isNotBlank(q.getDealType())) {
			cq.eq("dealType", q.getDealType());
		}
		if (ObjectUtil.isNotNull(q.getStartDate())) {
			cq.gt("recDate", DateUtil.beginOfDay(DateUtil.parseDate(q.getStartDate())));
		}
		if (ObjectUtil.isNotNull(q.getEndDate())) {
			cq.lt("recDate", DateUtil.endOfDay(DateUtil.parseDate(q.getEndDate())));
		}
		cq.addDescOrder("recDate");
		return queryByProperties(cq);
	}


	@Override
	public BigDecimal getSumAmount(String flowType) {
		String sql = "SELECT SUM(amount) FROM ls_plate_capital_flow WHERE flow_type=?";
		return get(sql, BigDecimal.class, flowType);
	}
}
