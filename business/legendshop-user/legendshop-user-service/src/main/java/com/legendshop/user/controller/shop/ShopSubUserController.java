/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.dto.ShopUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.dto.ShopSubUserDTO;
import com.legendshop.user.query.ShopSubUserQuery;
import com.legendshop.user.service.ShopSubUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "商家子账号管理")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/s/shop/subUser", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopSubUserController {

	final ShopSubUserService shopSubUserService;

	@PostMapping
	@PreAuthorize("@pms.hasPermission('s_shop_subUser_save')")
	@Operation(summary = "【商家】创建子账号")
	public R<Long> save(@Valid @RequestBody ShopSubUserDTO user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return R.fail(bindingResult.getFieldError().getDefaultMessage());
		}
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		if (null == shopUser) {
			return R.fail("请重新登录！");
		}
		user.setShopUserId(shopUser.getUserId());
		user.setShopId(shopUser.getShopId());
		return this.shopSubUserService.saveShopSubUser(user);
	}

	@PutMapping
	@PreAuthorize("@pms.hasPermission('s_shop_subUser_update')")
	@Operation(summary = "【商家】更新子账号")
	public R<Void> update(@RequestBody ShopSubUserDTO user) {
		return this.shopSubUserService.updateShopSubUser(user);
	}

	@PostMapping(value = "/updatePassword")
	@PreAuthorize("@pms.hasPermission('s_shop_subUser_updatePassword')")
	@Operation(summary = "【商家】更新子账号登录密码")
	@Parameters({
			@Parameter(name = "id", description = "员工ID"),
			@Parameter(name = "newPassword", description = "新密码")
	})
	public R<Void> updatePassword(@RequestParam Long id, @RequestParam String newPassword) {
		return this.shopSubUserService.updatePassword(id, newPassword);
	}


	@GetMapping(value = "/{id}")
	@PreAuthorize("@pms.hasPermission('s_shop_subUser_get')")
	@Operation(summary = "【商家】根据Id查询子账号")
	@Parameter(name = "id", description = "子账号Id")
	public R<ShopSubUserDTO> get(@PathVariable Long id) {
		return R.ok(this.shopSubUserService.getShopSubUser(id));
	}


	@GetMapping(value = "/queryUserPage")
	@PreAuthorize("@pms.hasPermission('s_shop_subUser_queryUserPage')")
	@Operation(summary = "【商家】根据条件查询子账号")
	public R<PageSupport<ShopSubUserDTO>> queryUserPage(ShopSubUserQuery query) {
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		if (null == shopUser) {
			return R.fail("请重新登录！");
		}
		query.setShopUserId(shopUser.getUserId());
		return R.ok(this.shopSubUserService.queryUserPage(query));
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("@pms.hasPermission('s_shop_subUser_del')")
	@Operation(summary = "【商家】根据Id删除子账号")
	@Parameter(name = "id", description = "子账号Id")
	public R<Void> del(@PathVariable Long id) {
		return R.process(this.shopSubUserService.delete(id) > 0, "删除失败,没有需要删除的记录");
	}

	@PutMapping(value = "/updateStatus/{id}")
	@PreAuthorize("@pms.hasPermission('s_shop_subUser_updateStatus')")
	@Operation(summary = "【商家】更新子账号在线状态")
	@Parameter(name = "id", description = "子账号Id")
	public R<Void> updateStatus(@PathVariable Long id) {
		return R.process(this.shopSubUserService.updateStatus(id) > 0, "更新失败,没有需要更新的记录");
	}

	@GetMapping(value = "/queryShopSubAccount")
	@Operation(summary = "【商家】查询商家下的全部子账号")
	public R<List<ShopSubUserDTO>> queryShopSubAccount() {
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		if (null == shopUser) {
			return R.fail("请重新登录！");
		}
		return R.ok(shopSubUserService.queryShopSubAccount(shopUser.getUserId()));
	}
}
