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
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.user.dao.ShopSubRoleMenuDao;
import com.legendshop.user.entity.ShopSubRoleMenu;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class ShopSubRoleMenuDaoImpl extends GenericDaoImpl<ShopSubRoleMenu, Long> implements ShopSubRoleMenuDao {
	@Override
	public List<ShopSubRoleMenu> queryByRoleId(Long roleId) {
		return super.queryByProperties(new EntityCriterion().eq("roleId", roleId));
	}

	@Override
	public List<ShopSubRoleMenu> queryByRoleIds(List<Long> roleIds) {
		if (CollectionUtils.isEmpty(roleIds)) {
			return new ArrayList<>();
		}
		return super.queryByProperties(new EntityCriterion().in("roleId", roleIds));
	}

	@Override
	public List<Long> save(List<Long> menuIds, Long roleId) {
		if (null == roleId || CollectionUtils.isEmpty(menuIds)) {
			return new ArrayList<>();
		}
		List<ShopSubRoleMenu> roleMenuList = new ArrayList<>(menuIds.size());
		menuIds.forEach(e -> {
			ShopSubRoleMenu roleMenu = new ShopSubRoleMenu();
			roleMenu.setMenuId(e);
			roleMenu.setRoleId(roleId);
			roleMenuList.add(roleMenu);
		});
		return this.save(roleMenuList);
	}

	@Override
	public Long save(Long menuIds, Long role) {
		return null;
	}

	@Override
	@CacheEvict(value = CacheConstants.SHOP_MENU_DETAILS, key = "#roleId + '_SHOP_SUB_USER_menu'")
	public int deleteByRoleId(Long roleId) {
		return this.update("DELETE FROM ls_shop_sub_role_menu WHERE role_id = ? ", roleId);
	}
}
