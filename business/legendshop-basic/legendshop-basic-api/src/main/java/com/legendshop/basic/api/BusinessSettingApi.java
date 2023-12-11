/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 业务配置服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "businessSettingApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface BusinessSettingApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;


	@GetMapping(value = PREFIX + "/business/setting/get/by/type")
	R<String> getByType(@RequestParam(value = "type") String type);

	@PostMapping(value = PREFIX + "/business/setting/update/by/type")
	R<Void> updateByType(@RequestParam(value = "type") String type, @RequestParam("categorySetting") String categorySetting);
}
