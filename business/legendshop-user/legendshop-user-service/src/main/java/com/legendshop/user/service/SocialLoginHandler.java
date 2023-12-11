/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import com.legendshop.user.dto.UserInfo;

/**
 * 社交登录登录处理器
 *
 * @author legendshop
 */
public interface SocialLoginHandler {

	/**
	 * 处理登录方法
	 *
	 * @param loginParams 登录参数
	 * @return userInfo
	 */
	R<UserInfo> handle(BasisLoginParamsDTO loginParams);


	/**
	 * 更新登录用户的OPENID
	 *
	 * @param loginParams
	 * @return
	 */
	R<Boolean> updateUserOpenId(BasisLoginParamsDTO loginParams);

	/**
	 * 认证URL
	 *
	 * @return
	 */
	String authUrl();
}
