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
import com.legendshop.basic.fallback.SystemConfigApiFallback;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 系统配置服务
 *
 * @author legendshop
 */
@FeignClient(value = ServiceNameConstants.BASIC_SERVICE, fallback = SystemConfigApiFallback.class)
public interface SystemConfigApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 基本信息查询
	 *
	 * @return
	 */
	@GetMapping(value = PREFIX + "/systemConfig/getSystemConfig")
	R<SystemConfigDTO> getSystemConfig();
}
