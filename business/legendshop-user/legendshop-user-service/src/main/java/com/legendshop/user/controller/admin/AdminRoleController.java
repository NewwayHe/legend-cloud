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
import com.legendshop.basic.dto.RoleMenuDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.user.dto.AdminRoleDTO;
import com.legendshop.user.query.RoleQuery;
import com.legendshop.user.service.AdminRoleMenuService;
import com.legendshop.user.service.AdminRoleService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(value = "/admin/role", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "管理员角色管理")
public class AdminRoleController {

	private final AdminRoleService roleService;

	private final AdminRoleMenuService roleMenuService;

	/**
	 * 分页查询角色信息
	 *
	 * @param roleQuery 角色查询封装对象
	 * @return pageList
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin_role_adminRoleManage_page')")
	@Operation(summary = "【后台】分页查询角色信息")
	public R<PageSupport<AdminRoleDTO>> getRolePage(RoleQuery roleQuery) {
		PageSupport<AdminRoleDTO> rolePage = roleService.page(roleQuery);
		return R.ok(rolePage);
	}

	@GetMapping("/list")
	@PreAuthorize("@pms.hasPermission('admin_role_adminRoleManage_list')")
	@Operation(summary = "【后台】获取后台管理角色列表")
	public R<List<AdminRoleDTO>> getRoleList() {
		return R.ok(roleService.queryAll());
	}

	/**
	 * 保存角色菜单
	 *
	 * @param roleMenuDTO 角色对象
	 * @return success、false
	 */
	@PostMapping("/menu")
	@SystemLog("保存角色菜单")
	@PreAuthorize("@pms.hasPermission('admin_role_adminRoleManage_saveMenu')")
	@Operation(summary = "【后台】保存角色 菜单")
	public R<Boolean> saveRoleMenus(@RequestBody RoleMenuDTO roleMenuDTO) {
		AdminRoleDTO roleDTO = roleService.getById(roleMenuDTO.getRoleId());
		return R.ok(roleMenuService.saveRoleMenus(roleDTO.getRoleCode(), roleDTO.getId(), roleMenuDTO.getMenuIds()));
	}

	/**
	 * 保存角色
	 *
	 * @param roleDTO 角色对象
	 * @return success、false
	 */
	@PostMapping
	@SystemLog("保存角色")
	@PreAuthorize("@pms.hasPermission('admin_role_adminRoleManage_save')")
	@Operation(summary = "【后台】保存角色")
	public R save(@Valid @RequestBody AdminRoleDTO roleDTO) {
		return roleService.save(roleDTO);
	}

	/**
	 * 修改角色
	 *
	 * @param roleDTO 角色对象
	 * @return success、false
	 */
	@PutMapping
	@SystemLog("修改角色")
	@PreAuthorize("@pms.hasPermission('admin_role_adminRoleManage_update')")
	@Operation(summary = "【后台】修改角色")
	public R<Boolean> update(@Valid @RequestBody AdminRoleDTO roleDTO) {
		return R.ok(roleService.update(roleDTO));
	}


	/**
	 * 根据id删除角色
	 *
	 * @param id
	 * @return success、false
	 */
	@DeleteMapping("/{id}")
	@SystemLog("删除角色")
	@PreAuthorize("@pms.hasPermission('admin_role_adminRoleManage_del')")
	@Operation(summary = "【后台】删除角色")
	public R<Boolean> deleteById(@PathVariable Long id) {
		return R.ok(roleService.deleteById(id));
	}

}
