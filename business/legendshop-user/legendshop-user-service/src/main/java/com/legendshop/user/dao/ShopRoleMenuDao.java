/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.user.entity.ShopRoleMenu;

import java.util.List;

/**
 * 角色菜单关联关系
 *
 * @author legendshop
 */
public interface ShopRoleMenuDao extends GenericDao<ShopRoleMenu, Long> {
	void deleteByRoleId(Long roleId);

	void batchSave(List<ShopRoleMenu> roleMenuList);

	/**
	 * 根据角色ID获取菜单列表
	 *
	 * @param roleId
	 * @return
	 */
	List<ShopRoleMenu> getShopRoleMenuByRoleId(Long roleId);
}
