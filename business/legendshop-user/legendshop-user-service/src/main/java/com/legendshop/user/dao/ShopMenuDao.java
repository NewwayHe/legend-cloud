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
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.entity.ShopMenu;

import java.util.List;

/**
 * 后台菜单Dao
 *
 * @author legendshop
 */
public interface ShopMenuDao extends GenericDao<ShopMenu, Long> {
	/**
	 * 根据角色ID和用户类型获取菜单列表
	 *
	 * @param roleId   角色ID
	 * @param userType 用户类型
	 * @return 符合角色ID和用户类型的菜单列表
	 */
	List<MenuBO> getByRoleId(Long roleId, String userType);

	/**
	 * 根据父级ID获取菜单列表
	 *
	 * @param parentId 父级菜单ID
	 * @return 对应父级ID的菜单列表
	 */
	List<ShopMenu> getByParentId(Long parentId);

	/**
	 * 根据角色ID列表获取菜单列表
	 *
	 * @param roleIds 角色ID列表
	 * @return 符合角色ID列表的菜单列表
	 */
	List<MenuBO> getByRoleIds(List<Long> roleIds);

	/**
	 * 根据角色ID列表获取子用户菜单列表
	 *
	 * @param roleIds 角色ID列表
	 * @return 符合角色ID列表的子用户菜单列表
	 */
	List<MenuBO> getSubUserByRoleIds(List<Long> roleIds);

	/**
	 * 获取所有菜单列表
	 *
	 * @return 所有菜单列表
	 */
	List<MenuBO> getAll();

	/**
	 * 根据类型查询菜单列表
	 *
	 * @param type 菜单类型
	 * @return 符合类型的菜单列表
	 */
	List<ShopMenu> queryByType(String type);
}
