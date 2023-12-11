/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.dto.ShopDetailDocumentsDTO;
import com.legendshop.shop.dto.ShopMessageDTO;
import com.legendshop.shop.service.ShopDecoratePageService;
import com.legendshop.shop.service.ShopDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商家店铺首页
 *
 * @author legendshop
 */
@Tag(name = "商家店铺首页")
@RestController
@AllArgsConstructor
public class ShopIndexController {

	final ShopDecoratePageService shopDecoratePageService;

	final ShopDetailService shopDetailService;


	final HttpServletRequest request;

	final UserTokenUtil userTokenUtil;


	@Operation(summary = "【用户】mobile端商家店铺首页")
	@Parameter(name = "shopId", description = "店铺ID", required = true)
	@PostMapping("/mobile/shop/index")
	public R<ShopDetailBO> mobileShopIndex(Long shopId) {
		Long userId = userTokenUtil.getUserId(request);
		R<ShopDetailBO> shopDetailBoR = shopDetailService.getUserShop(userId, shopId);
		return shopDetailBoR;
	}

	@Operation(summary = "商家店铺详情")
	@Parameter(name = "shopId", description = "店铺ID", required = true)
	@GetMapping("/shop/detail")
	public R<ShopMessageDTO> shopDetail(Long shopId) {
		R<ShopMessageDTO> shopDetailBoR = shopDetailService.getshopDetail(shopId);
		return shopDetailBoR;
	}

	@Operation(summary = "商家店铺证件详情")
	@Parameter(name = "shopId", description = "店铺ID", required = true)
	@GetMapping("/shop/getshopDetailDocuments")
	public R<ShopDetailDocumentsDTO> shopDetailDocuments(Long shopId) {
		Long userId = userTokenUtil.getUserId(request);
		R<ShopDetailDocumentsDTO> shopDetailBoR = shopDetailService.getshopDetailDocuments(userId, shopId);
		return shopDetailBoR;
	}

//	@Operation(summary = "【用户】pc端商家店铺首页")
//	 @Parameter(name = "shopId", description = "店铺ID" , required = true )
//	@PostMapping("/pc/shop/index")
//	public R<ShopDetailBO> pcShopIndex(Long shopId) {
//
//		Long userId = SecurityUtils.getUserId();
//		R<ShopDetailBO> userShop = shopDetailService.getUserShop(userId, shopId);
//		// 异步记录浏览历史
//
//		return userShop;
//	}


}
