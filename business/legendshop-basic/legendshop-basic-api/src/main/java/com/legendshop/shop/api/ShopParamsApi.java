/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.shop.dto.ShopParamItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 系统主配置服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "shopParamsApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface ShopParamsApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 根据主配置的name得到配置的dto
	 *
	 * @param name
	 * @param clazz
	 * @return
	 */
	@PostMapping(value = PREFIX + "/shopParams/getConfigDtoByParamName")
	<T> R<T> getConfigDtoByParamName(@RequestParam(value = "shopId") Long shopId, @RequestParam(value = "name") String name, @RequestBody Class<T> clazz);

	@GetMapping(value = PREFIX + "/shopParams/getSysParamItemsByParamName")
	R<List<ShopParamItemDTO>> getSysParamItemsByParamName(@RequestParam(value = "name") String name, @RequestParam(value = "shopId") Long shopId);

}
