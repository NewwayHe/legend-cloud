/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.AppStartAdvDTO;
import com.legendshop.shop.query.AppStartAdvQuery;
import com.legendshop.shop.service.AppStartAdvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 移动端启动广告控制器
 *
 * @author legendshop
 */
@Tag(name = "app启动广告")
@RestController
@RequestMapping(value = "/admin/app/start/adv", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminAppStartAdvController {

	@Autowired
	private AppStartAdvService appStartAdvService;

	@PreAuthorize("@pms.hasPermission('admin_appStartAdv_page')")
	@Operation(summary = "【后台】启动广告分页查询 admin_appStartAdv_page")
	@GetMapping("/page")
	public R<PageSupport<AppStartAdvDTO>> page(AppStartAdvQuery appStartAdvQuery) {
		PageSupport<AppStartAdvDTO> page = appStartAdvService.page(appStartAdvQuery);
		return R.ok(page);
	}

	@PreAuthorize("@pms.hasPermission('admin_appStartAdv_updateStatus')")
	@Operation(summary = "【后台】修改启动广告状态 admin_appStartAdv_updateStatus")
	@PutMapping("/updateStatus")
	public R<Boolean> updateStatus(@RequestBody AppStartAdvQuery appStartAdvQuery) {
		if (appStartAdvService.updateStatus(appStartAdvQuery.getId(), appStartAdvQuery.getStatus())) {
			return R.ok();
		}
		return R.fail();
	}


	@PreAuthorize("@pms.hasPermission('admin_appStartAdv_deleteById')")
	@Operation(summary = "【后台】删除启动广告信息 admin_appStartAdv_deleteById")
	@Parameter(name = "id", description = "广告id", required = true)
	@DeleteMapping("/{id}")
	public R deleteById(@PathVariable Long id) {
		return appStartAdvService.deleteAppAdv(id);
	}

	@PreAuthorize("@pms.hasPermission('admin_appStartAdv_add')")
	@Operation(summary = "【后台】添加启动广告信息 admin_appStartAdv_add")
	@PostMapping
	public R save(@Valid @RequestBody AppStartAdvDTO appStartAdvDTO) {
		appStartAdvService.save(appStartAdvDTO);
		return R.ok();
	}

	@PreAuthorize("@pms.hasPermission('admin_appStartAdv_update')")
	@Operation(summary = "【后台】修改启动广告信息 admin_appStartAdv_update")
	@PutMapping
	public R update(@Valid @RequestBody AppStartAdvDTO appStartAdvDTO) {
		return appStartAdvService.updateAppStartAdv(appStartAdvDTO);
	}

	@PreAuthorize("@pms.hasPermission('admin_appStartAdv_get')")
	@Operation(summary = "【后台】根据id查询启动广告信息 admin_appStartAdv_get")
	@Parameter(name = "id", description = "广告id", required = true)
	@GetMapping("/get/{id}")
	public R<AppStartAdvDTO> getById(@PathVariable("id") Long id) {
		AppStartAdvDTO appStartAdvDTO = appStartAdvService.getById(id);
		return R.ok(appStartAdvDTO);
	}


}
