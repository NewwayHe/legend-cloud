/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.user;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户ip地址接口
 *
 * @author legendshop
 */
@Tag(name = "用户收货地址")
@RestController
@RequestMapping(value = "/ip/address", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserIpAddressController {

	@Autowired
	private UserAddressService userAddressService;

	/**
	 * 获取用户的默认收货地址.
	 */
	@Operation(summary = "【用户】获取用户的默认收货地址")
	@GetMapping
	public R<UserAddressBO> getCommonAddress(HttpServletRequest request) {
		return R.ok(userAddressService.getCommonAddress(SecurityUtils.getUserId(), request));
	}
}
