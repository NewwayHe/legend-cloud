/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.dto.UserContactDTO;
import com.legendshop.user.entity.UserContact;
import com.legendshop.user.query.UserContactQuery;

import java.util.List;

/**
 * 用户提货信息Dao
 *
 * @author legendshop
 */
public interface UserContactDao extends GenericDao<UserContact, Long> {


	/**
	 * 更新用户的默认提货信息。
	 *
	 * @param contactId 提货信息ID
	 * @param userId    用户ID
	 * @return 操作结果
	 */
	int updateDefaultUserContact(Long contactId, Long userId);

	/**
	 * 获取用户的提货信息数量。
	 *
	 * @param userId 用户ID
	 * @return 提货信息数量
	 */
	long getUserContactCount(Long userId);

	/**
	 * 分页查询用户的提货信息。
	 *
	 * @param query 查询条件
	 * @return 分页的用户提货信息
	 */
	PageSupport<UserContactDTO> queryPage(UserContactQuery query);

	/**
	 * 更新其他默认提货信息的标志。
	 *
	 * @param contactId   当前提货信息ID
	 * @param userId      用户ID
	 * @param defaultFlag 默认标志 (0：非默认，1：默认)
	 * @return 操作结果
	 */
	int updateOtherDefault(Long contactId, Long userId, String defaultFlag);

	/**
	 * 查询用户默认提货地址
	 *
	 * @param userId
	 * @return
	 */
	UserContact getDefaultUserContact(Long userId);


	/**
	 * 根据ID查询用户提货信息
	 *
	 * @param id
	 * @return
	 */
	UserContact getUserContactInfo(Long id);

	/**
	 * 下单-获取用户最新的提货信息
	 *
	 * @param userId
	 * @return
	 */
	List<UserContact> getUserContactForOrder(Long userId);

	List<UserContact> queryByUserId(Long userId);
}
