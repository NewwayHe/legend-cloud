/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;

import com.legendshop.user.bo.UserSecureBO;
import com.legendshop.user.dto.MyPersonInfoDTO;

/**
 * 个人中心的用户等级
 *
 * @author legendshop
 */
public interface UserCenterPersonInfoDao {

	/**
	 * 根据用户ID获取个人信息。
	 *
	 * @param userId 用户ID
	 * @return 个人信息DTO
	 */
	MyPersonInfoDTO getPersonInfo(Long userId);

	/**
	 * 更新个人信息。
	 *
	 * @param myPersonInfo 个人信息DTO
	 */
	void updatePersonInfo(final MyPersonInfoDTO myPersonInfo);

	/**
	 * 根据用户ID更新昵称。
	 *
	 * @param userId   用户ID - User ID
	 * @param nickName 昵称 - Nickname
	 * @return 更新操作结果：成功（1），失败（0）
	 */
	int updateNickNameByUserId(Long userId, String nickName);

	/**
	 * 根据用户ID更新性别。
	 *
	 * @param userId 用户ID
	 * @param sex    性别
	 * @return 更新操作结果：成功（1），失败（0）
	 */
	int updateSexByUserId(Long userId, String sex);

	/**
	 * 根据用户ID获取用户安全信息。
	 *
	 * @param userId 用户ID
	 * @return 用户安全信息BO
	 */
	UserSecureBO getUserSecureByUserId(Long userId);
}
