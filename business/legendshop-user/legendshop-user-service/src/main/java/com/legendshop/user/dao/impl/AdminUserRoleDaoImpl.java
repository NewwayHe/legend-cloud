/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.GenericJdbcDao;
import com.legendshop.user.dao.AdminUserRoleDao;
import com.legendshop.user.entity.AdminUserRole;
import com.legendshop.user.enums.RoleTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class AdminUserRoleDaoImpl implements AdminUserRoleDao {

	@Autowired
	private GenericJdbcDao genericJdbcDao;

	@Override
	public List<AdminUserRole> getByUserId(Long userId) {


		return genericJdbcDao.query("select user_id,role_id from ls_admin_user_role where user_id = ?", new Object[]{userId}, new UserRoleRowMapper());
	}

	@Override
	public List<AdminUserRole> getUserRoleByRoleId(String roleId) {
		return genericJdbcDao.query("select user_id,role_id,app_no  from ls_admin_user_role where role_id = ?", new Object[]{roleId}, new UserRoleRowMapper());
	}

	@Override
	public AdminUserRole getUserRoleById(Long id) {
		return genericJdbcDao.get("select * from ls_admin_user_role  where id ?", new Object[]{id}, new UserRoleRowMapper());
	}

	@Override
	public int deleteUserRoleById(Long id) {
		return genericJdbcDao.update("delete  from ls_admin_user_role  where user_id", id);
	}

	@Override
	public int deleteUserRole(AdminUserRole adminUserRole) {
		return 0;
	}

	@Override
	public int saveUserRole(AdminUserRole adminUserRole) {
		return genericJdbcDao.update("insert into ls_admin_user_role(user_id,role_id)  values (?,?)", adminUserRole.getUserId(), adminUserRole.getRoleId());
	}

	@Override
	public Boolean saveUserRoleList(List<AdminUserRole> adminUserRoles) {
		if (CollUtil.isEmpty(adminUserRoles)) {
			return Boolean.FALSE;
		}
		List<Object[]> batchArgs = new ArrayList<>();
		adminUserRoles.forEach(userRole -> {
			Object[] param = new Object[2];
			param[0] = userRole.getUserId();
			param[1] = userRole.getRoleId();
			batchArgs.add(param);
		});
		genericJdbcDao.batchUpdate("insert into ls_admin_user_role(user_id,role_id)  values (?,?)", batchArgs);
		return Boolean.TRUE;
	}

	@Override
	public int deleteUserRole(List<AdminUserRole> adminUserRoleList) {
		if (adminUserRoleList == null || adminUserRoleList.size() == 0) {
			return 0;
		}
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (AdminUserRole ur : adminUserRoleList) {
			Object[] param = new Object[3];
			param[0] = ur.getUserId();
			param[1] = ur.getRoleId();
			batchArgs.add(param);
		}
		genericJdbcDao.batchUpdate("delete  from ls_admin_user_role  where user_id = ? and role_id = ? and app_no = ?", batchArgs);
		return 1;
	}

	/**
	 * 删除用户的权限
	 */
	@Override
	public void deleteByUserId(Long userId, RoleTypeEnum roleType) {
		genericJdbcDao.update("delete from ls_admin_user_role  where user_id = ?", userId);
	}


	@Override
	public void deleteByUserIdAndRoleId(String id, String roleId) {
		this.genericJdbcDao.update("delete  from ls_admin_user_role  where user_id = ?  and role_id = ?", id, roleId);
	}

	@Override
	public Integer isExistUserRole(Long userId, String roldId) {
		return this.genericJdbcDao.get("select count(*) from ls_admin_user_role where user_id = ?  and role_id = ?", Integer.class, userId, roldId);
	}

	private class UserRoleRowMapper implements RowMapper<AdminUserRole> {
		@Override
		public AdminUserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
			AdminUserRole ur = new AdminUserRole();
			ur.setUserId(rs.getLong("user_id"));
			ur.setRoleId(rs.getLong("role_id"));
			return ur;
		}
	}

}
