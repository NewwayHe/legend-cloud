/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.fallback.AdminFileApiFallback;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author legendshop
 */
@FeignClient(contextId = "AdminFileApi", value = ServiceNameConstants.BASIC_SERVICE, fallback = AdminFileApiFallback.class)
public interface AdminFileApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	@GetMapping(value = PREFIX + "/admin/file/checkFile")
	R<Long> checkFile();
}
