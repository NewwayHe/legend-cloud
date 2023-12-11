/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.user.bo.UserContactBO;
import com.legendshop.user.dto.UserContactDTO;
import com.legendshop.user.entity.UserContact;
import com.legendshop.user.query.UserContactQuery;

import java.util.List;

/**
 * 用户地址服务.
 *
 * @author legendshop
 */
public interface UserContactService extends BaseService<UserContactDTO> {


	Long getUserContactCount(Long id);

	int updateDefaultUserContact(Long contactId, Long userId);


	PageSupport<UserContactDTO> queryPage(UserContactQuery query);

	/**
	 * 新增收货地址
	 *
	 * @param dto
	 * @return
	 */
	Long saveContact(UserContactDTO dto);

	/**
	 * 编辑收货地址
	 *
	 * @param dto
	 * @return
	 */
	int updateContact(UserContactDTO dto);

	/**
	 * 获取用户默认地址
	 *
	 * @param userId
	 * @return
	 */
	UserContactBO getDefaultUserContact(Long userId);

	/**
	 * 根据ID获取用户收货地址信息
	 *
	 * @param id
	 * @return
	 */
	UserContactBO getUserContactInfo(Long id);

	/**
	 * 获取提货信息给下单用
	 *
	 * @param userId    用户ID
	 * @param contactId 用户选择的提货信息ID
	 * @return
	 */
	UserContactBO getUserContactForOrder(Long userId, Long contactId);

	/**
	 * 删除用户收货地址
	 *
	 * @param userId
	 * @param id
	 * @return
	 */
	boolean deleteUserAddress(Long userId, Long id);


	List<UserContact> queryByUserId(Long userId);

	UserContactBO getBoById(Long id);
}
