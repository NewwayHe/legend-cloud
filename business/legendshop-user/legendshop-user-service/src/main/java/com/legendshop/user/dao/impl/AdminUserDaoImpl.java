/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.common.core.enums.ApplicationEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.user.dao.AdminUserDao;
import com.legendshop.user.dao.AdminUserRoleDao;
import com.legendshop.user.dto.UserRoleId;
import com.legendshop.user.entity.AdminUser;
import com.legendshop.user.entity.AdminUserRole;
import com.legendshop.user.query.AdminUserQuery;
import com.legendshop.user.vo.AdminRoleVO;
import com.legendshop.user.vo.AdminUserVO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员管理
 *
 * @author legendshop
 */
@Repository
public class AdminUserDaoImpl extends GenericDaoImpl<AdminUser, Long> implements AdminUserDao {


	@Autowired
	private AdminUserRoleDao adminUserRoleDao;


	@Override
	public Long save(AdminUser adminUser, String[] roleIds) {
		if (ArrayUtil.isEmpty(roleIds)) {
			throw new BusinessException("role id can not empty for user " + adminUser.getId());
		}
		Long userId = save(adminUser);
		List<AdminUserRole> adminUserRoles = new ArrayList<AdminUserRole>();
		for (String roleId : roleIds) {
			UserRoleId id = new UserRoleId(userId, roleId, ApplicationEnum.BACK_END.value());
		}
		adminUserRoleDao.saveUserRoleList(adminUserRoles);
		return userId;
	}


	@Override
	public PageSupport<AdminUserVO> page(AdminUserQuery adminUserQuery) {
		QueryMap paramMap = new QueryMap();
		paramMap.like("username", adminUserQuery.getUsername(), MatchMode.ANYWHERE);
		paramMap.put("lockFlag", adminUserQuery.getLockFlag());
		paramMap.like("deptName", adminUserQuery.getDeptName(), MatchMode.ANYWHERE);
		paramMap.like("roleName", adminUserQuery.getRoleName(), MatchMode.ANYWHERE);

		SimpleSqlQuery hql = new SimpleSqlQuery(AdminUserVO.class, adminUserQuery.getPageSize(), adminUserQuery.getCurPage());
		hql.setSqlAndParameter("AdminUser.queryPage", paramMap);
		ResultSetExtractor<List<AdminUserVO>> resultSetExtractor = rs -> {
			List<AdminUserVO> list = new ArrayList<>();
			while (rs.next()) {
				list.add(buildAdminUser(rs));
			}
			return list;
		};
		return querySimplePage(hql, resultSetExtractor);

	}

	@SneakyThrows
	private AdminUserVO buildAdminUser(ResultSet rs) {
		AdminUserVO adminUserVO = new AdminUserVO();
		adminUserVO.setId(rs.getLong("userId"));
		adminUserVO.setUsername(rs.getString("username"));
		adminUserVO.setAvatar(rs.getString("avatar"));
		adminUserVO.setDelFlag(rs.getBoolean("delFlag"));
		adminUserVO.setLockFlag(rs.getBoolean("lockFlag"));
		adminUserVO.setCreateTime(rs.getTimestamp("createTime"));
		adminUserVO.setDeptId(rs.getInt("deptId"));
		adminUserVO.setDeptName(rs.getString("deptName"));

		String roleIds = rs.getString("roleId");
		String roleNames = rs.getString("roleName");
		if (StrUtil.isNotEmpty(roleIds)) {
			String[] roleIdArray = roleIds.split(",");
			String[] roleNameArray = roleNames.split(",");
			List<AdminRoleVO> roleDTOList = new ArrayList<>();
			for (int i = 0; i < roleIdArray.length; i++) {
				AdminRoleVO adminRoleVO = new AdminRoleVO();
				adminRoleVO.setId(roleIdArray[i]);
				adminRoleVO.setRoleName(roleNameArray[i]);
				roleDTOList.add(adminRoleVO);
			}
			adminUserVO.setRoleList(roleDTOList);
		}
		return adminUserVO;
	}

	@Override
	public AdminUser getUserByIdUsername(Long userId, String username) {
		return this.getByProperties(new EntityCriterion().eq("id", userId).eq("username", username));
	}


	@Override
	public AdminUser getUserByUsername(String username) {
		return this.getByProperties(new EntityCriterion().eq("username", username));
	}

	@Override
	public List<Long> queryUserByMenuId(Long menuId) {
		QueryMap paramMap = new QueryMap();
		paramMap.put("menuId", menuId);
		SQLOperation operation = this.getSQLAndParams("AdminUser.queryUserByMenuId", paramMap);
		return query(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Long.class));
	}

	@Override
	public List<Long> queryUserIdsByMenuName(String menuName) {
		return query("SELECT user_id FROM ls_admin_user_role WHERE role_id IN (SELECT role_id FROM ls_admin_role_menu  WHERE menu_id=(SELECT id FROM ls_admin_menu WHERE NAME=?))", Long.class, menuName);
	}

	@Override
	public List<Long> getAdmin() {
		return query("SELECT id FROM ls_admin_user", Long.class);
	}

	@Override
	public int updateStatusByUserName(String username, Boolean status) {
		return super.update("UPDATE ls_admin_user SET lock_flag = ? WHERE username = ? ", status, username);
	}

}
