/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.DecoratePageCategoryEnum;
import com.legendshop.basic.enums.DecoratePageSourceEnum;
import com.legendshop.basic.enums.DecoratePageStatusEnum;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.ShopCategoryApi;
import com.legendshop.product.bo.DecorateProductBO;
import com.legendshop.product.dto.ShopCategoryTree;
import com.legendshop.shop.bo.ShopDecoratePageBO;
import com.legendshop.shop.dto.ShopDecorateProductQuery;
import com.legendshop.shop.query.ShopDecoratePageQuery;
import com.legendshop.shop.service.ShopDecoratePageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 装修中心(DecoratePage)控制层，汇集装修组件所需接口
 *
 * @author legendshop
 */
@Tag(name = "装修中心")
@RestController
@RequestMapping(value = "/s/decorate/action", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ShopDecorateCenterController {

	private final ProductApi productApi;

	private final ShopCategoryApi shopCategoryApi;

	private final ShopDecoratePageService shopDecoratePageService;


	@Operation(summary = "【商家】获取商品弹层数据")
	@PostMapping("/productList")
	public R<PageSupport<DecorateProductBO>> productList(@RequestBody ShopDecorateProductQuery shopDecorateProductQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		shopDecorateProductQuery.setShopId(shopId);
		return productApi.queryDecorateProductList(shopDecorateProductQuery);
	}


	@Operation(summary = "【商家】获取店铺分类弹层数据")
	@GetMapping("/category")
	public R<List<ShopCategoryTree>> category() {
		return shopCategoryApi.getByShopIdAndStatus(SecurityUtils.getShopUser().getShopId(), CommonConstants.STATUS_NORMAL);
	}

	@Operation(summary = "【商家】获取PC店铺海报页面弹层数据")
	@Parameters({
			@Parameter(name = "curPageNO", description = "当前页码"),
			@Parameter(name = "pageName", description = "页面名称")
	})
	@GetMapping("/pcPosterPage")
	public R<PageSupport<ShopDecoratePageBO>> posterPage(@RequestParam Integer curPageNO, @RequestParam String pageName) {
		ShopDecoratePageQuery shopDecoratePageQuery = new ShopDecoratePageQuery();
		shopDecoratePageQuery.setCurPage(curPageNO);
		shopDecoratePageQuery.setPageSize(5);
		shopDecoratePageQuery.setName(pageName);
		// 只获取已发布的海报页
		shopDecoratePageQuery.setCategory(DecoratePageCategoryEnum.POSTER.value());
		shopDecoratePageQuery.setStatus(DecoratePageStatusEnum.RELEASED.getNum());
		shopDecoratePageQuery.setSource(DecoratePageSourceEnum.PC.value());
		PageSupport<ShopDecoratePageBO> result = shopDecoratePageService.queryPageList(shopDecoratePageQuery);
		return R.ok(result);
	}


}
