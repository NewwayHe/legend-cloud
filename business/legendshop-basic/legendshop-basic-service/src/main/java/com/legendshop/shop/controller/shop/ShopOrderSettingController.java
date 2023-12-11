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
import com.legendshop.shop.dto.ShopOrderSettingDTO;
import com.legendshop.shop.service.ShopDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺-订单设置
 *
 * @author legendshop
 */
@Tag(name = "店铺-订单设置")
@RestController
@RequestMapping(value = "/s/order/setting", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ShopOrderSettingController {


	private final ShopDetailService shopDetailService;


	@PreAuthorize("@pms.hasPermission('s_order_setting_get')")
	@Operation(summary = "【商家】获取店铺订单设置")
	@GetMapping
	public R<ShopOrderSettingDTO> orderSetting() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		ShopOrderSettingDTO shopOrderSettingDTO = shopDetailService.getShopOrderSetting(shopId);
		return R.ok(shopOrderSettingDTO);
	}

	@PreAuthorize("@pms.hasPermission('s_order_setting_update')")
	@Operation(summary = "【商家】更新店铺订单设置")
	@PostMapping("/update")
	public R<Void> updateOrderSetting(@Valid @RequestBody ShopOrderSettingDTO shopOrderSettingDTO) {

		Long shopId = SecurityUtils.getShopUser().getShopId();
		shopOrderSettingDTO.setShopId(shopId);
		return shopDetailService.updateShopOrderSetting(shopOrderSettingDTO);
	}


}
