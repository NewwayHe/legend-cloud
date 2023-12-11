/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.dto.*;
import com.legendshop.product.query.ProductDataCategoryQuery;
import com.legendshop.product.query.ProductDataSaleQuery;
import com.legendshop.product.query.ProductDataSaleTrendQuery;
import com.legendshop.product.query.ProductDataViewQuery;
import com.legendshop.product.service.ProductCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "商品报表统计")
@RestController
@RequestMapping(value = "/s/count", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopProductCountController {

	@Autowired
	private ProductCountService productCountService;

	@Operation(summary = "【商家】获取商品SPU统计")
	@GetMapping("/spu")
	public R<ProductDataSpuDTO> getSpuCount() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(productCountService.getShopSpuCount(shopId));

	}

	@Operation(summary = "【商家】获取商品SKU统计")
	@GetMapping("/sku")
	public R<ProductDataSkuDTO> getSkuCount() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(productCountService.getShopSkuCount(shopId));

	}

	@Operation(summary = "【商家】获取在售商品SKU统计")
	@GetMapping("/sku/sale")
	public R<ProductDataSkuOnSaleDTO> getSkuOnSaleCount() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(productCountService.getShopSkuOnSaleCount(shopId));

	}

	@Operation(summary = "【商家】获取商品SPU访问统计")
	@GetMapping("/spu/click")
	public R<ProductDataSpuClickDTO> getSpuClickCount() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(productCountService.getShopSpuClickCount(shopId));

	}

	@Operation(summary = "【商家】获取商品SKU分类排行信息")
	@GetMapping("/sku/category")
	public R<List<ProductDataSkuByCategoryDTO>> getProductSkuByCategory() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(productCountService.getShopSkuByCategory(shopId));
	}

	@Operation(summary = "【商家】获取商品list")
	@GetMapping("/good/list")
	public R<List<ProductDataGoodDTO>> getGoodList() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(productCountService.getGoodList(shopId));
	}

	@Operation(summary = "【商家】获取店铺商品概况")
	@GetMapping("/shop/detail")
	public R<ProductDataGoodsByShopDTO> getShopGoodsCount(String startDate, String endDate) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(productCountService.getShopGoodsCount(shopId, startDate, endDate));
	}

	@Operation(summary = "【商家】获取商品访问折线图")
	@GetMapping("/shop/line")
	public R<List<ProductDataViewLineDTO>> getProductViewLine(ProductDataViewQuery viewQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		viewQuery.setShopId(shopId);
		return R.ok(productCountService.getShopViewLine(viewQuery));
	}


	@Operation(summary = "【商家】获取商品访问概况分页")
	@GetMapping("/shop/page")
	public R<PageSupport<ProductDataViewPageDTO>> getProductViewPage(ProductDataViewQuery viewQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		viewQuery.setShopId(shopId);
		return R.ok(productCountService.getProductViewPage(viewQuery));
	}

	@Operation(summary = "【商家】导出excel商品访问概况分页")
	@GetMapping("/shop/page/excel")
	@ExportExcel(name = "商品访问概况", sheet = "商品访问概况")
	public List<ProductDataViewPageDTO> getProductViewPageExcel(ProductDataViewQuery viewQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		viewQuery.setShopId(shopId);
		return productCountService.getProductViewPageExcel(viewQuery);
	}

	@Operation(summary = "【商家】获取饼状图分类树")
	@GetMapping("/category/tree")
	public R<List<ProductDataCategoryDTO>> getCategoryTree(ProductDataCategoryQuery categoryQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		categoryQuery.setShopId(shopId);
		return R.ok(productCountService.getShopCategoryTree(categoryQuery));
	}

	@Operation(summary = "【后台】获取饼状图分类列表")
	@GetMapping("/category/list")
	public R<List<ProductDataCategoryDTO>> getCategory(ProductDataCategoryQuery categoryQuery) {
		return R.ok(productCountService.getShopCategory(categoryQuery));
	}

	@Operation(summary = "【商家】获取商品类目概况分页数据")
	@GetMapping("/category/page")
	public R<PageSupport<ProductDataCategoryPageDTO>> getCategoryDetailPage(ProductDataCategoryQuery categoryQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		categoryQuery.setShopId(shopId);
		return R.ok(productCountService.getShopCategoryDetailPage(categoryQuery));
	}

	@Operation(summary = "【商家】导出excel商品类目概况分页数据")
	@GetMapping("/category/page/excel")
	@ExportExcel(name = "商品类目概况", sheet = "商品类目概况")
	public List<ProductDataCategoryPageDTO> getCategoryDetailPageExcel(ProductDataCategoryQuery categoryQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		categoryQuery.setShopId(shopId);
		return productCountService.getShopCategoryDetailPageExcel(categoryQuery);
	}

	@Operation(summary = "【商家】获取销售排行分页数据")
	@GetMapping("/sale/order")
	public R<PageSupport<ProductDataShopSaleDTO>> getShopSaleDetailPage(ProductDataSaleQuery saleQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		saleQuery.setShopId(shopId);
		return R.ok(productCountService.getShopSaleDetailPage(saleQuery));
	}

	@Operation(summary = "【商家】导出excel销售排行分页数据")
	@GetMapping("/sale/order/excel")
	@ExportExcel(name = "销售排行", sheet = "销售排行")
	public List<ProductDataShopSaleDTO> getShopSaleDetailPageExcel(ProductDataSaleQuery saleQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		saleQuery.setShopId(shopId);
		return productCountService.getShopSaleDetailPageExcel(saleQuery);
	}

	@Operation(summary = "【商家】获取销售趋势图折线图数据")
	@GetMapping("/trend/line")
	public R<List<ProductDataGoodSaleDTO>> getShopSaleTrendLine(ProductDataSaleTrendQuery saleTrendQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		saleTrendQuery.setShopId(shopId);
		return R.ok(productCountService.getShopSaleTrendLine(saleTrendQuery));
	}

	@Operation(summary = "【商家】获取销售趋势图成交列表数据")
	@GetMapping("/trend/deal")
	public R<List<ProductDataSaleDealDTO>> getShopSaleDealPage(ProductDataSaleTrendQuery saleTrendQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		saleTrendQuery.setShopId(shopId);
		return R.ok(productCountService.getShopSaleDealPage(saleTrendQuery));
	}

	@Operation(summary = "【商家】导出excel销售趋势图成交列表数据")
	@GetMapping("/trend/deal/excel")
	@ExportExcel(name = "趋势图成交列表", sheet = "趋势图成交列表")
	public List<ProductDataSaleDealDTO> getShopSaleDealPageExcel(ProductDataSaleTrendQuery saleTrendQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		saleTrendQuery.setShopId(shopId);
		return productCountService.getShopSaleDealPage(saleTrendQuery);
	}

	@Operation(summary = "【商家】获取销售趋势图访问列表数据")
	@GetMapping("/trend/view")
	public R<List<ProductDataSaleViewDTO>> getShopSaleViewPage(ProductDataSaleTrendQuery saleTrendQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		saleTrendQuery.setShopId(shopId);
		return R.ok(productCountService.getShopSaleViewPage(saleTrendQuery));
	}

	@Operation(summary = "【商家】导出excel销售趋势图访问列表数据")
	@GetMapping("/trend/view/excel")
	@ExportExcel(name = "趋势图访问列表", sheet = "趋势图访问列表")
	public List<ProductDataSaleViewDTO> getShopSaleViewPageExcel(ProductDataSaleTrendQuery saleTrendQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		saleTrendQuery.setShopId(shopId);
		return productCountService.getShopSaleViewPage(saleTrendQuery);
	}

	@Operation(summary = "【商家】获取销售趋势图收藏列表数据")
	@GetMapping("/trend/favorite")
	public R<List<ProductDataSaleFavoriteDTO>> getShopSaleFavoritePage(ProductDataSaleTrendQuery saleTrendQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		saleTrendQuery.setShopId(shopId);
		return R.ok(productCountService.getShopSaleFavoritePage(saleTrendQuery));
	}

	@Operation(summary = "【商家】获取销售趋势图收藏列表数据")
	@GetMapping("/trend/favorite/excel")
	@ExportExcel(name = "趋势图收藏列表", sheet = "趋势图收藏列表")
	public List<ProductDataSaleFavoriteDTO> getShopSaleFavoritePageExcel(ProductDataSaleTrendQuery saleTrendQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		saleTrendQuery.setShopId(shopId);
		return productCountService.getShopSaleFavoritePage(saleTrendQuery);
	}
}
