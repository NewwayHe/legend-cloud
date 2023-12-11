/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import com.legendshop.user.bo.UserSecureBO;
import com.legendshop.user.dto.MyPersonInfoDTO;

/**
 * 用户中心服务
 *
 * @author legendshop
 */
public interface UserCenterPersonInfoService {

	/**
	 * Gets the user center person info.
	 *
	 * @param userId the user id
	 * @return the user center person info
	 */
	MyPersonInfoDTO getUserCenterPersonInfo(Long userId);

	/**
	 * Update user info.
	 *
	 * @param myPersonInfo the my person info
	 */
	void updateUserInfo(MyPersonInfoDTO myPersonInfo);


	/**
	 * 修改昵称
	 *
	 * @param userId
	 * @param nickName
	 * @return
	 */
	int updateNickNameByUserId(Long userId, String nickName);

	/**
	 * 修改性别
	 *
	 * @param userId
	 * @param sex
	 * @return
	 */
	int updateSexByUserId(Long userId, String sex);


	/**
	 * 获取账户安全信息
	 *
	 * @param userId
	 * @return
	 */
	UserSecureBO getUserSecureByUserId(Long userId);
}
