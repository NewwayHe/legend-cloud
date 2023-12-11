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
import com.legendshop.activity.service.CouponProductService;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.ProductItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class CouponProductApiImpl implements CouponProductApi {

	final CouponProductService couponProductService;

	@Override
	public R<List<CouponProductDTO>> queryByCouponId(@RequestParam(value = "couponId") Long couponId) {
		return R.ok(couponProductService.queryByCouponId(couponId));
	}


	@Override
	public R<List<ProductItemDTO>> queryInfoByCouponId(@RequestParam(value = "couponId") Long couponId) {
		return R.ok(couponProductService.queryInfoByCouponId(couponId));
	}

}
