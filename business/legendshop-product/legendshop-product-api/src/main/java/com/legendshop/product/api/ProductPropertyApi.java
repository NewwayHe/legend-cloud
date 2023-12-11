/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.product.bo.ProductPropertyBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "productPropertyApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface ProductPropertyApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	@GetMapping(PREFIX + "/productProperty/queryParamByCategoryId")
	R<List<ProductPropertyBO>> queryParamByCategoryId(@RequestParam("categoryId") Long categoryId, @RequestParam(required = false, value = "productId") Long productId);

	@PostMapping(PREFIX + "/productProperty/queryParamByCategoryIds")
	R<List<ProductPropertyBO>> queryParamByCategoryIds(@RequestBody List<Long> categoryId, @RequestParam(required = false, value = "productId") Long productId);

	@GetMapping(PREFIX + "/productProperty/queryAttachmentByUrl")
	R<List<String>> queryAttachmentByUrl(@RequestParam("url") String url);
}
