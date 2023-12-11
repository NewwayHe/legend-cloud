/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.SysParamsDao;
import com.legendshop.basic.entity.SysParams;
import com.legendshop.basic.query.SysParamPageQuery;
import com.legendshop.basic.query.SysParamQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (SysParams)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-08-28 12:00:45
 */
@Repository
public class SysParamsDaoImpl extends GenericDaoImpl<SysParams, Long> implements SysParamsDao {

	@Override
	public PageSupport<SysParams> queryPageList(SysParamQuery sysParamQuery) {
		CriteriaQuery cq = new CriteriaQuery(SysParams.class, sysParamQuery.getPageSize(), sysParamQuery.getCurPage());
		cq.eq("groupBy", sysParamQuery.getGroup());
		//cq.addOr()
		cq.like("name", sysParamQuery.getName(), MatchMode.ANYWHERE);
		// 排序
		cq.addDescOrder("sort");
		return queryPage(cq);
	}


	@Override
	public SysParams getByName(String name) {
		return getByProperties(new EntityCriterion().eq("name", name));
	}

	@Override
	public List<SysParams> getByGroup(String group) {
		return queryByProperties(new EntityCriterion().eq("groupBy", group).addDescOrder("sort"));
	}

	@Override
	public PageSupport<SysParams> getByGroupPage(SysParamPageQuery query) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(SysParams.class, query.getPageSize(), query.getCurPage());
		criteriaQuery.addDescOrder("sort");
		if (StringUtils.isNotBlank(query.getGroup())) {
			criteriaQuery.eq("groupBy", query.getGroup());
		}
		criteriaQuery.like("des", query.getDes(), MatchMode.ANYWHERE);
		return queryPage(criteriaQuery);
	}
}
