/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.shop;


import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.dto.ShopUserDTO;
import com.legendshop.user.dto.VerifyShopUserMobileDTO;
import com.legendshop.user.service.ShopUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * 商家用户管理控制器
 *
 * @author legendshop
 */
@Tag(name = "商家用户管理")
@RestController
@RequestMapping(value = "/s/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ShopUserController {

	private final ShopUserService shopUserService;

	/**
	 * 获取当前用户信息
	 *
	 * @return ShopUserDTO
	 */
	@GetMapping
	@Operation(summary = "【商家】获取当前登录商家")
	public R<ShopUserDTO> shopUser() {
		BaseUserDetail baseUser = SecurityUtils.getBaseUser();
		if (null == baseUser) {
			return R.fail("用户登录失效，请重新登录！");
		}
		Long userId = baseUser.getUserId();
		ShopUserDTO userDTO = this.shopUserService.getById(userId);
		// 增加用户类型，用于判断用户是否为子账号
		userDTO.setUserType(baseUser.getUserType());
		return R.ok(userDTO);
	}

	@Operation(summary = "【商家】修改绑定手机号")
	@PostMapping("/updateMobilePhone")
	@PreAuthorize("@pms.hasPermission('s_shop_updateMobilePhone')")
	public R updateMobilePhone(@Valid @RequestBody VerifyShopUserMobileDTO dto) {
		Long userId = SecurityUtils.getUserId();
		Assert.notNull(userId, "身份过期，请重新登录！");
		dto.setId(userId);
		String userType = SecurityUtils.getUserType();
		dto.setUserType(UserTypeEnum.codeValue(SecurityUtils.getUserType()));
		return this.shopUserService.updateMobilePhone(dto);
	}
}
