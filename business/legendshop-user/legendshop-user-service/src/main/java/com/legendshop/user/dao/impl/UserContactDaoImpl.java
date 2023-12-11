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
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.lambda.LambdaCriteriaQuery;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.user.dao.UserContactDao;
import com.legendshop.user.dto.UserContactDTO;
import com.legendshop.user.entity.UserContact;
import com.legendshop.user.query.UserContactQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户提货信息Dao
 *
 * @author legendshop
 */
@Repository
public class UserContactDaoImpl extends GenericDaoImpl<UserContact, Long> implements UserContactDao {


	@Override
	public int updateDefaultUserContact(Long contactId, Long userId) {
		//1.更新原来的收货地址
		update("update ls_user_contact set default_flag = 0 where default_flag = 1 and user_id = ?", userId);

		//2.更新新的收货地址
		return update("update ls_user_contact set default_flag = 1 where id = ?  and user_id = ? ", contactId, userId);
	}

	/**
	 * 取用户的默认地址
	 */
	@Override
	public UserContact getDefaultUserContact(Long userId) {

		LambdaEntityCriterion<UserContact> entityCriterion = new LambdaEntityCriterion<>(UserContact.class);
		entityCriterion.eq(UserContact::getUserId, userId);
		entityCriterion.eq(UserContact::getDefaultFlag, Boolean.TRUE);
		return getByProperties(entityCriterion);

	}

	@Override
	public UserContact getUserContactInfo(Long id) {
		LambdaEntityCriterion<UserContact> entityCriterion = new LambdaEntityCriterion<>(UserContact.class);
		entityCriterion.eq(UserContact::getId, id);
		return getByProperties(entityCriterion);
	}

	@Override
	public List<UserContact> getUserContactForOrder(Long userId) {

		LambdaEntityCriterion<UserContact> criterion = new LambdaEntityCriterion<>(UserContact.class);
		criterion.eq(UserContact::getUserId, userId);
		criterion.addDescOrder(UserContact::getDefaultFlag);
		return queryByProperties(criterion);
	}

	@Override
	public List<UserContact> queryByUserId(Long userId) {
		LambdaEntityCriterion<UserContact> criterion = new LambdaEntityCriterion<>(UserContact.class);
		criterion.eq(UserContact::getUserId, userId);
		criterion.addDescOrder(UserContact::getDefaultFlag);
		return queryByProperties(criterion);
	}

	@Override
	public long getUserContactCount(Long userId) {
		return getLongResult("select count(*) from ls_user_contact where user_id = ?", userId);
	}


	@Override
	public PageSupport<UserContactDTO> queryPage(UserContactQuery query) {

		LambdaCriteriaQuery<UserContactDTO> criteriaQuery = new LambdaCriteriaQuery<>(UserContactDTO.class, query);
		criteriaQuery.eq(UserContactDTO::getUserId, query.getUserId());

		criteriaQuery.addDescOrder(UserContactDTO::getDefaultFlag);
		criteriaQuery.addDescOrder(UserContactDTO::getUpdateTime);
		return queryDTOPage(criteriaQuery);
	}

	@Override
	public int updateOtherDefault(Long contactId, Long userId, String defaultFlag) {
		String sql = "update ls_user_contact set default_flag = ? where id <> ? AND user_id = ? AND default_flag = 1  ";
		return this.update(sql, defaultFlag, contactId, userId);
	}

}
