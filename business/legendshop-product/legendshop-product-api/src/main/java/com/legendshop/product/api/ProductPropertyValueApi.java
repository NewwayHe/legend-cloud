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
import com.legendshop.product.bo.ProductPropertyValueBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "productPropertyValueApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface ProductPropertyValueApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	@PostMapping(PREFIX + "/productPropertyValue/getProductPropertyValue")
	R<List<ProductPropertyValueBO>> getProductPropertyValue(@RequestBody List<Long> valueIdList);
}
