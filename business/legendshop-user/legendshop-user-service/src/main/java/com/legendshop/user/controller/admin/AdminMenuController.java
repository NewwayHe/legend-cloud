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
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dto.AdminMenuDTO;
import com.legendshop.user.dto.MenuTree;
import com.legendshop.user.dto.TreeSelectMenuDTO;
import com.legendshop.user.service.AdminMenuService;
import com.legendshop.user.utils.TreeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 后台菜单管理控制器
 * <p>
 * admin_user_menu_get_order_list
 *
 * @author legendshop
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin/menu", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "菜单管理")
public class AdminMenuController {
	private final AdminMenuService adminMenuService;

	/**
	 * 查询后台菜单管理
	 *
	 * @return
	 */
	@GetMapping
	@Operation(summary = "【后台】查询后台菜单管理")
	@Parameters({
			@Parameter(name = "type", description = "类型", required = true),
			@Parameter(name = "parentId", description = "父类Id", required = true)
	})
	public R<List<MenuTree>> getAdminMenu(String type, Long parentId) {
		// 获取符合条件的菜单
		Set<MenuBO> all = new HashSet<>();
		SecurityUtils.getRoles().forEach(roleId -> all.addAll(adminMenuService.getByRoleId(roleId)));
		return R.ok(TreeUtil.filterMenu(all, type, parentId));
	}


	/**
	 * 返回树形菜单集合
	 *
	 * @param lazy     是否是懒加载
	 * @param parentId 父节点ID
	 * @return 树形菜单
	 */
	@GetMapping(value = "/tree")
	@Operation(summary = "【后台】返回树形菜单集合")
	@Parameters({
			@Parameter(name = "parentId", description = "父类Id", required = true)
	})
	public R<List<MenuTree>> getTree(boolean lazy, Long parentId) {
		return R.ok(adminMenuService.treeMenu(lazy, parentId));
	}

	/**
	 * 返回角色的菜单集合
	 *
	 * @param roleId
	 * @return
	 */
	@GetMapping("/tree/{roleId}")
	@Operation(summary = "【后台】返回角色的菜单集合")
	@Parameters({
			@Parameter(name = "roleId", description = "角色Id", required = true)
	})
	public R getRoleTree(@PathVariable Long roleId) {
		return R.ok(adminMenuService.getByRoleId(roleId).stream().map(MenuBO::getId).collect(Collectors.toList()));
	}


	/**
	 * 保存菜单
	 *
	 * @param adminMenuDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("保存菜单")
	@PreAuthorize("@pms.hasPermission('admin_menu_adminMenuManage_save')")
	@Operation(summary = "【后台】保存菜单")
	public R save(@Valid @RequestBody AdminMenuDTO adminMenuDTO) {
		adminMenuService.save(adminMenuDTO);
		return R.ok(adminMenuDTO);
	}

	/**
	 * 修改菜单
	 *
	 * @param adminMenuDTO
	 * @return
	 */
	@PutMapping
	@SystemLog("修改菜单")
	@PreAuthorize("@pms.hasPermission('admin_menu_adminMenuManage_update')")
	@Operation(summary = "【后台】修改菜单")
	public R update(@Valid @RequestBody AdminMenuDTO adminMenuDTO) {
		return R.ok(adminMenuService.update(adminMenuDTO));
	}

	/**
	 * 根据id删除菜单
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@SystemLog("删除菜单")
	@PreAuthorize("@pms.hasPermission('admin_menu_adminMenuManage_del')")
	@Operation(summary = "【后台】删除菜单")
	@Parameters({
			@Parameter(name = "id", description = "菜单Id", required = true)
	})
	public R deleteById(@PathVariable Long id) {
		return adminMenuService.deleteById(id);
	}


	@PreAuthorize("@pms.hasPermission('admin_menu_tree_select_admin_menu_list')")
	@Operation(summary = "【后台】获取平台菜单下拉树结构集合")
	@GetMapping(value = "/tree/select/admin/menu/list")
	public R<List<TreeSelectMenuDTO>> treeSelectAdminMenuList() {
		List<TreeSelectMenuDTO> treeSelectAdminMenuDTOList = adminMenuService.queryAdminMenuTreeSelectList();
		return R.ok(treeSelectAdminMenuDTOList);
	}

}
