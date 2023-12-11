/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.QueryMap;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dao.AdminMenuDao;
import com.legendshop.user.entity.AdminMenu;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单Dao.
 *
 * @author legendshop
 */
@Repository
public class AdminMenuDaoImpl extends GenericDaoImpl<AdminMenu, Long> implements AdminMenuDao {

	@Override
	public List<MenuBO> getByRoleId(Long roleId) {
		String sql = getSQL("AdminMenu.getByRoleId", new QueryMap().put("roleId", roleId));
		return this.query(sql, MenuBO.class, roleId);
	}

	@Override
	public List<MenuBO> getByRoleIds(List<Long> roleIds) {
		if (CollectionUtils.isEmpty(roleIds)) {
			return new ArrayList<>();
		}
		QueryMap queryMap = new QueryMap();
		queryMap.put("roleIds", roleIds);
		String sql = getSQL("AdminMenu.getByRoleIds", queryMap);
		return this.query(sql, MenuBO.class, roleIds.toArray());
	}

	@Override
	public List<AdminMenu> getByParentId(Long parentId) {
		return this.queryByProperties(new EntityCriterion().eq("parentId", parentId).addAscOrder("sort"));
	}

	@Override
	public List<AdminMenu> queryByType(String type) {
		return this.queryByProperties(new EntityCriterion().eq("type", type));
	}
}
