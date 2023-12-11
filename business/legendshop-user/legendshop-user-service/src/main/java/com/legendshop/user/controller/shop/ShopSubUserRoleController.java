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
import com.legendshop.user.dto.ShopSubRoleDTO;
import com.legendshop.user.query.ShopRoleQuery;
import com.legendshop.user.service.ShopSubRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商家子账号角色管理
 *
 * @author legendshop
 */
@Tag(name = "商家子账号角色管理")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/s/shop/userRole", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopSubUserRoleController {

	final ShopSubRoleService shopSubRoleService;

	@PostMapping
	@PreAuthorize("@pms.hasPermission('s_shop_userRole_save')")
	@Operation(summary = "【商家】创建子账号角色")
	public R<Long> save(@Valid @RequestBody ShopSubRoleDTO role, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return R.fail(bindingResult.getFieldError().getDefaultMessage());
		}
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		if (null == shopUser) {
			return R.fail("请重新登录！");
		}
		role.setShopId(shopUser.getShopId());
		return this.shopSubRoleService.saveShopSubRole(role);
	}

	@PutMapping
	@PreAuthorize("@pms.hasPermission('s_shop_userRole_update')")
	@Operation(summary = "【商家】更新子账号角色")
	public R<Void> update(@RequestBody ShopSubRoleDTO role) {
		return this.shopSubRoleService.updateShopSubRole(role);
	}

	@GetMapping(value = "/{roleId}")
	@PreAuthorize("@pms.hasPermission('s_shop_userRole_get')")
	@Operation(summary = "【商家】根据角色ID获取子账号角色")
	@Parameter(name = "roleId", description = "角色Id")
	public R<ShopSubRoleDTO> get(@PathVariable Long roleId) {
		return R.ok(this.shopSubRoleService.getShopSubRole(roleId));
	}

	@GetMapping(value = "/queryRolePage")
	@PreAuthorize("@pms.hasPermission('s_shop_userRole_queryRolePage')")
	@Operation(summary = "【商家】根据条件查询子账号角色")
	public R<PageSupport<ShopSubRoleDTO>> queryRolePage(ShopRoleQuery query) {
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		if (null == shopUser) {
			return R.fail("请重新登录！");
		}
		query.setUserId(shopUser.getShopId());
		return R.ok(this.shopSubRoleService.getShopSubRolePage(query));
	}


	@GetMapping(value = "/queryAll")
	@PreAuthorize("@pms.hasPermission('s_shop_userRole_queryAll')")
	@Operation(summary = "【商家】查询子账号角色")
	public R<List<ShopSubRoleDTO>> queryAll(ShopRoleQuery query) {
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		if (null == shopUser) {
			return R.fail("请重新登录！");
		}
		query.setUserId(shopUser.getShopId());
		return R.ok(this.shopSubRoleService.queryAll(query));
	}


	@DeleteMapping(value = "/{roleId}")
	@PreAuthorize("@pms.hasPermission('s_shop_userRole_del')")
	@Operation(summary = "【商家】根据角色ID删除子账号角色")
	@Parameter(name = "roleId", description = "角色Id")
	public R<Void> del(@PathVariable Long roleId) {
		return this.shopSubRoleService.deleteShopSubRole(roleId);
	}
}
