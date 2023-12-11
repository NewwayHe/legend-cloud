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
import com.legendshop.shop.dto.PlateCapitalFlowDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 平台资金流水服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "plateCapitalFlowApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface PlateCapitalFlowApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 保存
	 *
	 * @param plateCapitalFlowDTO
	 * @return
	 */
	@PostMapping(PREFIX + "/plate/capital/flow/save")
	R<Long> save(@RequestBody PlateCapitalFlowDTO plateCapitalFlowDTO);
}
