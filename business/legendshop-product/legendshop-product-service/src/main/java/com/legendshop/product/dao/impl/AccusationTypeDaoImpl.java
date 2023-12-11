/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.dao.AccusationTypeDao;
import com.legendshop.product.entity.AccusationType;
import com.legendshop.product.query.AccusationTypeQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 举报类型Dao
 *
 * @author legendshop
 */
@Repository
public class AccusationTypeDaoImpl extends GenericDaoImpl<AccusationType, Long> implements AccusationTypeDao {

	@Override
	public PageSupport<AccusationType> queryPage(AccusationTypeQuery query) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(AccusationType.class, query.getPageSize(), query.getCurPage());
		criteriaQuery.like("name", query.getName(), MatchMode.ANYWHERE);
		criteriaQuery.eq("status", query.getStatus());
		criteriaQuery.addDescOrder("createTime");
		criteriaQuery.addDescOrder("updateTime");
		return queryPage(criteriaQuery);
	}

	@Override
	public List<AccusationType> queryAllOnLine() {
		return query("select id,name from ls_accusation_type where status =1", AccusationType.class);
	}

	@Override
	public int batchUpdateStatus(List<Long> ids, Integer status) {
		if (ids == null || ids.size() == 0) {
			return 0;
		}

		StringBuffer sb = new StringBuffer("update ls_accusation_type s set s.status=?,s.update_time=NOW() where id in( ");
		List<Long> list = new ArrayList<>();
		list.add(status.longValue());
		for (Long id : ids) {
			sb.append("?,");
			list.add(id);
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return update(sb.toString(), list.toArray());
	}

}
