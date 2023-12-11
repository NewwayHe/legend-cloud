/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.user.dao.ShopUserDao;
import com.legendshop.user.dto.ShopUserDTO;
import com.legendshop.user.entity.ShopUser;
import com.legendshop.user.query.ShopUserQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商城用户Dao.
 *
 * @author legendshop
 */
@Repository
public class ShopUserDaoImpl extends GenericDaoImpl<ShopUser, Long> implements ShopUserDao {

	@Override
	public List<ShopUser> getShopUseByShopId(Long shopId) {
		return this.queryByProperties(new EntityCriterion().eq("shopId", shopId));
	}

	@Override
	public ShopUser getShopUser(Long shopId, String name) {
		return this.getByProperties(new EntityCriterion().eq("shopId", shopId).eq("name", name).eq("enabled", "1"));
	}

	@Override
	public List<String> queryPerm(Long shopUserId) {
		return query("SELECT p.label FROM ls_shop_user u, ls_shop_perm p WHERE u.shop_role_id = p.role_id and u.id = ?", String.class, shopUserId);
	}

	@Override
	public List<ShopUser> getShopUseRoleId(Long shopRoleId) {
		return this.queryByProperties(new EntityCriterion().eq("shopRoleId", shopRoleId));
	}

	@Override
	public PageSupport<ShopUser> queryShopUser(String curPageNO, Long shopId) {
		SimpleSqlQuery query = new SimpleSqlQuery(ShopUser.class, 20, curPageNO);
		QueryMap map = new QueryMap();
		map.put("shopId", shopId);
		query.setSqlAndParameter("ShopUser.queryShopUser", map);
		return querySimplePage(query);
	}

	@Override
	public PageSupport<ShopUser> page(ShopUserQuery shopUserQuery) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(ShopUser.class, shopUserQuery.getPageSize(), shopUserQuery.getCurPage());
		if (StringUtils.isNotBlank(shopUserQuery.getUsername())) {
			criteriaQuery.like("username", shopUserQuery.getUsername(), MatchMode.ANYWHERE);
		}
		if (StringUtils.isNotBlank(shopUserQuery.getMobilePhone())) {
			criteriaQuery.like("mobile", shopUserQuery.getMobilePhone(), MatchMode.ANYWHERE);
		}
		if (null != shopUserQuery.getLockFlag()) {
			criteriaQuery.eq("lockFlag", shopUserQuery.getLockFlag());
		}
		if (StringUtils.isNotBlank(shopUserQuery.getUserId())) {
			criteriaQuery.like("id", shopUserQuery.getUserId(), MatchMode.ANYWHERE);
		}
		return queryPage(criteriaQuery);
	}

	@Override
	public ShopUser getByUsername(String account) {
		String sql = "SELECT su.*,sd.id AS shopId FROM" +
				" ls_shop_user su LEFT JOIN ls_shop_detail sd ON su.id = sd.shop_user_id " +
				" WHERE username = ? OR mobile = ?";
		return this.get(sql, ShopUser.class, account, account);
	}


	@Override
	public boolean isMobileExist(String mobile) {
		return ObjectUtil.isNotEmpty(get("SELECT 1  FROM ls_shop_user WHERE mobile=?", Integer.class, mobile));
	}

	@Override
	public ShopUser getByMobile(String mobile) {
		return this.getByProperties(new EntityCriterion().eq("mobile", mobile).eq("delFlag", Boolean.TRUE));
	}

	@Override
	public String getMobileByUserId(Long userId) {
		return this.get("SELECT mobile FROM ls_shop_user WHERE id = ?", String.class, userId);
	}

	@Override
	public boolean updateMobileByUserId(Long id, String mobile) {
		return this.update("update ls_shop_user set mobile = ? where id = ?", mobile, id) > 0;
	}

	@Override
	public boolean updateLoginPassword(ShopUserDTO shopUserDTO) {
		Long id = shopUserDTO.getId();
		String password = shopUserDTO.getPassword();
		if (null == id) {
			return false;
		}
		if (StringUtils.isBlank(password)) {
			return false;
		}
		return this.update("UPDATE ls_shop_user SET password = ? , update_time = NOW() WHERE id = ? ", password, id) > 0;
	}

	@Override
	public boolean updateStatus(Long id) {
		return super.update("UPDATE ls_shop_user SET lock_flag = !lock_flag WHERE id = ?", id) > 0;
	}

	@Override
	public boolean batchUpdateStatus(BasicBatchUpdateStatusDTO basicBatchUpdateStatusDTO) {
		List<Long> args = new ArrayList<>();
		args.add(Long.valueOf(basicBatchUpdateStatusDTO.getStatus()));
		StringBuffer sb = new StringBuffer("update ls_shop_user u set u.lock_flag=? where u.id in(");
		for (Long id : basicBatchUpdateStatusDTO.getIds()) {
			args.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return update(sb.toString(), args.toArray()) > 0;
	}

	@Override
	public boolean updateAvatar(Long userId, String avatar) {
		return this.update("update ls_shop_user set avatar = ? where id = ?", avatar, userId) > 0;
	}

	@Override
	public boolean isUserExist(String userName) {
		return get("SELECT 1  FROM ls_shop_user WHERE username=?", Integer.class, userName) > 0;
	}

	@Override
	public Integer getNewShopData(Date startDate, Date endDate) {

		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("ShopCount.queryNewShop", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));

	}

	@Override
	public ShopUser getByShopId(Long shopId) {
		return getByProperties(new EntityCriterion().eq("shopId", shopId));
	}

	@Override
	public int updateStatusByUserName(String username, Boolean status) {
		return super.update("UPDATE ls_shop_user SET lock_flag = ? WHERE username = ? ", status, username);
	}

}
