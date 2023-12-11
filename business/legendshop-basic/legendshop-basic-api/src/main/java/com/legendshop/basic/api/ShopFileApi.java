/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.AttachmentFileFolderDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "shopFileApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface ShopFileApi {
	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/***
	 * [商家] 初始化商家根目录
	 * @param attachmentFileFolderDTO
	 * @return
	 */
	@PostMapping(value = PREFIX + "/s/file/save")
	R save(@RequestBody AttachmentFileFolderDTO attachmentFileFolderDTO);

	@GetMapping(value = PREFIX + "/s/file/getIds")
	R<List<Long>> getIds();

	@GetMapping(value = PREFIX + "/s/file/getByShopId")
	R<Long> getByShopId(@RequestParam(value = "id") Long id);

	@GetMapping(value = PREFIX + "/s/file/updateFileName")
	R<Long> updateFileName(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "shopId") Long shopId);
}

