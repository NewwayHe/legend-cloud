/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.shop;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.ShopUserDTO;
import com.legendshop.user.dto.VerifyShopUserDTO;
import com.legendshop.user.service.ShopUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 商家用户注册控制器
 *
 * @author legendshop
 */
@Tag(name = "商家用户注册")
@RestController
@RequestMapping(value = "/shop/register", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ShopUserRegisterController {

	private final ShopUserService shopUserService;


	/**
	 * 商家用户注册
	 *
	 * @param shopUser
	 * @return
	 */
	@PostMapping
	@Operation(summary = "【商家】商家用户注册")
	public R register(@Valid @RequestBody VerifyShopUserDTO shopUser) {
		return this.shopUserService.save(shopUser);
	}

	/**
	 * 判断手机号是否存在
	 *
	 * @param mobile the mobile
	 * @return ok:不存在  fail:存在
	 */
	@Operation(summary = "【商家】判断手机号是否存在")
	@Parameter(name = "mobile", description = "手机号", required = true)
	@GetMapping("/isMobileExist")
	public R isMobileExist(@RequestParam String mobile) {
		boolean result = shopUserService.isMobileExist(mobile.trim());
		if (result) {
			return R.fail("该手机号已被注册，不能重复注册");
		}
		return R.ok();
	}

	/**
	 * 重置密码
	 */
	@Operation(summary = "【商家】重置密码")
	@PutMapping("/reset/password")
	public R resetPassword(@Valid @RequestBody VerifyShopUserDTO shopUser) {
		ShopUserDTO shopUserDTO = shopUserService.getByMobile(shopUser.getMobile());
		if (ObjectUtil.isEmpty(shopUserDTO)) {
			return R.fail("该手机号并未注册账号");
		}
		return R.process(this.shopUserService.updatePassword(shopUserDTO, shopUser.getPassword()), "密码重置失败");
	}

}
