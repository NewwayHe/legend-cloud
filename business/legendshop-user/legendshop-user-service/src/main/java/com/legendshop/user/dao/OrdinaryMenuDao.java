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
import com.legendshop.user.entity.OrdinaryMenu;

import java.util.List;

/**
 * 后台菜单Dao
 *
 * @author legendshop
 */
public interface OrdinaryMenuDao extends GenericDao<OrdinaryMenu, Long> {

	/**
	 * 根据角色ID获取菜单列表
	 *
	 * @param roleId 角色ID
	 * @return 角色所拥有的菜单列表
	 */
	List<MenuBO> getByRoleId(Long roleId);

	/**
	 * 根据角色ID列表获取菜单列表
	 *
	 * @param roleIds 角色ID列表
	 * @return 角色列表所拥有的菜单列表
	 */
	List<MenuBO> getByRoleIds(List<Long> roleIds);

	/**
	 * 根据父菜单ID获取菜单列表
	 *
	 * @param parentId 父菜单ID
	 * @return 指定父菜单下的菜单列表
	 */
	List<OrdinaryMenu> getByParentId(Long parentId);

}
