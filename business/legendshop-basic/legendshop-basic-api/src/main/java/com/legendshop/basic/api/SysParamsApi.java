/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.PayTypeDTO;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.dto.SysParamsDTO;
import com.legendshop.basic.enums.SysParamGroupEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
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
@FeignClient(contextId = "sysParamsApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface SysParamsApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 根据主配置的name得到配置的dto
	 *
	 * @param name
	 * @param clazz
	 * @return
	 */
	@PostMapping(value = PREFIX + "/sysParams/getConfigDtoByParamName")
	<T> R<T> getConfigDtoByParamName(@RequestParam(value = "name") String name, @RequestBody Class<T> clazz);

	/**
	 * 根据主配置的name得到配置的dto
	 *
	 * @param name
	 * @param clazz
	 * @return
	 */
	@PostMapping(value = PREFIX + "/sysParams/getNotCacheConfigByName")
	<T> R<T> getNotCacheConfigByName(@RequestParam(value = "name") String name, @RequestBody Class<T> clazz);

	@GetMapping(value = PREFIX + "/sysParams/getSysParamItemsByParamName")
	R<List<SysParamItemDTO>> getSysParamItemsByParamName(@RequestParam(value = "name") String name);

	/**
	 * {@link SysParamGroupEnum}
	 *
	 * @param groupEnum
	 * @return
	 */
	@GetMapping(value = PREFIX + "/sysParams/getByGroup")
	R<List<SysParamsDTO>> getByGroup(@RequestParam(value = "groupEnum") SysParamGroupEnum groupEnum);

	@GetMapping(value = PREFIX + "/sysParams/getEnabledPayType")
	R<List<PayTypeDTO>> getEnabledPayType();

	/**
	 * 用户支持的支付类型
	 */
	@GetMapping(value = PREFIX + "/sysParams/getUseEnabledPayType")
	R<List<PayTypeDTO>> getUseEnabledPayType();
}
