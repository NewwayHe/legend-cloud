/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;


import com.legendshop.user.entity.AdminRoleMenu;

import java.util.List;

/**
 * 角色菜单表
 *
 * @author legendshop
 */
public interface AdminRoleMenuDao {

	/**
	 * 检查是否有子菜单.
	 */
	List<AdminRoleMenu> getByRoleId(Long roleId);

	/**
	 * 根据菜单id删除角色菜单
	 *
	 * @param menuId
	 */
	void deleteByMenuId(Long menuId);

	/**
	 * 根据角色id删除角色菜单
	 *
	 * @param roleId
	 */
	void deleteByRoleId(Long roleId);

	/**
	 * 批量保存角色菜单
	 *
	 * @param roleMenuList
	 */
	void batchSave(List<AdminRoleMenu> roleMenuList);


}
