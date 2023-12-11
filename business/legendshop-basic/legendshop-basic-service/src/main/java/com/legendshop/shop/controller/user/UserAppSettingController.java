/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.user;

import com.legendshop.basic.dto.CategorySettingDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.service.AppSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端--移动装修基础设置控制器
 *
 * @author legendshop
 */
@Tag(name = "移动端基础设置")
@RestController
@RequestMapping(value = "/p/app/setting", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserAppSettingController {

	private final AppSettingService appSettingService;

	@GetMapping(value = "/category")
	@Operation(summary = "【用户】获取分类设置")
	public R<CategorySettingDTO> getCategorySetting() {
		return R.ok(appSettingService.getCategorySetting());
	}


}
