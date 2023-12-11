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
import com.legendshop.product.dto.PreSellProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author legendshop
 */
@FeignClient(contextId = "preSellProductApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface PreSellProductApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;
	/**
	 * 根据预售商品ID获取预售商品信息的方法。
	 *
	 * @param preSellProdId 预售商品ID
	 * @return 包含预售商品信息的结果对象
	 */
	@GetMapping(PREFIX + "/preSellProductClient/getByProductId")
	R<PreSellProductDTO> getByProductId(@RequestParam("preSellProdId") Long preSellProdId);

}
