/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller;

import com.legendshop.activity.dto.ActivityConfigDTO;
import com.legendshop.activity.service.ActivityConfigService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 活动配置
 *
 * @author legendshop
 * @create: 2021-11-02 15:15
 */
@Tag(name = "营销活动配置")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/activitySetting", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActivityConfigController {

	private final ActivityConfigService activityConfigService;

	@GetMapping
	@Operation(summary = "获取营销活动配置", description = "")
	public R<ActivityConfigDTO> getConfig() {
		return activityConfigService.getConfig();
	}
}
