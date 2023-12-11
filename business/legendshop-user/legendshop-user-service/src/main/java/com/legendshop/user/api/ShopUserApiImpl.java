/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.ShopUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.service.ShopUserService;
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
public class ShopUserApiImpl implements ShopUserApi {

	private final ShopUserService shopUserService;

	@Override
	public R<UserInfo> getUserInfo(String username) {
		return this.shopUserService.getUserInfo(username);
	}

	@Override
	public R<Void> updateAvatar(Long userId, String avatar) {
		return this.shopUserService.updateAvatar(userId, avatar);
	}

	@Override
	public R<ShopUserDTO> getShopUserInfo(Long shopUserId) {
		return R.ok(this.shopUserService.getById(shopUserId));
	}

	@Override
	public R<List<ShopUserDTO>> getByIds(List<Long> ids) {
		return R.ok(this.shopUserService.getByIds(ids));
	}

	@Override
	public R<ShopUserDTO> getByShopId(Long shopUserId) {
		return R.ok(this.shopUserService.getById(shopUserId));
	}

	@Override
	public R<List<ShopUserDTO>> queryAllShop() {
		return R.ok(this.shopUserService.queryAll());
	}

	@Override
	public R updateStatusByUserName(String username, Boolean status) {
		return this.shopUserService.updateStatusByUserName(username, status);
	}
}
