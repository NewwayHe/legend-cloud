/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.user;

import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.AppStartAdvDTO;
import com.legendshop.shop.service.AppStartAdvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@Tag(name = "app启动广告")
@RestController
@RequestMapping(value = "/app/start/adv", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserAppStartAdvController {

	private final AppStartAdvService appStartAdvService;

	/**
	 * 获取随机一条app启动广告
	 *
	 * @return
	 */
	@Operation(summary = "【用户】获取随机一条app启动广告")
	@GetMapping
	public R<AppStartAdvDTO> getAppStartAdv() {
		return R.ok(appStartAdvService.getRandomAppStartAdv());
	}
}
