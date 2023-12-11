/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.admin;

import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.DecoratePageCategoryEnum;
import com.legendshop.basic.enums.DecoratePageSourceEnum;
import com.legendshop.basic.enums.DecoratePageStatusEnum;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.basic.query.DecoratePageQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.api.CategoryApi;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.ProductGroupApi;
import com.legendshop.product.bo.DecorateProductBO;
import com.legendshop.product.bo.ProductGroupBO;
import com.legendshop.product.dto.CategoryTree;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.product.query.ProductGroupQuery;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.shop.bo.DecoratePageBO;
import com.legendshop.shop.service.DecoratePageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 装修中心(DecoratePage)控制层，汇集装修组件所需接口
 *
 * @author legendshop
 */
@Tag(name = "装修中心")
@RestController
@RequestMapping(value = "/admin/decorate/action", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminDecorateCenterController {

	private final ProductApi productApi;

	private final CategoryApi categoryApi;

	private final ProductGroupApi productGroupApi;

	private final DecoratePageService decoratePageService;


	@Operation(summary = "【后台】获取商品弹层数据")
	@Parameters({
			@Parameter(name = "curPage", description = "当前页码"),
			@Parameter(name = "pageSize", description = "获取数量"),
			@Parameter(name = "productName", description = "商品名称")
	})
	@PostMapping("/prodList")
	public R<PageSupport<DecorateProductBO>> prodList(Integer curPage, Integer pageSize, String productName) {
		ProductQuery productQuery = new ProductQuery();
		productQuery.setCurPage(curPage);
		productQuery.setPageSize(pageSize);
		productQuery.setName(productName);
		productQuery.setStatus(ProductStatusEnum.PROD_ONLINE.getValue());
		productQuery.setOpStatus(OpStatusEnum.PASS.getValue());
		productQuery.setDelStatus(ProductDelStatusEnum.PROD_NORMAL.getValue());
		PageSupport<DecorateProductBO> result = productApi.queryDecorateProductList(productQuery).getData();
		return R.ok(result);
	}


	@Operation(summary = "【后台】获取分类弹层数据")
	@PostMapping("/category")
	public R<List<CategoryTree>> category() {
		R<String> result = categoryApi.getDecorateCategoryList();
		if (!result.getSuccess()) {
			return R.fail(result.getMsg());
		}
		List<CategoryTree> categoryTreeList = JSONUtil.toList(JSONUtil.parseArray(result.getData()), CategoryTree.class);
		return R.ok(categoryTreeList);
	}


	@Operation(summary = "【后台】获取分组弹层数据")
	@Parameters({
			@Parameter(name = "curPage", description = "当前页码"),
			@Parameter(name = "pageSize", description = "获取数量"),
			@Parameter(name = "productGroupName", description = "分组名称")
	})
	@PostMapping("/prodGroup")
	public R<PageSupport<ProductGroupBO>> prodGroup(Integer curPage, Integer pageSize, String productGroupName) {
		ProductGroupQuery productGroupQuery = new ProductGroupQuery();
		productGroupQuery.setCurPage(curPage);
		productGroupQuery.setPageSize(pageSize);
		productGroupQuery.setName(productGroupName);
		return productGroupApi.queryProductGroupListPage(productGroupQuery);
	}


	@Operation(summary = "【后台】获取PC页面弹层数据")
	@Parameters({
			@Parameter(name = "curPage", description = "当前页码"),
			@Parameter(name = "pageSize", description = "获取数量"),
			@Parameter(name = "pageName", description = "页面名称")
	})
	@PostMapping("/pc/poster/page")
	public R<PageSupport<DecoratePageBO>> pcPosterPage(Integer curPage, Integer pageSize, String pageName) {
		DecoratePageQuery decoratePageQuery = new DecoratePageQuery();
		decoratePageQuery.setCurPage(curPage);
		decoratePageQuery.setPageSize(pageSize);
		decoratePageQuery.setName(pageName);
		// 只获取已发布的海报页
		decoratePageQuery.setCategory(DecoratePageCategoryEnum.POSTER.value());
		decoratePageQuery.setStatus(DecoratePageStatusEnum.RELEASED.getNum());
		decoratePageQuery.setSource(DecoratePageSourceEnum.PC.value());
		PageSupport<DecoratePageBO> result = decoratePageService.queryPageList(decoratePageQuery);
		return R.ok(result);
	}

	@Operation(summary = "【后台】获取mobile页面弹层数据")
	@Parameters({
			@Parameter(name = "curPage", description = "当前页码"),
			@Parameter(name = "pageSize", description = "获取数量"),
			@Parameter(name = "pageName", description = "页面名称")
	})
	@PostMapping("/mobile/poster/page")
	public R<PageSupport<DecoratePageBO>> mobilePosterPage(Integer curPage, Integer pageSize, String pageName) {
		DecoratePageQuery decoratePageQuery = new DecoratePageQuery();
		decoratePageQuery.setCurPage(curPage);
		decoratePageQuery.setPageSize(pageSize);
		decoratePageQuery.setName(pageName);
		// 只获取已发布的海报页
		decoratePageQuery.setCategory(DecoratePageCategoryEnum.POSTER.value());
		decoratePageQuery.setStatus(DecoratePageStatusEnum.RELEASED.getNum());
		decoratePageQuery.setSource(DecoratePageSourceEnum.MOBILE.value());
		PageSupport<DecoratePageBO> result = decoratePageService.queryPageList(decoratePageQuery);
		return R.ok(result);
	}


}
