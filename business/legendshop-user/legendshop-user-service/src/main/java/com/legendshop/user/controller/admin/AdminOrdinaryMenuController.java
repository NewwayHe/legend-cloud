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
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dto.MenuTree;
import com.legendshop.user.dto.OrdinaryMenuDTO;
import com.legendshop.user.service.OrdinaryMenuService;
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
@Tag(name = "普通用户菜单管理")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/ordinary/menu", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminOrdinaryMenuController {

	final OrdinaryMenuService ordinaryMenuService;

	@GetMapping(value = "/all")
	@PreAuthorize("@pms.hasPermission('admin_menu_ordinaryMenuManage_all')")
	@Operation(summary = "【后台】获取所有普通用户菜单权限")
	public R<List<OrdinaryMenuDTO>> queryAll() {
		return this.ordinaryMenuService.queryAll();
	}

	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin_menu_ordinaryMenuManage_save')")
	@Operation(summary = "【后台】添加普通用户菜单权限")
	public R<Long> save(@Valid @RequestBody OrdinaryMenuDTO menu, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			R.fail(bindingResult.getFieldError().getDefaultMessage());
		}
		return this.ordinaryMenuService.save(menu);
	}

	@GetMapping(value = "/{menuId}")
	@PreAuthorize("@pms.hasPermission('admin_menu_ordinaryMenuManage_get')")
	@Operation(summary = "【后台】根据菜单Id获取菜单权限")
	@Parameter(name = "menuId", description = "菜单Id", required = true)
	public R<OrdinaryMenuDTO> getById(@PathVariable Long menuId) {
		return this.ordinaryMenuService.getById(menuId);
	}

	/**
	 * 返回角色的菜单集合
	 */
	@GetMapping("/tree/{roleId}")
	@PreAuthorize("@pms.hasPermission('admin_menu_ordinaryMenuManage_roloTree')")
	@Operation(summary = "【后台】根据角色Id获取菜单权限")
	public R<List<Long>> getRoleTree(@PathVariable Long roleId) {
		return R.ok(ordinaryMenuService.getByRoleId(roleId).stream().map(MenuBO::getId).collect(Collectors.toList()));
	}

	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_menu_ordinaryMenuManage_update')")
	@Operation(summary = "【后台】更新普通用户菜单权限")
	public R<Void> update(@RequestBody OrdinaryMenuDTO menu) {
		return this.ordinaryMenuService.update(menu);
	}

	@DeleteMapping(value = "/{menuId}")
	@PreAuthorize("@pms.hasPermission('admin_menu_ordinaryMenuManage_del')")
	@Operation(summary = "【后台】根据菜单Id删除普通用户菜单权限")
	@Parameter(name = "menuId", description = "菜单Id")
	public R<Void> del(@PathVariable Long menuId) {
		return this.ordinaryMenuService.del(menuId);
	}

	/**
	 * 返回树形菜单集合
	 *
	 * @param lazy     是否是懒加载
	 * @param parentId 父节点ID
	 * @return 树形菜单
	 */
	@GetMapping(value = "/tree")
	@PreAuthorize("@pms.hasPermission('admin_menu_ordinaryMenuManage_tree')")
	@Operation(summary = "【后台】返回普通用户菜单树形菜单集合")
	public R<List<MenuTree>> getTree(Boolean lazy, Long parentId) {
		if (null == lazy) {
			lazy = false;
		}
		return R.ok(this.ordinaryMenuService.treeMenu(lazy, parentId));
	}

}
