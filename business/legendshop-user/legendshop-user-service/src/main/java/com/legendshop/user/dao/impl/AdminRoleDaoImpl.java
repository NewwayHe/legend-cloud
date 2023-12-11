/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.legendshop.jpaplus.GenericJdbcDao;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import com.legendshop.common.core.enums.ApplicationEnum;
import com.legendshop.user.dao.AdminRoleDao;
import com.legendshop.user.entity.AdminRole;
import com.legendshop.user.query.RoleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 角色Dao
 *
 * @author legendshop
 */
@Repository
public class AdminRoleDaoImpl extends GenericDaoImpl<AdminRole, Long> implements AdminRoleDao {

	@Autowired
	private GenericJdbcDao genericJdbcDao;

	private String sqlForFindRolesByUser = "SELECT DISTINCT r.role_type as name FROM ls_usr_role ur ,ls_role r WHERE r.enabled ='1'  AND ur.role_id=r.id AND r.app_no =? AND ur.user_id= ? ";

	private String sqlForFindFunctionsByUser = "select DISTINCT f.protect_function as name from ls_usr_role ur ,ls_role r,ls_permission p, ls_function f where r.enabled = '1'  and ur.role_id=r.id and r.id=p.role_id and p.function_id=f.id and r.app_no = ? and f.app_no = ? and ur.user_id= ?";

	private static String sqlQueryPermByShopAccountRole = "SELECT label FROM ls_shop_perm where role_id = ?";

	/**
	 * The find other role by user.
	 */
	private String findOtherRoleByUser = "select id,name,role_type as roleType, enabled,note from ls_role where app_no = ? and id not in (select r.id from ls_role r,ls_usr_role ur, #USER_TABLE# u where r.id=ur.role_id and u.id=ur.user_id and u.id = ?  and r.app_no = ?)";

	/**
	 * The find other role by user count.
	 */
	private String findOtherRoleByUserCount = "select count(*) from ls_role where  app_no = ? and id not in (select r.id from ls_role r,ls_usr_role ur, #USER_TABLE# u where r.id=ur.role_id and u.id=ur.user_id and u.id =  ? and r.app_no = ?)";

	/**
	 * The find role by user id.
	 */
	private String findRoleByUserId = "select r.id,r.name,r.role_type as roleType, r.enabled, r.note from ls_role r,ls_usr_role ur,#USER_TABLE# u where r.id=ur.role_id and u.id=ur.user_id and u.id = ? and r.app_no = ?";

	@Override
	public List<String> findRolesNameByUser(Long userId, String appNo) {
		return genericJdbcDao.query(sqlForFindRolesByUser, new Object[]{appNo, userId}, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int index) throws SQLException {
				return new String(rs.getString("name"));
			}
		});
	}

	@Override
	public List<String> findFunctionsByUser(Long userId, String appNo) {
		return genericJdbcDao.query(sqlForFindFunctionsByUser, new Object[]{appNo, appNo, userId}, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int index) throws SQLException {
				return new String(rs.getString("name"));
			}
		});
	}


	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	@Override
	public List<String> getPermByShopAccountRole(Long shopRoleId) {
		return genericJdbcDao.query(sqlQueryPermByShopAccountRole, String.class, shopRoleId);
	}

	@Override
	public AdminRole findRoleById(Long roleId) {
		return getByProperties(new EntityCriterion().eq("id", roleId));
	}

	@Override
	public void deleteRoleById(Long roleId) {
		this.deleteById(roleId);
		// 删除角色和权限对应的关系
		update("delete from ls_permission where role_id = ?", roleId);
	}

	@Override
	public PageSupport<AdminRole> page(RoleQuery roleQuery) {
		CriteriaQuery cq = new CriteriaQuery(AdminRole.class, roleQuery.getPageSize(), roleQuery.getCurPage())
				.like("roleName", roleQuery.getName(), MatchMode.ANYWHERE)
				.addDescOrder("createTime");
		return queryPage(cq);
	}

	@Override
	public List<AdminRole> findRoleByFunction(Long functionId) {
		String sql = "select r.id,r.name,r.role_type as roleType, r.enabled, r.note from ls_role r,ls_permission p,ls_function f where r.id = p.role_id and f.id=p.function_id and f.id = ?";
		return query(sql, AdminRole.class, functionId);
	}

	@Override
	public PageSupport<AdminRole> findAllRolePage(PageParams pageParams, String name, String enabled, String appNo, Integer category) {
		CriteriaQuery cq = new CriteriaQuery(AdminRole.class, pageParams.getPageSize(), pageParams.getCurPage());
		cq.like("name", name, MatchMode.ANYWHERE);
		cq.eq("enabled", enabled);
		cq.eq("appNo", appNo);
		cq.eq("category", category);
		cq.addDescOrder("category");
		return queryPage(cq);
	}


	@Override
	public PageSupport<AdminRole> findOtherRoleByUser(PageParams pageParams, Long userId, String appNo) {
		SimpleSqlQuery query = new SimpleSqlQuery(AdminRole.class, pageParams.getPageSize(), pageParams.getCurPage());

		String userTable = getUserTableByAppNo(appNo);

		String sqlForFindOtherRoleByUser = findOtherRoleByUser.replace("#USER_TABLE#", userTable);
		String sqlForFindOtherRoleByUserCount = findOtherRoleByUserCount.replace("#USER_TABLE#", userTable);

		query.setQueryString(sqlForFindOtherRoleByUser);
		query.setAllCountString(sqlForFindOtherRoleByUserCount);
		query.setParam(new Object[]{appNo, userId, appNo});

		return querySimplePage(query);
	}


	/**
	 * 根据前后台动态切换表.
	 *
	 * @param userId the user id
	 * @param appNo  the app no
	 * @return the list< role>
	 */
	@Override
	public List<AdminRole> findRoleByUserId(Long userId, String appNo) {
		String userTable = getUserTableByAppNo(appNo);
		String sql = findRoleByUserId.replace("#USER_TABLE#", userTable);
		return query(sql, AdminRole.class, userId, appNo);
	}


	/**
	 * 根据类型得出表结构.
	 *
	 * @param appNo the app no
	 * @return the user table by app no
	 */
	private String getUserTableByAppNo(String appNo) {
		String userTable = null;
		// 后台管理员
		if (ApplicationEnum.BACK_END.value().equals(appNo)) {
			userTable = "ls_admin_user";
		}
		// 前台用户
		else if (ApplicationEnum.FRONT_END.value().equals(appNo)) {
			userTable = "ls_user";
		}
		return userTable;
	}


	@Override
	public List<String> findRoleIdsByUser(Long userId, String appNo) {
		return genericJdbcDao.query(sqlForFindRolesByUser, new Object[]{appNo, userId}, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int index) throws SQLException {
				return rs.getString("name");
			}
		});
	}

	@Override
	public List<AdminRole> getByUserId(Long userId) {
		String sql = getSQL("AdminRole.getByUserId");
		return this.query(sql, AdminRole.class, userId);
	}

}
