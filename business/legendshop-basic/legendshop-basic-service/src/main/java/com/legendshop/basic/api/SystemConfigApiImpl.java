/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.SystemConfigDTO;
import com.legendshop.basic.service.SystemConfigService;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * Feign调用
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class SystemConfigApiImpl implements SystemConfigApi {

	private final SystemConfigService systemConfigService;

	@Override
	public R<SystemConfigDTO> getSystemConfig() {
		return R.ok(this.systemConfigService.getSystemConfig());
	}
}
