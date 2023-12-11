/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.shop;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.service.ShopDetailService;
import com.legendshop.shop.vo.ShopDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 店铺-设置
 *
 * @author legendshop
 */
@Tag(name = "店铺-设置")
@RestController
@RequestMapping(value = "/s/setting", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ShopSettingController {

	private final ShopDetailService shopDetailService;


	@GetMapping
	@PreAuthorize("@pms.hasPermission('s_shop_setting_get')")
	@Operation(summary = "【商家】获取店铺基本信息")
	public R<ShopDetailVO> BasicInformation() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		ShopDetailVO shopDetailVO = shopDetailService.getShopDetailVO(shopId);
		return R.ok(shopDetailVO);
	}

	@PutMapping
	@PreAuthorize("@pms.hasPermission('s_shop_setting_update')")
	@Operation(summary = "【商家】编辑店铺基本信息")
	public R<Void> updateShopDetail(@Valid @RequestBody ShopDetailDTO shopDetail) {
		return shopDetailService.updateShopDetail(shopDetail);
	}

	/**
	 * 修改默认分销比例
	 */
	@PutMapping("/updateDefaultDisScale/{defaultDisScale}")
	@Operation(summary = "【商家】申修改默认分销比例")
	public R updateDefaultDisScale(@PathVariable BigDecimal defaultDisScale) {
		Long userId = SecurityUtils.getShopUser().getUserId();
		return R.ok(shopDetailService.updateDefaultDisScale(defaultDisScale, userId));
	}


	/**
	 * 修改默认分销比例
	 */
	@GetMapping("/getDefaultDisScale")
	@Operation(summary = "【商家】获取默认分销比例")
	public R<BigDecimal> updateDefaultDisScale() {
		Long shopUserId = SecurityUtils.getShopUser().getUserId();
		return shopDetailService.getDefaultDisScale(shopUserId);
	}
}
