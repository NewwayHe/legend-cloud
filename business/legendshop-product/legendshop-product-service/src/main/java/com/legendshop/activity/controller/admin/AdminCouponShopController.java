/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller.admin;


import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.service.CouponShopService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author legendshop
 */
@Tag(name = "平台优惠券（指定店铺类型），控制器")
@RestController
@RequestMapping(value = "/admin/couponShop", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminCouponShopController {

	private final CouponShopService couponShopService;

	@PutMapping
	@SystemLog("更新平台优惠券所能使用的店铺")
	@Parameters({
			@Parameter(name = "selectShopId", description = "已勾选的shopId", required = true),
			@Parameter(name = "couponId", description = "优惠券ID", required = false)
	})
	@Operation(summary = "【平台】更新平台优惠券所能使用的店铺", description = "")
	public R updateCouponProduct(@RequestBody CouponDTO couponDTO) {
		return couponShopService.update(couponDTO);
	}


	@DeleteMapping("/{couponShopId}/{couponId}")
	@SystemLog("删除平台优惠券所能使用的店铺")
	@Operation(summary = "【平台】删除平台优惠券所能使用的店铺", description = "")
	@Parameters({
			@Parameter(name = "couponShopId", description = "优惠店铺ID", required = true),
			@Parameter(name = "couponId", description = "优惠券ID", required = true)
	})
	public R updateCouponProduct(@PathVariable(value = "couponShopId") Long couponShopId, @PathVariable(value = "couponId") Long couponId) {
		return R.ok(couponShopService.delete(couponShopId, couponId));
	}

	@PostMapping("/copy/{couponId}")
	@SystemLog("拷贝平台优惠券所能使用的店铺")
	@Parameter(name = "couponId", description = "优惠ID", required = true)
	@Operation(summary = "【平台】拷贝平台优惠券所能使用的店铺", description = "")
	public R copyCouponShop(@PathVariable(value = "couponId") Long couponId) {
		return couponShopService.copyCouponShop(couponId);
	}
}
