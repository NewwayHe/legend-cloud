/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.bo.ShopRoleBO;
import com.legendshop.user.dto.ShopRoleDTO;
import com.legendshop.user.dto.ShopRoleMenuDTO;
import com.legendshop.user.dto.UpdateShopRoleDTO;
import com.legendshop.user.query.ShopRoleQuery;
import com.legendshop.user.service.ShopRoleMenuService;
import com.legendshop.user.service.ShopRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台角色控制器
 *
 * @author legendshop
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin/shop/role", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "商家角色管理")
public class AdminShopRoleController {

	private final ShopRoleService shopRoleService;

	private final ShopRoleMenuService shopRoleMenuService;

	/**
	 * 分页查询角色信息
	 *
	 * @param roleQuery 角色查询封装对象
	 * @return pageList
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin_role_shopRoleManage_page')")
	@Operation(summary = "【后台】分页查询角色信息")
	public R<PageSupport<ShopRoleDTO>> getRolePage(ShopRoleQuery roleQuery) {

		PageSupport<ShopRoleDTO> rolePage = shopRoleService.getShopRolePage(roleQuery);
		return R.ok(rolePage);
	}

	@GetMapping("/list")
	@PreAuthorize("@pms.hasPermission('admin_role_shopRoleManage_list')")
	@Operation(summary = "【后台】查询角色列表")
	public R<List<ShopRoleDTO>> getRoleList(ShopRoleQuery roleQuery) {
		return R.ok(shopRoleService.queryAll(roleQuery));
	}

	/**
	 * 保存角色菜单
	 *
	 * @param roleMenuDTO 角色对象
	 * @return success、false
	 */
	@PostMapping("/menu")
	@PreAuthorize("@pms.hasPermission('admin_role_shopRoleManage_saveMenu')")
	@Operation(summary = "【后台】保存商家角色菜单权限")
	public R<Boolean> saveRoleMenus(@RequestBody ShopRoleMenuDTO roleMenuDTO) {
		ShopRoleDTO roleDTO = shopRoleService.getShopRole(roleMenuDTO.getRoleId());
		return R.ok(shopRoleMenuService.saveRoleMenus(roleDTO.getRoleCode(), roleDTO.getId(), roleMenuDTO.getMenuIds()));
	}

	/**
	 * 保存角色
	 *
	 * @param roleDTO 角色对象
	 * @return success、false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin_role_shopRoleManage_save')")
	@Operation(summary = "【后台】保存商家角色")
	public R<Long> save(@Valid @RequestBody ShopRoleDTO roleDTO) {
		return shopRoleService.saveShopRole(roleDTO);
	}

	/**
	 * 修改角色
	 *
	 * @param roleDTO 角色对象
	 * @return success、false
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_role_shopRoleManage_update')")
	@Operation(summary = "【后台】修改商家角色")
	public R<Void> update(@Valid @RequestBody ShopRoleDTO roleDTO) {
		return this.shopRoleService.updateShopRole(roleDTO);
	}


	/**
	 * 根据id删除角色
	 *
	 * @param id the id
	 * @return success、false
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin_role_shopRoleManage_del')")
	@Operation(summary = "【后台】根据id删除商家角色")
	public R<Void> deleteById(@PathVariable Long id) {
		return this.shopRoleService.deleteShopRole(id);
	}


	@PostMapping("/remove/by/userId")
	@PreAuthorize("@pms.hasPermission('admin_shop_role_remove_by_userId')")
	@Operation(summary = "【后台】根据用户ID和角色ID移除商家用户角色")
	@Parameters({
			@Parameter(name = "userId", description = "用户Id", required = true),
			@Parameter(name = "roleId", description = "角色Id", required = true)
	})
	public R<Void> removeByUserId(@RequestParam Long userId, @RequestParam Long roleId) {
		return shopRoleService.removeByUserId(userId, roleId);
	}


	@GetMapping("/get/role/page/by/userId")
	@PreAuthorize("@pms.hasPermission('admin_shop_role_get_role_page_by_userId')")
	@Operation(summary = "【后台】获取角色选择弹窗数据")
	public R<PageSupport<ShopRoleBO>> getRolePageByUserId(ShopRoleQuery shopRoleQuery) {
		return shopRoleService.getRolePageByUserId(shopRoleQuery);
	}


	@PostMapping("/update/role/for/user")
	@PreAuthorize("@pms.hasPermission('admin_shop_role_update_role_for_user')")
	@Operation(summary = "【后台】指定用户更新关联角色")
	public R<Void> updateRoleForUser(@RequestBody UpdateShopRoleDTO updateShopRoleDTO) {
		return shopRoleService.updateRoleForUser(updateShopRoleDTO.getUserId(), updateShopRoleDTO.getRoleIds());
	}

}
