/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.admin;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dto.MenuTree;
import com.legendshop.user.dto.ShopMenuDTO;
import com.legendshop.user.dto.TreeSelectMenuDTO;
import com.legendshop.user.service.ShopMenuService;
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
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Tag(name = "商家菜单管理")
@RequiredArgsConstructor
@RequestMapping(value = "/admin/shop/menu", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AdminShopMenuController {

	final ShopMenuService shopMenuService;

	@GetMapping(value = "/all")
	@PreAuthorize("@pms.hasPermission('admin_menu_shopMenuManage_all')")
	@Operation(summary = "【后台】获取所有商家菜单权限")
	public R<List<ShopMenuDTO>> queryAll() {
		return shopMenuService.queryAll();
	}

	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin_menu_shopMenuManage_save')")
	@Operation(summary = "【后台】添加商家菜单权限")
	public R<Long> save(@Valid @RequestBody ShopMenuDTO menu, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			R.fail(bindingResult.getAllErrors().get(0).toString());
		}
		return this.shopMenuService.save(menu);
	}

	@GetMapping(value = "/{menuId}")
	@PreAuthorize("@pms.hasPermission('admin_menu_shopMenuManage_get')")
	@Operation(summary = "【后台】根据菜单Id获取菜单权限")
	@Parameter(name = "menuId", description = "菜单Id")
	public R<ShopMenuDTO> getById(@PathVariable Long menuId) {
		return this.shopMenuService.getById(menuId);
	}

	/**
	 * 返回角色的菜单集合
	 */
	@GetMapping("/tree/{roleId}")
	@PreAuthorize("@pms.hasPermission('admin_menu_shopMenuManage_roleTree')")
	@Operation(summary = "【后台】根据角色Id获取菜单权限")
	public R<List<Long>> getRoleTree(@PathVariable Long roleId) {
		return R.ok(shopMenuService.getByRoleId(roleId, UserTypeEnum.SHOP.name()).stream().map(MenuBO::getId).collect(Collectors.toList()));
	}

	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_menu_shopMenuManage_update')")
	@Operation(summary = "【后台】更新商家菜单权限")
	public R<Void> update(@RequestBody ShopMenuDTO menu) {
		return this.shopMenuService.update(menu);
	}

	@DeleteMapping(value = "/{menuId}")
	@PreAuthorize("@pms.hasPermission('admin_menu_shopMenuManage_del')")
	@Operation(summary = "【后台】根据菜单Id删除商家菜单权限")
	@Parameter(name = "menuId", description = "菜单Id")
	public R<Void> del(@PathVariable Long menuId) {
		return this.shopMenuService.del(menuId);
	}

	/**
	 * 返回树形菜单集合
	 *
	 * @param lazy     是否是懒加载
	 * @param parentId 父节点ID
	 * @return 树形菜单
	 */
	@GetMapping(value = "/tree")
	@PreAuthorize("@pms.hasPermission('admin_menu_shopMenuManage_tree')")
	@Operation(summary = "【后台】返回商家菜单树形菜单集合")
	public R<List<MenuTree>> getTree(Boolean lazy, Long parentId) {
		if (null == lazy) {
			lazy = false;
		}
		return R.ok(this.shopMenuService.treeMenu(lazy, parentId));
	}

	@PreAuthorize("@pms.hasPermission('admin_shop_menu_tree_select_list')")
	@Operation(summary = "【后台】获取商家菜单下拉树结构集合")
	@GetMapping(value = "/tree/select/list")
	public R<List<TreeSelectMenuDTO>> treeSelectShopMenuList() {
		List<TreeSelectMenuDTO> treeSelectMenuDTOS = shopMenuService.queryShopMenuTreeSelectList();
		return R.ok(treeSelectMenuDTOS);
	}

}
