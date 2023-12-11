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
import com.legendshop.product.bo.ProductCommentStatisticsBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author legendshop
 */
@FeignClient(contextId = "productCommentStatisticsApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface ProductCommentStatisticsApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	@GetMapping(PREFIX + "/productCommentStatistics/getByShopId/{id}")
	R<ProductCommentStatisticsBO> getProductCommentStatByShopId(@PathVariable("id") Long id);
}
