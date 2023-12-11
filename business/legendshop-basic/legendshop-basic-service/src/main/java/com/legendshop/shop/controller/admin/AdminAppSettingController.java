/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.admin;

import com.legendshop.basic.dto.CategorySettingDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.service.AppSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 后台--移动装修基础设置
 *
 * @author legendshop
 */
@Tag(name = "移动端基础设置")
@RestController
@RequestMapping(value = "/admin/app/setting", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminAppSettingController {

	private final AppSettingService appSettingService;


	@PreAuthorize("@pms.hasPermission('admin_app_setting_get_category_setting')")
	@Operation(summary = "【后台】获取分类设置")
	@GetMapping(value = "/category")
	public R<CategorySettingDTO> getCategorySetting() {
		return R.ok(appSettingService.getCategorySetting());
	}


	@PreAuthorize("@pms.hasPermission('admin_app_setting_update_category_setting')")
	@Operation(summary = "【后台】保存分类设置")
	@Parameter(name = "categorySetting", description = "分类设置DTO", required = true)
	@PutMapping(value = "/update/category")
	public R<Void> updateCategorySetting(@RequestBody CategorySettingDTO categorySetting) {
		return appSettingService.updateCategorySetting(categorySetting);
	}

}
