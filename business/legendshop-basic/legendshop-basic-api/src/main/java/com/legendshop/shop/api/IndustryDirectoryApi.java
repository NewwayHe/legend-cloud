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
import com.legendshop.shop.dto.IndustryDirectoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 店铺收藏服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "IndustryDirectoryApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface IndustryDirectoryApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 根据ID获取行业
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(PREFIX + "/industry/directory/getById")
	R<IndustryDirectoryDTO> getById(@RequestParam("id") Long id);

	/**
	 * 根据ID获取行业
	 *
	 * @param id
	 * @return
	 */
	@PostMapping(PREFIX + "/industry/directory/queryById")
	R<List<IndustryDirectoryDTO>> queryById(@RequestBody List<Long> id);
}
