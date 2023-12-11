/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.admin;

import com.legendshop.basic.dto.PayTypeDTO;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "后台缓存")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/cache")
public class AdminCacheController {

	final SysParamsService sysParamsService;

	@Operation(summary = "【后台】后台清空缓存")
	@GetMapping(value = "/cleanCache")
	public R<List<PayTypeDTO>> cleanCache() {
		sysParamsService.cleanCache();
		return R.ok();
	}
}
