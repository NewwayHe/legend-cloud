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
import com.legendshop.user.dao.OrdinaryRoleDao;
import com.legendshop.user.entity.OrdinaryRole;
import com.legendshop.user.query.OrdinaryRoleQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色Dao
 *
 * @author legendshop
 */
@Repository
public class OrdinaryRoleDaoImpl extends GenericDaoImpl<OrdinaryRole, Long> implements OrdinaryRoleDao {
	@Override
	public List<OrdinaryRole> getByUserId(Long userId) {
		String sql = getSQL("OrdinaryRole.getByUserId");
		return super.query(sql, OrdinaryRole.class, userId);
	}

	@Override
	public PageSupport<OrdinaryRole> queryRolePage(OrdinaryRoleQuery query) {
		CriteriaQuery cq = new CriteriaQuery(OrdinaryRole.class, query.getCurPage());
		cq.setPageSize(query.getPageSize());
		cq.like("roleName", query.getName(), MatchMode.ANYWHERE);
		cq.addAscOrder("createTime");
		return queryPage(cq);
	}

	@Override
	public List<OrdinaryRole> queryAll(OrdinaryRoleQuery query) {
		EntityCriterion entityCriterion = new EntityCriterion(true);
		entityCriterion.like("username", query.getName(), MatchMode.ANYWHERE);
		entityCriterion.addAscOrder("createTime");
		return queryByProperties(entityCriterion);
	}

	@Override
	public List<OrdinaryRole> queryByUserId(Long id) {
		return super.query("SELECT r.* FROM ls_ordinary_role r LEFT JOIN ls_ordinary_user_role ur ON r.id = ur.role_id WHERE ur.user_id = ?", OrdinaryRole.class, id);
	}
}
