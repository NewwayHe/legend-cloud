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
import com.legendshop.user.dto.DeptDTO;
import com.legendshop.user.dto.DeptTree;
import com.legendshop.user.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin/dept", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "部门管理")
public class AdminDeptController {
	private final DeptService deptService;

	/**
	 * 通过ID查询
	 *
	 * @param id ID
	 * @return SysDept
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable Long id) {
		return R.ok(deptService.getById(id));
	}

	/**
	 * 获取子部门列表
	 *
	 * @return 返回子级
	 */
	@Operation(summary = "获取子部门列表", description = "获取子部门列表")
	@GetMapping(value = "/getSubDeptList/{deptId}")
	public R<List<DeptDTO>> getSubDeptList(@PathVariable Long deptId) {
		return R.ok(deptService.querySubDept(deptId));
	}

	/**
	 * 返回树形菜单集合
	 *
	 * @return 树形菜单
	 */
	@Operation(summary = "获取所有部门列表", description = "获取所有部门列表")
	@GetMapping(value = "/getAllDeptList")
	public R<List<DeptTree>> getTree() {
		return R.ok(deptService.queryAllDeptTree());
	}

	/**
	 * 添加
	 *
	 * @param deptDTO 实体
	 * @return success/false
	 */
	@Operation(summary = "添加部门", description = "添加部门")
	@SystemLog("添加部门")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin_dept_add')")
	public R save(@Valid @RequestBody DeptDTO deptDTO) {
		return R.ok(deptService.save(deptDTO));
	}


	/**
	 * 删除
	 *
	 * @param id ID
	 * @return success/false
	 */
	@Operation(summary = "删除部门", description = "删除部门")
	@SystemLog("删除部门")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin_dept_del')")
	public R removeById(@PathVariable Long id) throws Exception {
		return R.ok(deptService.deleteByDeptId(id));
	}

	/**
	 * 编辑
	 *
	 * @param deptDTO 实体
	 * @return success/false
	 */
	@Operation(summary = "编辑部门", description = "编辑部门")
	@SystemLog("编辑部门")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_dept_edit')")
	public R update(@Valid @RequestBody DeptDTO deptDTO) {
		return R.ok(deptService.update(deptDTO));
	}


}
