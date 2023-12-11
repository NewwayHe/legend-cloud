/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.admin;

import com.legendshop.basic.dto.SystemConfigDTO;
import com.legendshop.basic.service.SystemConfigService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 基本设置控制器
 *
 * @author legendshop
 */
@Tag(name = "基本设置")
@RestController
@RequestMapping(value = "/admin/system/config", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminSystemConfigController {

	private final SystemConfigService systemConfigService;

	/**
	 * 基本信息查询
	 *
	 * @return
	 */
	@Operation(summary = "【后台】基本信息查询")
	@GetMapping
	public R<SystemConfigDTO> getSystemConfig() {
		return R.ok(systemConfigService.getSystemConfig());
	}


	/**
	 * 编辑基本信息
	 *
	 * @param systemConfigDTO
	 * @return
	 */
	@Operation(summary = "【后台】编辑基本信息")
	@PutMapping
	public R<Boolean> update(@Valid @RequestBody SystemConfigDTO systemConfigDTO) {
		return R.ok(systemConfigService.update(systemConfigDTO));
	}
}
