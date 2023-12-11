/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.dao.ShopRoleDao;
import com.legendshop.user.entity.ShopRole;
import com.legendshop.user.query.ShopRoleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色Dao
 *
 * @author legendshop
 */
@Repository
public class ShopRoleDaoImpl extends GenericDaoImpl<ShopRole, Long> implements ShopRoleDao {


	@Override
	public List<ShopRole> getByUserId(Long userId) {
		String sql = getSQL("ShopRole.getByUserId");
		return super.query(sql, ShopRole.class, userId);
	}


	@Override
	public PageSupport<ShopRole> queryRolePage(ShopRoleQuery query) {
		CriteriaQuery cq = new CriteriaQuery(ShopRole.class, query.getCurPage());
		cq.setPageSize(query.getPageSize());
		if (StringUtils.isNotBlank(query.getName())) {
			cq.like("roleName", query.getName(), MatchMode.ANYWHERE);
		}
		cq.addAscOrder("createTime");
		return queryPage(cq);
	}

	@Override
	public List<ShopRole> queryAll(ShopRoleQuery query) {
		EntityCriterion entityCriterion = new EntityCriterion();
		if (StringUtils.isNotBlank(query.getName())) {
			entityCriterion.like("roleName", query.getName(), MatchMode.ANYWHERE);
		}
		entityCriterion.addAscOrder("createTime");
		return queryByProperties(entityCriterion);
	}
}
