/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;


import com.legendshop.user.dao.AdminRoleMenuDao;
import com.legendshop.user.entity.AdminRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色菜单表
 *
 * @author legendshop
 */
@Repository
public class AdminRoleMenuDaoImpl implements AdminRoleMenuDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sqlForGetRoleMenu = "SELECT f.menu_id,p.role_id,r.role_type AS role_name FROM ls_role r,ls_perm p,ls_func f WHERE r.id = p.role_id AND f.id = p.function_id AND f.menu_id IS NOT NULL ORDER BY f.menu_id";

	@Override
	public List<AdminRoleMenu> getByRoleId(Long roleId) {
		List<AdminRoleMenu> roleMenuList = new ArrayList<>();
		jdbcTemplate.query(sqlForGetRoleMenu, new Object[]{roleId}, new BeanPropertyRowMapper<>(AdminRoleMenu.class));
		return roleMenuList;
	}

	@Override
	public void deleteByMenuId(Long menuId) {
		String sql = "delete from ls_admin_role_menu where menu_id=?";
		jdbcTemplate.update(sql, menuId);
	}

	@Override
	public void deleteByRoleId(Long roleId) {
		String sql = "delete from ls_admin_role_menu where role_id=?";
		jdbcTemplate.update(sql, roleId);
	}

	@Override
	public void batchSave(List<AdminRoleMenu> roleMenuList) {
		String sql = "insert into ls_admin_role_menu(role_id,menu_id) values(?,?)";
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return roleMenuList.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setLong(1, roleMenuList.get(i).getRoleId());
				ps.setLong(2, roleMenuList.get(i).getMenuId());
			}
		});
	}
}
