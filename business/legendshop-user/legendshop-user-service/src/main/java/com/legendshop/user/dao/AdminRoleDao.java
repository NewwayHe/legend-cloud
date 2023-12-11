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
import cn.legendshop.jpaplus.support.PageParams;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.entity.AdminRole;
import com.legendshop.user.query.RoleQuery;

import java.util.List;

/**
 * 角色Dao
 *
 * @author legendshop
 */
public interface AdminRoleDao extends GenericDao<AdminRole, Long> {

	/**
	 * 根据用户ID和appNo查找用户的角色名称列表。
	 *
	 * @param userId 用户ID
	 * @param appNo
	 * @return 用户的角色名称列表
	 */
	List<String> findRolesNameByUser(Long userId, String appNo);

	/**
	 * 根据用户ID和appNo查找用户的功能列表。
	 *
	 * @param userId 用户ID
	 * @param appNo
	 * @return 用户的功能列表
	 */
	List<String> findFunctionsByUser(Long userId, String appNo);

	/**
	 * 根据店铺角色ID获取权限列表。
	 *
	 * @param shopRoleId 店铺角色ID
	 * @return 权限列表
	 */
	List<String> getPermByShopAccountRole(Long shopRoleId);

	/**
	 * 根据角色ID查找角色信息。
	 *
	 * @param roleId 角色ID
	 * @return 对应角色的信息
	 */
	AdminRole findRoleById(Long roleId);

	/**
	 * 根据角色ID删除角色。
	 *
	 * @param roleId 角色ID
	 */
	void deleteRoleById(Long roleId);

	/**
	 * 分页获取角色信息。
	 *
	 * @param roleQuery 角色查询条件
	 * @return 分页的角色信息
	 */
	PageSupport<AdminRole> page(RoleQuery roleQuery);

	/**
	 * 根据功能ID查找角色列表。
	 *
	 * @param functionId 功能ID
	 * @return 对应功能的角色列表
	 */
	List<AdminRole> findRoleByFunction(Long functionId);

	/**
	 * 分页获取所有角色信息。
	 *
	 * @param pageParams 分页参数
	 * @param name       角色名称
	 * @param enabled    是否启用
	 * @param appNo
	 * @param category   角色分类
	 * @return 分页的所有角色信息
	 */
	PageSupport<AdminRole> findAllRolePage(PageParams pageParams, String name, String enabled, String appNo, Integer category);

	/**
	 * 根据用户ID和appNo查找用户没有的角色列表。
	 *
	 * @param pageParams 分页参数
	 * @param userId     用户ID
	 * @param appNo
	 * @return 用户没有的角色列表
	 */
	PageSupport<AdminRole> findOtherRoleByUser(PageParams pageParams, Long userId, String appNo);

	/**
	 * 根据用户ID和appNo查找用户的角色列表。
	 *
	 * @param userId 用户ID
	 * @param appNo
	 * @return 用户的角色列表
	 */
	List<AdminRole> findRoleByUserId(Long userId, String appNo);

	/**
	 * 根据用户ID和appNo查找用户的角色ID列表。
	 *
	 * @param userId 用户ID
	 * @param appNo
	 * @return 用户的角色ID列表
	 */
	List<String> findRoleIdsByUser(Long userId, String appNo);

	/**
	 * 根据用户ID获取角色列表。
	 *
	 * @param userId 用户ID
	 * @return 用户对应的角色列表
	 */
	List<AdminRole> getByUserId(Long userId);


}
