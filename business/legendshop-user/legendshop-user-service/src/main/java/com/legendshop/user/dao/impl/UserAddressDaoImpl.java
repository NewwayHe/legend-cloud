/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;


import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.dao.UserAddressDao;
import com.legendshop.user.dto.UserAddressDTO;
import com.legendshop.user.entity.UserAddress;
import com.legendshop.user.query.UserAddressQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户收货地址Dao
 *
 * @author legendshop
 */
@Repository
public class UserAddressDaoImpl extends GenericDaoImpl<UserAddress, Long> implements UserAddressDao {

	@Override
	public List<UserAddress> getUserAddress(String userName) {
		return this.queryByProperties(new EntityCriterion().eq("userName", userName).addDescOrder("commonAddr").addDescOrder("createTime"));
	}

	@Override
	public Long getMaxNumber(String userName) {
		return getLongResult("select count(*) from ls_user_address where user_name = ?", userName);
	}

	@Override
	public int updateDefaultUserAddress(Long addrId, Long userId) {
		//1.更新原来的收货地址
		update("update ls_user_address set common_flag = 0 where common_flag = 1 and user_id = ?", userId);

		//2.更新新的收货地址
		return update("update ls_user_address set common_flag = 1 where id = ?  and user_id = ? ", addrId, userId);
	}

	/**
	 * 取用户的默认地址
	 */
	@Override
	public UserAddressBO getDefaultAddress(Long userId) {
		return get(getSQL("Address.getDefaultAddress"), UserAddressBO.class, userId);
	}

	@Override
	public UserAddressBO getAddressInfo(Long id) {
		return get(getSQL("Address.getAddressInfo"), UserAddressBO.class, id);
	}

	@Override
	public List<UserAddressBO> getUserAddressForOrder(Long userId) {
		return query(getSQL("Address.getUserAddressForOrder"), UserAddressBO.class, userId);
	}

	@Override
	public List<UserAddress> queryByUserId(Long userId) {
		LambdaEntityCriterion<UserAddress> criterion = new LambdaEntityCriterion<>(UserAddress.class);
		criterion.eq(UserAddress::getUserId, userId);
		criterion.addDescOrder(UserAddress::getCommonFlag);
		return queryByProperties(criterion);
	}

	@Override
	public long getUserAddressCount(Long userId) {
		return getLongResult("select count(*) from ls_user_address where user_id = ?", userId);
	}


	@Override
	public PageSupport<UserAddressDTO> queryPage(UserAddressQuery userAddressQuery) {
		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(UserAddressDTO.class, userAddressQuery.getPageSize(), userAddressQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("userId", userAddressQuery.getUserId());
		sqlQuery.setSqlAndParameter("Address.queryPage", map);
		return querySimplePage(sqlQuery);
	}

	@Override
	public int updateOtherDefault(Long curAddrId, Long userId, String commonAddr) {
		String sql = "update ls_user_address set common_flag = ? where id <> ? AND user_id = ? AND common_flag = 1  ";
		return this.update(sql, commonAddr, curAddrId, userId);
	}

}
