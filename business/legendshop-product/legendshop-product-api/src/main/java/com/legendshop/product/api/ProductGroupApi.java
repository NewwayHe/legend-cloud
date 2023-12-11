/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.product.bo.ProductGroupBO;
import com.legendshop.product.bo.ProductGroupRelationBO;
import com.legendshop.product.query.ProductGroupQuery;
import com.legendshop.product.query.ProductGroupRelationQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author legendshop
 */
@FeignClient(contextId = "productGroupApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface ProductGroupApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	/**
	 * 根据商品组ID获取商品组信息的方法。
	 *
	 * @param id 商品组ID
	 * @return 商品组信息对象
	 */
	@GetMapping(PREFIX + "/productGroup/{id}")
	R<ProductGroupBO> getProductGroup(@PathVariable("id") Long id);

	/**
	 * 分页查询通过名字列表
	 * @param productGroupQuery
	 * @return
	 */
	@PostMapping(PREFIX + "/productGroup/queryProductGroupListPage")
	R<PageSupport<ProductGroupBO>> queryProductGroupListPage(@RequestBody ProductGroupQuery productGroupQuery);

	/**
	 * 删除商品分组及其关联表关联数据
	 *
	 * @param relationQuery
	 * @return
	 */
	@PostMapping(PREFIX + "/productGroup/queryProduct/page")
	R<PageSupport<ProductGroupRelationBO>> queryProductList(@RequestBody ProductGroupRelationQuery relationQuery);


}
