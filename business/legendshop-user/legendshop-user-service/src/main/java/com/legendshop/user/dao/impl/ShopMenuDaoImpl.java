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
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dao.ShopMenuDao;
import com.legendshop.user.entity.ShopMenu;
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
public class ShopMenuDaoImpl extends GenericDaoImpl<ShopMenu, Long> implements ShopMenuDao {

	@Override
	public List<MenuBO> getByRoleId(Long roleId, String userType) {
		String sql;
		if (UserTypeEnum.SHOP_SUB_USER.equals(UserTypeEnum.valueOf(userType))) {
			sql = getSQL("ShopMenu.getSubUserByRoleId", new QueryMap().put("roleId", roleId));
		} else {
			sql = getSQL("ShopMenu.getByRoleId", new QueryMap().put("roleId", roleId));
		}

		return this.query(sql, MenuBO.class, roleId);
	}

	@Override
	public List<ShopMenu> getByParentId(Long parentId) {
		return this.queryByProperties(new EntityCriterion().eq("parentId", parentId).addAscOrder("sort"));
	}

	@Override
	public List<MenuBO> getByRoleIds(List<Long> roleIds) {
		if (CollectionUtils.isEmpty(roleIds)) {
			return new ArrayList<>();
		}
		QueryMap queryMap = new QueryMap();
		queryMap.put("roleIds", roleIds);
		String sql = getSQL("ShopMenu.getByRoleIds", queryMap);
		return this.query(sql, MenuBO.class, roleIds.toArray());
	}

	@Override
	public List<MenuBO> getSubUserByRoleIds(List<Long> roleIds) {
		if (CollectionUtils.isEmpty(roleIds)) {
			return new ArrayList<>();
		}
		QueryMap queryMap = new QueryMap();
		queryMap.put("roleIds", roleIds);
		String sql = getSQL("ShopMenu.getSubUserByRoleIds", queryMap);
		return this.query(sql, MenuBO.class, roleIds.toArray());
	}

	@Override
	public List<MenuBO> getAll() {
		return this.query("SELECT ls_shop_menu.* FROM ls_shop_menu LEFT JOIN ls_shop_sub_role_menu ON ls_shop_menu.id = ls_shop_sub_role_menu.menu_id ORDER BY ls_shop_menu.sort DESC", MenuBO.class);
	}

	@Override
	public List<ShopMenu> queryByType(String type) {
		return this.queryByProperties(new EntityCriterion().eq("type", type));
	}
}
