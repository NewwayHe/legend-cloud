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
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "productGroupRelationApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface ProductGroupRelationApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	/**
	 * 根据分组ID获取商品ID集合
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(PREFIX + "/productGroupRelation/{id}")
	R<List<Long>> getProductIdListByGroupId(@PathVariable("id") Long id);
}
