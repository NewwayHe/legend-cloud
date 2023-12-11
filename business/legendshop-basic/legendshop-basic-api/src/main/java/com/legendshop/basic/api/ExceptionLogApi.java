/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.ExceptionLogDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 错误日志服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "exceptionLogApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface ExceptionLogApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	@PostMapping(value = PREFIX + "/exceptionLog/saveExceptionLog")
	R<Long> save(@RequestBody ExceptionLogDTO dto);
}
