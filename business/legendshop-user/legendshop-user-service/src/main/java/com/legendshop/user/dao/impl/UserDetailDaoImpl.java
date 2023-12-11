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
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.user.bo.UserBO;
import com.legendshop.user.dao.UserDetailDao;
import com.legendshop.user.dto.UserInformationDTO;
import com.legendshop.user.entity.UserDetail;
import com.legendshop.user.query.UserDetailQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 用户详情Dao
 *
 * @author legendshop
 */
@Repository
public class UserDetailDaoImpl extends GenericDaoImpl<UserDetail, Long> implements UserDetailDao {

	/**
	 * 用户最后登录时间
	 */
	@Override
	public Date getLastLoginTime(Long userId) {
		return this.getJdbcTemplate().queryForObject("select user_lasttime,user_regtime from ls_user_detail where id = ?", new Object[]{userId},
				(rs, rowNum) -> {
					Date lastLoginTime = rs.getDate("user_lasttime");
					if (lastLoginTime != null) {
						return lastLoginTime;
					} else {
						return rs.getDate("user_regtime"); // 用户注册时间
					}
				});
	}

	@Override
	public void updateLastLoginTime(Long userId) {
		super.update("update ls_user_detail set user_lasttime=? where id = ? ", new Date(), userId);
	}


	@Override
	public String getUserNameByMail(String email) {
		return super.get("select user_id from ls_user_detail where user_mail = ?", String.class, email);
	}

	@Override
	public String getShopUserNameByMail(String email) {
		return super.get(
				"SELECT u.user_id FROM ls_user_detail u LEFT JOIN ls_shop_detail d ON u.shop_id=d.shop_id WHERE u.user_mail = ? AND u.shop_id IS NOT NULL AND d.status ='1'",
				String.class, email);
	}

	@Override
	public String getUserNameByPhone(String phone) {
		return super.get("select user_id from ls_user_detail where user_mobile = ?", String.class, phone);
	}

	@Override
	public String getShopUserNameByPhone(String email) {
		return super.get(
				"SELECT u.user_id FROM ls_user_detail u LEFT JOIN ls_shop_detail d ON u.shop_id=d.shop_id WHERE u.user_mobile = ? AND u.shop_id IS NOT NULL AND d.status ='1'",
				String.class, email);
	}

	@Override
	public String getUserIdByOpenId(String openId, String type) {
		return super.get("select u.user_id from ls_user_detail u, ls_passport p where u.user_id = p.user_id and p.type= ? and p.open_id  = ?", String.class,
				type, openId);
	}

	/**
	 * 根据用户输入，把可能的邮件或者手机号码找到对应的用户名称，昵称一定要有一个英文符号
	 */
	@Override
	public String convertUserLoginName(String name) {
		if (Validator.isEmail(name)) {
			return this.getUserNameByMail(name);
		} else if (Validator.isMobile(name)) {
			return this.getUserNameByPhone(name);
		} else {
			return name;
		}
	}

	/**
	 * 根据用户输入，把可能的邮件或者手机号码找到对应的用户名称，昵称一定要有一个英文符号
	 */
	@Override
	public String convertShopUserLoginName(String name) {
		if (Validator.isEmail(name)) {
			return this.getShopUserNameByMail(name);
		} else if (Validator.isMobile(name)) {
			return this.getShopUserNameByPhone(name);
		} else {
			return name;
		}
	}


	@Override
	public Integer getUserScore(Long userId) {
		return get("select score from ls_user_detail where id=?", Integer.class, userId);
	}

	@Override
	public String getUserMobile(Long userId) {
		return get("select user_mobile from ls_user_detail where user_id = ?", String.class, userId);
	}


	@Override
	public Boolean updateNewPassword(Long userId, String newPassword) {
		int result = update("update ls_user set password = ? where name = ? ;", newPassword, userId);
		return result == 1;
	}


	@Override
	public UserBO getUserByWxOpenId(String wxOpenId) {
		if (StrUtil.isBlank(wxOpenId)) {
			return null;
		}
		return get("select u.* from ls_user u,ls_user_detail d where u.id = d.user_id and u.open_id = ?", UserBO.class, wxOpenId);
	}


	@Override
	public UserBO getUserByName(Long userId) {
		return null;
	}


	@Override
	public UserDetail getUserDetailById(Long userId) {
		return this.getByProperties(new EntityCriterion().eq("userId", userId));
	}

	/**
	 * 减去用户积分
	 */
	@Override
	public int updateScore(Integer updateSore, Integer oldScore, Long userId) {
		String updateSql = getSQL(this.getDialect().getDialectType() + ".updateScore");
		return update(updateSql, updateSore, oldScore, updateSore, userId);
	}

	/**
	 * 充值积分
	 */
	@Override
	public int updateScore(Integer updateSore, Long userId) {
		return update("update ls_user_detail set score = score + ? where user_id=? ", updateSore, userId);
	}

