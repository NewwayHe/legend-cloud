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
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.dto.UserAddressDTO;
import com.legendshop.user.entity.UserAddress;
import com.legendshop.user.service.UserAddressService;
import com.legendshop.user.service.convert.UserAddressConverter;
import jakarta.servlet.http.HttpServletRequest;
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
public class UserAddressApiImpl implements UserAddressApi {


	private final UserAddressService userAddressService;
	private final UserAddressConverter userAddressConverter;

	private final HttpServletRequest request;

	@Override
	public R<UserAddressBO> getDefaultAddress(Long userId) {
		return R.ok(this.userAddressService.getDefaultAddress(userId));
	}

	@Override
	public R<UserAddressDTO> getById(Long id) {
		return R.ok(this.userAddressService.getById(id));
	}

	@Override
	public R<UserAddressBO> getAddressInfo(Long id) {
		return R.ok(this.userAddressService.getAddressInfo(id));
	}

	@Override
	public R<UserAddressBO> getUserAddressForOrder(Long userId, Long addressId) {
		return R.ok(this.userAddressService.getUserAddressForOrder(userId, addressId));
	}

	@Override
	public R<UserAddressBO> getCommonAddress(Long userId) {
		return R.ok(this.userAddressService.getCommonAddress(userId, request));
	}

	@Override
	public R<List<UserAddressDTO>> queryByUserId(Long userId) {

		List<UserAddress> userAddresses = userAddressService.queryByUserId(userId);
		return R.ok(userAddressConverter.to(userAddresses));
	}


}
