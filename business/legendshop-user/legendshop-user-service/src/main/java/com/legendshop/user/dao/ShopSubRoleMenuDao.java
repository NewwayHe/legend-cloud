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
import com.legendshop.user.entity.ShopSubRoleMenu;

import java.util.List;

/**
 * 商家子账号菜单权限
 *
 * @author legendshop
 */
public interface ShopSubRoleMenuDao extends GenericDao<ShopSubRoleMenu, Long> {

	/**
	 * 根据角色ID查询对应的商店子角色菜单列表。
	 *
	 * @param roleId 角色ID
	 * @return 匹配角色ID的商店子角色菜单列表
	 */
	List<ShopSubRoleMenu> queryByRoleId(Long roleId);

	/**
	 * 根据角色ID列表查询对应的商店子角色菜单列表。
	 *
	 * @param roleIds 角色ID列表
	 * @return 匹配角色ID列表的商店子角色菜单列表
	 */
	List<ShopSubRoleMenu> queryByRoleIds(List<Long> roleIds);

	/**
	 * 保存给定角色ID的菜单列表，并关联到对应角色。
	 *
	 * @param menuIds 菜单ID列表
	 * @param roleId  角色ID
	 * @return 已保存的关联菜单ID列表
	 */
	List<Long> save(List<Long> menuIds, Long roleId);

	/**
	 * 保存单个菜单ID并关联到给定的角色ID。
	 *
	 * @param menuId 菜单ID
	 * @param roleId 角色ID
	 * @return 已保存的关联菜单ID
	 */
	Long save(Long menuId, Long roleId);

	/**
	 * 根据角色ID删除相关的商店子角色菜单。
	 *
	 * @param roleId 角色ID
	 * @return 删除的商店子角色菜单数量
	 */
	int deleteByRoleId(Long roleId);

}
