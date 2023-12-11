/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.ProductUpdateBO;
import com.legendshop.product.service.DraftProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ShopDraftProductController
 * @date 2022/5/12 14:11
 * @description：商品草稿
 */
@Tag(name = "商品草稿管理")
@RestController
@RequestMapping(value = "/s/product/draft", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopDraftProductController {

	@Autowired
	private DraftProductService draftProductService;

	/**
	 * 商品详情
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_product_get')")
	@Parameter(name = "id", description = "商品ID", required = true)
	@Operation(summary = "【商家】商品草稿详情")
	public R<ProductUpdateBO> getDraftById(@PathVariable Long id) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		ProductUpdateBO productBO = draftProductService.getProductUpdateBOByProductId(id, shopId);
		return R.ok(productBO);
	}


	/**
	 * 草稿提审
	 */
	@PostMapping("/arraignment")
	@SystemLog(type = SystemLog.LogType.SHOP, value = "草稿提审")
	@Operation(summary = "【商家】草稿提审")
	@PreAuthorize("@pms.hasPermission('s_product_product_update')")
	public R<Void> arraignment(@RequestParam Long productId) {
		return draftProductService.arraignment(productId);
	}

	/**
	 * 草稿提审撤销
	 */
	@PostMapping("/revokeArraignment")
	@SystemLog(type = SystemLog.LogType.SHOP, value = "草稿提审撤销")
	@Operation(summary = "【商家】草稿提审撤销")
	@PreAuthorize("@pms.hasPermission('s_product_product_update')")
	public R revokeArraignment(@RequestParam Long productId) {
		return draftProductService.revokeArraignment(productId);
	}
}
