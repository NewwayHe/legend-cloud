/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import com.legendshop.user.dto.BaseUserQuery;
import com.legendshop.user.dto.SysUserDTO;

/**
 * @author legendshop
 */
public interface BaseUserService {
	/**
	 * 根据用户类型和用户Id获取用户手机号
	 * （当前仅实现 User、Shop、ShopSubUser）
	 */
	String getMobile(BaseUserQuery query);

	/**
	 * 根据用户类型和用户手机获取用户信息（userId,username,delFlag,lockFlag）
	 * （当前仅实现 User、Shop、ShopSubUser）
	 */
	SysUserDTO getByMobile(BaseUserQuery query);

	/**
	 * 根据用户类型和用户手机判断用户是否存在
	 * （当前仅实现 User、Shop、ShopSubUser）
	 * true = 被占用
	 * false = 未使用
	 */
	boolean isMobileExist(BaseUserQuery query);
}
