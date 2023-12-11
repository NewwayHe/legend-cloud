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
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.user.bo.OrdinaryUserBO;
import com.legendshop.user.dao.OrdinaryUserDao;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.entity.OrdinaryUser;
import com.legendshop.user.query.OrdinaryUserQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户Dao
 *
 * @author legendshop
 */
@Repository
public class OrdinaryUserDaoImpl extends GenericDaoImpl<OrdinaryUser, Long> implements OrdinaryUserDao {


	private QueryMap map;

	/**
	 * 用户是否存在
	 */
	@Override
	public boolean isUserExists(Long userId) {
		List<String> list = this.query("select 1 from ls_user_detail where user_id = ?", String.class, userId);
		return CollUtil.isNotEmpty(list);
	}

	@Override
	public OrdinaryUser getUser(String identifier) {
		return this.get("SELECT * FROM ls_ordinary_user WHERE username = ? OR mobile = ? OR id = ?", OrdinaryUser.class, identifier, identifier, identifier);
	}

	@Override
	public PageSupport<OrdinaryUser> page(OrdinaryUserQuery ordinaryUserQuery) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(OrdinaryUser.class, ordinaryUserQuery.getPageSize(), ordinaryUserQuery.getCurPage());
		criteriaQuery.like("username", ordinaryUserQuery.getUsername(), MatchMode.ANYWHERE);
		criteriaQuery.like("mobile", ordinaryUserQuery.getMobilePhone(), MatchMode.ANYWHERE);
		criteriaQuery.eq("lockFlag", ordinaryUserQuery.getLockFlag());
		criteriaQuery.addDescOrder("createTime");
		return this.queryPage(criteriaQuery);
	}

	@Override
	public OrdinaryUser getByMobile(String mobile) {
		return this.getByProperties(new EntityCriterion().eq("mobile", mobile).eq("delFlag", Boolean.TRUE));
	}

	@Override
	public List<OrdinaryUser> queryByMobile(List<String> mobileList) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("SELECT * FROM ls_ordinary_user u where u.del_flag=1 and u.mobile in(");
		for (String ignored : mobileList) {
			buffer.append("?,");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(")");
		return query(buffer.toString(), OrdinaryUser.class, mobileList.toArray());
	}

	@Override
	public String getMobileByUserId(Long userId) {
		return this.get("SELECT mobile FROM ls_ordinary_user WHERE id = ?", String.class, userId);
	}

	@Override
	public String getMobileByUsername(String username) {
		return this.get("SELECT mobile FROM ls_ordinary_user WHERE username = ?", String.class, username);
	}

	@Override
	public boolean updateLoginPassword(OrdinaryUserDTO ordinaryUserDTO) {
		Long id = ordinaryUserDTO.getId();
		String password = ordinaryUserDTO.getPassword();
		if (null == id) {
			return false;
		}
		if (StringUtils.isBlank(password)) {
			return false;
		}
		return this.update("UPDATE ls_ordinary_user SET password = ? , update_time = NOW() WHERE id = ? ", password, id) > 0;
	}

	@Override
	public boolean updateStatus(Long userId) {
		return super.update("UPDATE ls_ordinary_user SET lock_flag = !lock_flag WHERE id = ?", userId) > 0;
	}

	@Override
	public int updateStatusByUserName(String userName, Boolean status) {
		return super.update("UPDATE ls_ordinary_user SET lock_flag = ? WHERE username = ? ", status, userName);
	}

	@Override
	public int updateAvatar(Long userId, String avatar) {
		return super.update("UPDATE ls_ordinary_user SET avatar = ? WHERE id=?", avatar, userId);
	}

	@Override
	public boolean batchUpdateStatus(BasicBatchUpdateStatusDTO basicBatchUpdateStatusDTO) {

		List<Long> args = new ArrayList<>();
		args.add(Long.valueOf(basicBatchUpdateStatusDTO.getStatus()));
		StringBuffer sb = new StringBuffer("update ls_ordinary_user u set u.lock_flag=? where u.id in(");
		for (Long id : basicBatchUpdateStatusDTO.getIds()) {
			args.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return update(sb.toString(), args.toArray()) > 0;
	}

	@Override
	public List<Long> getUserIds(Integer off, Integer size) {
		return super.query("SELECT id FROM ls_ordinary_user LIMIT ?, ?", Long.class, off, size);
	}

	@Override
	public boolean isMobileExist(String mobile) {
		return get("SELECT count(1)  FROM ls_ordinary_user WHERE mobile=?", Integer.class, mobile) > 0;
	}

	@Override
	public int updateMobileByUserId(Long userId, String mobile) {
		String sql = "update ls_ordinary_user set mobile = ? where id = ?";
		return update(sql, mobile, userId);
	}

	@Override
	public PageSupport<OrdinaryUserBO> pageTwo(OrdinaryUserQuery ordinaryUserQuery) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(OrdinaryUserBO.class, ordinaryUserQuery.getPageSize(), ordinaryUserQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("username", ordinaryUserQuery.getUsername());
		map.put("mobile", ordinaryUserQuery.getMobilePhone());
		map.put("nickname", ordinaryUserQuery.getNickName());
		map.put("lockFlag", ordinaryUserQuery.getLockFlag());
		simpleSqlQuery.setSqlAndParameter("OrdinaryUser.queryPage", map);
		PageSupport<OrdinaryUserBO> pageSupport = querySimplePage(simpleSqlQuery);
		return pageSupport;
	}

	@Override
	public PageSupport<OrdinaryUserDTO> queryAllUser(OrdinaryUserQuery ordinaryUserQuery) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(OrdinaryUserDTO.class, ordinaryUserQuery.getPageSize(), ordinaryUserQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("nickname", ordinaryUserQuery.getNickName());
		simpleSqlQuery.setSqlAndParameter("OrdinaryUser.querySearchPage", map);
		PageSupport<OrdinaryUserDTO> pageSupport = querySimplePage(simpleSqlQuery);
		return pageSupport;
	}

	@Override
	public List<OrdinaryUser> getByLikeMobile(String mobile) {
		return this.queryByProperties(new EntityCriterion().like("mobile", mobile, MatchMode.ANYWHERE).eq("delFlag", Boolean.TRUE));
	}
}