	@Override
	public String getuserIdByShopId(Long shopId) {
		return super.get("SELECT user_id FROM ls_user_detail WHERE shop_id=?", String.class, shopId);
	}


	@Override
	public UserDetail getUserDetailByShopId(Long shopId) {
		return get("SELECT u.id AS userId,u.name AS userId,u.password AS PASSWORD,ud.* FROM ls_user u,ls_user_detail ud,ls_shop_detail ls WHERE u.id=ud.id AND ud.id=ls.user_id AND ls.shop_id= ?",
				UserDetail.class, shopId);
	}


	@Override
	public Long getShopIdByUserId(Long userId) {
		String sql = "SELECT shop_id FROM ls_user_detail WHERE id = ?";
		return super.get(sql, Long.class, userId);
	}

	@Override
	public PageSupport<UserDetail> queryUserDetailPage(UserDetailQuery userDetailQuery) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(UserDetail.class, userDetailQuery.getCurPage());
		criteriaQuery.setPageSize(userDetailQuery.getPageSize());
		if (StringUtils.isNotBlank(userDetailQuery.getUsername())) {
			criteriaQuery.like("username", userDetailQuery.getUsername(), MatchMode.ANYWHERE);
		}
		if (StringUtils.isNotBlank(userDetailQuery.getMobilePhone())) {
			criteriaQuery.like("mobile", userDetailQuery.getMobilePhone(), MatchMode.ANYWHERE);
		}
		if (null != userDetailQuery.getLockFlag()) {
			criteriaQuery.eq("lockFlag", userDetailQuery.getLockFlag());
		}
		return this.queryPage(criteriaQuery);
	}

	@Override
	public UserBO getByOpenId(String openId) {
		String sql = "select id as id,name as name,password as password,enabled as enabled,note as note,dept_id as deptId,open_id as openId from ls_user where open_id=? ";
		return get(sql, UserBO.class, openId);

	}

	@Override
	public String getUserNameByPhoneOrUsername(String userMobile, Long userId) {
		return super.get("select user_id from ls_user_detail where user_mobile = ? or id = ?", String.class, userMobile, userId);
	}

	/**
	 * 记录用户的登录历史
	 */
	@Override
	public void updateUserLoginHistory(String ip, Long userId) {
		String sql = getSQL("login.updateUserDetail");
		if (log.isDebugEnabled()) {
			log.debug("Login Ip {}, userId {} ", ip, userId);
		}
		update(sql, ip, userId);
	}


	@Override
	public int updatePayPassword(Long userId, String payPassword) {
		String sql = "update ls_user_wallet set pay_password = ? where user_id = ?";
		return update(sql, payPassword, userId);
	}

	@Override
	public List<UserDetail> queryByUserId(List<Long> userIds) {
		if (CollUtil.isEmpty(userIds)) {
			return Collections.emptyList();
		}
		return queryByProperties(new EntityCriterion().in("userId", userIds));
	}

	@Override
	public String getNickNameByUserId(Long id) {
		return get("SELECT nick_name FROM ls_user_detail WHERE user_id = ?", String.class, id);
	}

	@Override
	@CacheEvict(value = "UserDetail", key = "#userId")
	public int updateConsumptionStatistics(Long userId, BigDecimal amount, Integer count) {
		if (count > 0) {
			String sql = "update ls_user_detail set consumption_amount = consumption_amount+?,consumption_order_count = consumption_order_count+?,recent_consumption_time = NOW() where user_id = ?";
			return update(sql, amount, count, userId);
		} else {
			String sql = "update ls_user_detail set consumption_amount = consumption_amount-?,consumption_order_count = consumption_order_count+? where user_id = ? and consumption_amount-?>=0 and consumption_order_count+?>=0";
			return update(sql, amount, count, userId, amount, count);
		}

	}


	@Override
	public List<UserInformationDTO> getUserInfoByIds(List<Long> userIds) {
		if (CollUtil.isEmpty(userIds)) {
			return new ArrayList<>();
		}
		StringBuffer sb = new StringBuffer("select o.id as userId,o.avatar,o.mobile,u.nick_name,o.create_time,u.we_chat_number " +
				"from ls_user_detail u right join ls_ordinary_user o on u.user_id =o.id where o.id in (");
		for (Long userId : userIds) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return query(sb.toString(), UserInformationDTO.class, userIds.toArray());
	}

	@Override
	@CacheEvict(value = "UserDetail", key = "#userId")
	public void evictUserCache(Long userId) {

	}

	@Override
	public List<UserDetail> queryByNotAddress() {
		LambdaEntityCriterion<UserDetail> criterion = new LambdaEntityCriterion<>(UserDetail.class);
		criterion.isNull(UserDetail::getProvinceId);
		return queryByProperties(criterion);
	}

	@Override
	public Integer updateMobileByUserId(Long userId, String mobile) {
		return update("update ls_ordinary_user set mobile = ? where id = ?", mobile, userId);
	}

}
