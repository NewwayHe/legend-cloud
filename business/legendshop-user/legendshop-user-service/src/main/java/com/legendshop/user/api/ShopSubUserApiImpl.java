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
import com.legendshop.user.dto.ShopSubUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.service.ShopSubUserService;
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
public class ShopSubUserApiImpl implements ShopSubUserApi {

	final ShopSubUserService shopSubUserService;

	@Override
	public R<ShopSubUserDTO> getById(Long id) {
		return R.ok(this.shopSubUserService.getShopSubUser(id));
	}

	@Override
	public R<UserInfo> getUserInfo(String username) {
		return this.shopSubUserService.getUserInfo(username);
	}

	@Override
	public R<List<ShopSubUserDTO>> queryAllShopSubUser() {
		return R.ok(this.shopSubUserService.queryAllShopSubUser());
	}

	@Override
	public R updateStatusByUserName(String username, Boolean status) {
		return this.shopSubUserService.updateStatusByUserName(username, status);
	}
}
