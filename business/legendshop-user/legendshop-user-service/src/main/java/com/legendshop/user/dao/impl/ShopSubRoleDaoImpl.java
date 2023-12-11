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
import com.legendshop.user.dao.ShopSubRoleDao;
import com.legendshop.user.entity.ShopSubRole;
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
public class ShopSubRoleDaoImpl extends GenericDaoImpl<ShopSubRole, Long> implements ShopSubRoleDao {


	@Override
	public List<ShopSubRole> getByUserId(Long userId) {
		String sql = getSQL("ShopSubRole.getByUserId");
		return super.query(sql, ShopSubRole.class, userId);
	}

	@Override
	public List<ShopSubRole> getShopSubRoleByShopId(Long shopId) {
		return this.queryByProperties(new EntityCriterion().eq("shopId", shopId));
	}

	@Override
	public PageSupport<ShopSubRole> getShopSubRolePage(ShopRoleQuery query) {
		CriteriaQuery cq = new CriteriaQuery(ShopSubRole.class, query.getCurPage());
		cq.setPageSize(query.getPageSize());
		if (null != query.getUserId()) {
			cq.eq("shopId", query.getUserId());
		}

		if (StringUtils.isNotBlank(query.getName())) {
			cq.like("username", query.getName(), MatchMode.ANYWHERE);
		}

		cq.addAscOrder("createTime");
		return queryPage(cq);
	}

	@Override
	public List<ShopSubRole> queryAll(ShopRoleQuery query) {
		EntityCriterion entityCriterion = new EntityCriterion();

		if (null != query.getUserId()) {
			entityCriterion.eq("shopId", query.getUserId());
		}

		if (StringUtils.isNotBlank(query.getName())) {
			entityCriterion.like("username", query.getName(), MatchMode.ANYWHERE);
		}

		entityCriterion.addAscOrder("createTime");
		return queryByProperties(entityCriterion);
	}

	@Override
	public Integer deleteByRoleId(Long id) {
		return update("update ls_shop_sub_user_role set role_id = null where role_id = ?", id);
	}

	@Override
	public List<Long> queryByRoleId(Long id) {
		return query("select id from ls_shop_sub_user_role where role_id = ? ", Long.class, id);
	}
}
