/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.admin;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.RoleMenuDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.bo.OrdinaryRoleBO;
import com.legendshop.user.dto.OrdinaryRoleDTO;
import com.legendshop.user.dto.UpdateOrdinaryRoleDTO;
import com.legendshop.user.query.OrdinaryRoleQuery;
import com.legendshop.user.service.OrdinaryRoleMenuService;
import com.legendshop.user.service.OrdinaryRoleService;
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
@RequestMapping(value = "/admin/ordinary/role", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "普通角色管理")
public class AdminOrdinaryRoleController {

	private final OrdinaryRoleService roleService;

	private final OrdinaryRoleMenuService roleMenuService;

	/**
	 * 分页查询角色信息
	 *
	 * @param roleQuery 角色查询封装对象
	 * @return pageList
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin_role_ordinaryRoleManage_page')")
	@Operation(summary = "【后台】分页查询普通用户角色信息")
	public R<PageSupport<OrdinaryRoleDTO>> getRolePage(OrdinaryRoleQuery roleQuery) {
		return R.ok(roleService.page(roleQuery));
	}

	@GetMapping("/list")
	@PreAuthorize("@pms.hasPermission('admin_role_ordinaryRoleManage_list')")
	@Operation(summary = "【后台】查询全部普通用户角色信息")
	public R<List<OrdinaryRoleDTO>> getRoleList(OrdinaryRoleQuery roleQuery) {
		return R.ok(roleService.queryAll(roleQuery));
	}

	/**
	 * 保存角色菜单
	 *
	 * @param roleMenuDTO 角色对象
	 * @return success、false
	 */
	@PostMapping("/menu")
	@PreAuthorize("@pms.hasPermission('admin_role_ordinaryRoleManage_saveMenu')")
	@Operation(summary = "【后台】保存普通用户角色菜单")
	public R<Boolean> saveRoleMenus(@RequestBody RoleMenuDTO roleMenuDTO) {
		OrdinaryRoleDTO roleDTO = roleService.getById(roleMenuDTO.getRoleId());
		return R.ok(roleMenuService.saveRoleMenus(roleDTO.getRoleCode(), roleDTO.getId(), roleMenuDTO.getMenuIds()));
	}

	/**
	 * 保存角色
	 *
	 * @param roleDTO 角色对象
	 * @return success、false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin_role_ordinaryRoleManage_save')")
	@Operation(summary = "【后台】保存普通用户角色")
	public R save(@Valid @RequestBody OrdinaryRoleDTO roleDTO) {
		roleDTO.setCreateTime(DateUtil.date());
		roleDTO.setUpdateTime(DateUtil.date());
		return roleService.save(roleDTO);
	}

	/**
	 * 修改角色
	 *
	 * @param roleDTO 角色对象
	 * @return success、false
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_role_ordinaryRoleManage_update')")
	@Operation(summary = "【后台】修改普通用户角色")
	public R<Boolean> update(@Valid @RequestBody OrdinaryRoleDTO roleDTO) {
		roleDTO.setUpdateTime(DateUtil.date());
		return R.ok(roleService.update(roleDTO));
	}


	/**
	 * 根据id删除角色
	 *
	 * @param id
	 * @return success、false
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin_role_ordinaryRoleManage_del')")
	@Operation(summary = "【后台】删除普通用户角色")
	public R<Boolean> deleteById(@PathVariable Long id) {
		return R.ok(roleService.deleteById(id));
	}

	/**
	 * 根据用户Id获取用户角色
	 *
	 * @param id
	 * @return success、false
	 */
	@PutMapping("/queryByUserId/{id}")
	@PreAuthorize("@pms.hasPermission('admin_role_ordinaryRoleManage_queryByUserId')")
	@Operation(summary = "【后台】根据用户Id获取用户角色")
	public R<List<OrdinaryRoleDTO>> queryByUserId(@PathVariable Long id) {
		return R.ok(roleService.queryByUserId(id));
	}

	@PostMapping("/remove/by/userId")
	@PreAuthorize("@pms.hasPermission('admin_ordinary_role_remove_by_userId')")
	@Operation(summary = "【后台】根据用户ID和角色ID移除普通用户角色")
	@Parameters({
			@Parameter(name = "userId", description = "用户Id", required = true),
			@Parameter(name = "roleId", description = "角色Id", required = true)
	})
	public R<Void> removeByUserId(@RequestParam Long userId, @RequestParam Long roleId) {
		return roleService.removeByUserId(userId, roleId);
	}


	@GetMapping("/get/role/page/by/userId")
	@PreAuthorize("@pms.hasPermission('admin_ordinary_role_get_role_page_by_userId')")
	@Operation(summary = "【后台】获取角色选择弹窗数据")
	public R<PageSupport<OrdinaryRoleBO>> getRolePageByUserId(OrdinaryRoleQuery ordinaryRoleQuery) {
		return roleService.getRolePageByUserId(ordinaryRoleQuery);
	}


	@PostMapping("/update/role/for/user")
	@PreAuthorize("@pms.hasPermission('admin_ordinary_role_update_role_for_user')")
	@Operation(summary = "【后台】指定用户更新关联角色")
	public R<Void> updateRoleForUser(@RequestBody UpdateOrdinaryRoleDTO updateOrdinaryRoleDTO) {
		return roleService.updateRoleForUser(updateOrdinaryRoleDTO.getUserId(), updateOrdinaryRoleDTO.getRoleIds());
	}

}
