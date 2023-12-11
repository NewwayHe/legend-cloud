/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.api;

import com.legendshop.activity.dto.CouponProductDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.product.dto.ProductItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "couponProductApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface CouponProductApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	/**
	 * 根据id查询
	 *
	 * @param couponId
	 * @return
	 */
	@GetMapping(PREFIX + "/coupon/queryByCouponId")
	R<List<CouponProductDTO>> queryByCouponId(@RequestParam(value = "couponId") Long couponId);


	/**
	 * 根据id查询商品项
	 *
	 * @param couponId
	 * @return
	 */
	@GetMapping(PREFIX + "/coupon/queryInfoByCouponId")
	R<List<ProductItemDTO>> queryInfoByCouponId(@RequestParam(value = "couponId") Long couponId);


}
