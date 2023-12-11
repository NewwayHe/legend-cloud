/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class AdminUserApiImpl implements AdminUserApi {

	final AdminUserService adminUserService;

	@Override
	@SentinelResource()
	public R<UserInfo> getUserInfo(String username) {
		return this.adminUserService.getUserInfo(username);
	}

	@Override
	@SentinelResource()
	public R<List<Long>> queryUsersByMenuId(int menuId) {
		return this.adminUserService.queryUserByMenuId(new Long(menuId));
	}

	@Override
	public R<List<Long>> queryUserIdsByMenuName(String menuName) {
		return R.ok(adminUserService.queryUserIdsByMenuName(menuName));
	}

	@Override
	public R<List<Long>> getAdamin(Long productId) {
		return R.ok(adminUserService.getAdmin());
	}

	@Override
	@SentinelResource()
	public R updateStatusByUserName(String username, Boolean status) {
		return this.adminUserService.updateStatusByUserName(username, status);
	}
}
