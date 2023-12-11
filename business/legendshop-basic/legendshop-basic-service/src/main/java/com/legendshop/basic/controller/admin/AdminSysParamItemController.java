/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.BatchUpdateSysParamDTO;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.query.SysParamItemQuery;
import com.legendshop.basic.service.SysParamItemService;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * (SysParamItem)表控制层
 *
 * @author legendshop
 * @since 2020-08-28 14:17:39
 */
@Tag(name = "配置项")
@RestController
@RequestMapping(value = "/admin/sys/param/item", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminSysParamItemController {

	/**
	 * (SysParamItem)服务对象
	 */
	@Autowired
	private SysParamItemService sysParamItemService;
	@Autowired
	private SysParamsService sysParamsService;

	@PreAuthorize("@pms.hasPermission('admin_sys_param_item_get')")
	@Operation(summary = "【后台】根据id查询配置项")
	@Parameter(name = "id", description = "id", required = true)
	@GetMapping("/{id}")
	public R<SysParamItemDTO> getById(@PathVariable("id") Long id) {
		return R.ok(sysParamItemService.getById(id));
	}


	@PreAuthorize("@pms.hasPermission('admin_sys_param_item_del')")
	@Operation(summary = "【后台】根据id删除配置项")
	@Parameter(name = "id", description = "id", required = true)
	@DeleteMapping("/{id}")
	public R deleteById(@PathVariable("id") Long id) {
		return R.ok(sysParamItemService.deleteById(id));
	}


	@PreAuthorize("@pms.hasPermission('admin_sys_param_item_save')")
	@Operation(summary = "【后台】保存配置项")
	@PostMapping
	public R save(@Valid @RequestBody SysParamItemDTO sysParamItemDTO) {
		sysParamItemDTO.setUpdateTime(new Date());
		return sysParamItemService.save(sysParamItemDTO);
	}


	@PreAuthorize("@pms.hasPermission('admin_sys_param_item_update')")
	@Operation(summary = "【后台】修改配置项")
	@PutMapping
	public R update(@Valid @RequestBody SysParamItemDTO sysParamItemDTO) {
		sysParamItemDTO.setUpdateTime(new Date());
		return R.ok(sysParamItemService.update(sysParamItemDTO));
	}

	@PreAuthorize("@pms.hasPermission('admin_sys_param_item_updateSysItems')")
	@Operation(summary = "【后台】批量修改配置项")
	@PutMapping("/sys/items")
	public R updateSysItems(@Valid @RequestBody List<SysParamItemDTO> sysParamItemDtoList) {
		sysParamItemService.updateSysParamItems(sysParamItemDtoList);
		return R.ok();
	}

	@PreAuthorize("@pms.hasPermission('admin_sys_param_item_updateValueOnlyById')")
	@Operation(summary = "【后台】批量修改配置项value")
	@PutMapping("/sys/value/items")
	public R updateValueOnlyById(@Valid @RequestBody BatchUpdateSysParamDTO batchUpdateSysParamDTO) {
		sysParamsService.updateValueOnlyById(batchUpdateSysParamDTO);
		return R.ok();
	}


	@PreAuthorize("@pms.hasPermission('admin_sys_param_item_page')")
	@Operation(summary = "【后台】查询配置项列表")
	@GetMapping("/page")
	public R<PageSupport<SysParamItemDTO>> queryPage(SysParamItemQuery sysParamItemQuery) {
		PageSupport<SysParamItemDTO> pageList = sysParamItemService.queryPageList(sysParamItemQuery);
		return R.ok(pageList);
	}
}
