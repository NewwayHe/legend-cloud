/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;

import com.legendshop.user.entity.AdminUserRole;
import com.legendshop.user.enums.RoleTypeEnum;

import java.util.List;

/**
 * 用户角色Dao
 *
 * @author legendshop
 */
public interface AdminUserRoleDao {

	List<AdminUserRole> getByUserId(Long userId);

	List<AdminUserRole> getUserRoleByRoleId(String roleId);

	AdminUserRole getUserRoleById(Long id);

	int deleteUserRoleById(Long id);

	int deleteUserRole(List<AdminUserRole> adminUserRoleList);

	int saveUserRole(AdminUserRole adminUserRole);

	int deleteUserRole(AdminUserRole adminUserRole);

	Boolean saveUserRoleList(List<AdminUserRole> adminUserRoles);

	/**
	 * 删除用户对应的角色
	 *
	 * @param userId
	 */
	void deleteByUserId(Long userId, RoleTypeEnum roleTypeEnum);

	void deleteByUserIdAndRoleId(String id, String roldId);

	Integer isExistUserRole(Long userId, String roldId);

}
