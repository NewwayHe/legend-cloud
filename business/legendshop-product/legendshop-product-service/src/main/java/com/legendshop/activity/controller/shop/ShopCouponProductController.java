/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller.shop;


import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.service.CouponProductService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author legendshop
 */
@Tag(name = "优惠券管理")
@RestController
@RequestMapping(value = "/s/couponProduct", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ShopCouponProductController {

	private final CouponProductService couponProductService;

	@PutMapping
	@SystemLog("更新优惠券商品")
	@Parameters({
			@Parameter(name = "selectSkuId", description = "已勾选的skuid", required = true),
			@Parameter(name = "unSelectSkuId", description = "未勾选的skuID", required = true),
			@Parameter(name = "couponId", description = "优惠券ID", required = false)
	})
	@PreAuthorize("@pms.hasPermission('s_coupon_updateCouponProduct')")
	@Operation(summary = "【商家】更新优惠券商品", description = "")
	public R updateCouponProduct(@RequestBody CouponDTO couponDTO) {
		return R.ok(couponProductService.update(couponDTO));
	}

	@DeleteMapping("/{couponProductId}/{couponId}")
	@SystemLog("删除优惠券商品")
	@Parameter(name = "couponProductId", description = "优惠商品ID", required = true)
	@PreAuthorize("@pms.hasPermission('s_coupon_deleteCouponProduct')")
	@Operation(summary = "【商家】删除优惠券商品", description = "")
	public R updateCouponProduct(@PathVariable(value = "couponProductId") Long couponProductId, @PathVariable(value = "couponId") Long couponId) {
		return R.ok(couponProductService.delete(couponProductId, couponId));
	}

	@PostMapping("/copy/{couponId}")
	@SystemLog("拷贝优惠券商品")
	@Parameter(name = "couponId", description = "优惠ID", required = true)
	@Operation(summary = "【商家】拷贝优惠券商品", description = "")
	public R copyCouponProduct(@PathVariable(value = "couponId") Long couponId) {
		return couponProductService.copyCouponProduct(couponId);
	}
}
