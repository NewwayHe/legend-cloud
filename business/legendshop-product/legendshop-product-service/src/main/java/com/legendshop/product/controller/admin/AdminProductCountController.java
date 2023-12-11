/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.admin;


import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.product.dto.*;
import com.legendshop.product.query.*;
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
@RequestMapping(value = "/admin/count", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminProductCountController {

	@Autowired
	private ProductCountService productCountService;

	@Operation(summary = "【后台】获取商品SPU统计")
	@GetMapping("/spu")
	public R<ProductDataSpuDTO> getSpuCount() {

		return R.ok(productCountService.getProductDataSpuCount());

	}

	@Operation(summary = "【后台】获取商品SKU统计")
	@GetMapping("/sku")
	public R<ProductDataSkuDTO> getSkuCount() {

		return R.ok(productCountService.getProductDataSkuCount());

	}

	@Operation(summary = "【后台】获取在售商品SKU统计")
	@GetMapping("/sku/sale")
	public R<ProductDataSkuOnSaleDTO> getSkuOnSaleCount() {

		return R.ok(productCountService.getProductDataSkuOnSaleCount());

	}

	@Operation(summary = "【后台】获取商品SPU访问统计")
	@GetMapping("/spu/click")
	public R<ProductDataSpuClickDTO> getSpuClickCount() {

		return R.ok(productCountService.getProductDataSpuClickCount());

	}

	@Operation(summary = "【后台】获取商品SKU分类排行信息")
	@GetMapping("/sku/category")
	public R<List<ProductDataSkuByCategoryDTO>> getProductSkuByCategory() {

		return R.ok(productCountService.getProductSkuByCategory());
	}

	@Operation(summary = "【后台】获取店铺list")
	@GetMapping("/shop/list")
	public R<List<ProductDataShopDTO>> getShopList() {

		return R.ok(productCountService.getShopList());
	}

	@Operation(summary = "【后台】获取商品list")
	@GetMapping("/good/list")
	public R<List<ProductDataGoodDTO>> getGoodList(Long shopId) {
		return R.ok(productCountService.getGoodList(shopId));
	}

	@Operation(summary = "【后台】获取店铺商品概况")
	@GetMapping("/shop/detail")
	public R<ProductDataGoodsByShopDTO> getShopGoodsCount(Long id, String startDate, String endDate) {

		return R.ok(productCountService.getShopGoodsCount(id, startDate, endDate));
	}

	@Operation(summary = "【后台】获取商品访问折线图")
	@GetMapping("/shop/line")
	public R<List<ProductDataViewLineDTO>> getProductViewLine(ProductDataViewQuery viewQuery) {
		return R.ok(productCountService.getProductViewLine(viewQuery));
	}

	@Operation(summary = "【后台】获取商品访问概况分页")
	@GetMapping("/shop/page")
	public R<PageSupport<AdminProductDataViewPageDTO>> getProductViewPage(ProductDataViewQuery viewQuery) {
		return R.ok(productCountService.getAdminProductViewPage(viewQuery));
	}

	@Operation(summary = "【后台】导出excel商品访问概况分页")
	@GetMapping("/shop/page/excel")
	@ExportExcel(name = "商品访问概况", sheet = "商品访问概况")
	public List<AdminProductDataViewPageDTO> getProductViewPageExcel(ProductDataViewQuery viewQuery) {
		return productCountService.getAdminProductViewPageExcel(viewQuery);
	}

	@Operation(summary = "【后台】获取饼状图分类树")
	@GetMapping("/category/tree")
	public R<List<ProductDataCategoryDTO>> getCategoryTree(ProductDataCategoryQuery categoryQuery) {
		return R.ok(productCountService.getCategoryTree(categoryQuery));
	}

	@Operation(summary = "【后台】获取饼状图分类列表")
	@GetMapping("/category/list")
	public R<List<ProductDataCategoryDTO>> getCategory(ProductDataCategoryQuery categoryQuery) {
		return R.ok(productCountService.getCategory(categoryQuery));
	}

	@Operation(summary = "【后台】获取商品类目概况分页数据")
	@GetMapping("/category/page")
	public R<PageSupport<ProductDataCategoryPageDTO>> getCategoryDetailPage(ProductDataCategoryQuery categoryQuery) {
		return R.ok(productCountService.getCategoryDetailPage(categoryQuery));
	}

	@Operation(summary = "【后台】导出Excel商品类目概况分页数据")
	@GetMapping("/category/page/excel")
	@ExportExcel(name = "商品类目概况", sheet = "商品类目概况")
	public List<ProductDataCategoryPageDTO> getCategoryDetailPageExcel(ProductDataCategoryQuery categoryQuery) {

		return productCountService.getCategoryDetailPageExcel(categoryQuery);
	}


	@Operation(summary = "【后台】获取商品统计搜索概况数据")
	@GetMapping("/search/data")
	public R<List<ProductDataSearchDTO>> getProductSearchData(String source) {

		return R.ok(productCountService.getProductSearchData(source));
	}

	@Operation(summary = "【后台】获取搜索概况折线图数据")
	@GetMapping("/search/line")
	public R<List<ProductDataSearchPicDTO>> getSearchDetailLine(ProductDataSearchPicQuery searchPicQuery) {

		return R.ok(productCountService.getSearchLine(searchPicQuery));
	}

	@Operation(summary = "【后台】获取搜索概况详情数据")
	@GetMapping("/search/page")
	public R<PageSupport<ProductDataSearchPicDTO>> getSearchDetailPage(ProductDataSearchPicQuery searchPicQuery) {

		return R.ok(productCountService.getSearchDetailPage(searchPicQuery));
	}

	@Operation(summary = "【后台】导出excel搜索概况详情数据")
	@GetMapping("/search/page/excel")
	@ExportExcel(name = "搜索概况", sheet = "搜索概况")
	public List<ProductDataSearchPicDTO> getSearchDetailPageExcel(ProductDataSearchPicQuery searchPicQuery) {

		return productCountService.getSearchDetailPageExcel(searchPicQuery);
	}

	@Operation(summary = "【后台】获取销售排行分页数据")
	@GetMapping("/sale/order")
	public R<PageSupport<AdminProductDataShopSaleDTO>> getShopSaleDetailPage(ProductDataSaleQuery saleQuery) {
		return R.ok(productCountService.getAdminShopSaleDetailPage(saleQuery));
	}

	@Operation(summary = "【后台】导出excel销售排行分页数据")
	@GetMapping("/sale/order/excel")
	@ExportExcel(name = "销售排行", sheet = "销售排行")
	public List<AdminProductDataShopSaleDTO> getShopSaleDetailPageExcel(ProductDataSaleQuery saleQuery) {
		return productCountService.getAdminShopSaleDetailPageExcel(saleQuery);
	}

	@Operation(summary = "【后台】获取销售趋势图折线图数据")
	@GetMapping("/trend/line")
	public R<List<ProductDataGoodSaleDTO>> getShopSaleTrendLine(ProductDataSaleTrendQuery saleTrendQuery) {
		return R.ok(productCountService.getShopSaleTrendLine(saleTrendQuery));
	}

	@Operation(summary = "【后台】获取销售趋势图成交列表数据")
	@GetMapping("/trend/deal")
	public R<List<ProductDataSaleDealDTO>> getShopSaleDealPage(ProductDataSaleTrendQuery saleTrendQuery) {
		return R.ok(productCountService.getShopSaleDealPage(saleTrendQuery));
	}

	@Operation(summary = "【后台】导出excel销售趋势图成交列表数据")
	@GetMapping("/trend/deal/excel")
	@ExportExcel(name = "趋势图成交列表", sheet = "趋势图成交列表")
	public List<ProductDataSaleDealDTO> getShopSaleDealPageExcel(ProductDataSaleTrendQuery saleTrendQuery) {
		return productCountService.getShopSaleDealPage(saleTrendQuery);
	}

	@Operation(summary = "【后台】获取销售趋势图访问列表数据")
	@GetMapping("/trend/view")
	public R<List<ProductDataSaleViewDTO>> getShopSaleViewPage(ProductDataSaleTrendQuery saleTrendQuery) {
		return R.ok(productCountService.getShopSaleViewPage(saleTrendQuery));
	}

	@Operation(summary = "【后台】导出excel销售趋势图访问列表数据")
	@GetMapping("/trend/view/excel")
	@ExportExcel(name = "趋势图访问列表", sheet = "趋势图访问列表")
	public List<ProductDataSaleViewDTO> getShopSaleViewPageExcel(ProductDataSaleTrendQuery saleTrendQuery) {
		return productCountService.getShopSaleViewPage(saleTrendQuery);
	}

	@Operation(summary = "【后台】获取销售趋势图收藏列表数据")
	@GetMapping("/trend/favorite")
	public R<List<ProductDataSaleFavoriteDTO>> getShopSaleFavoritePage(ProductDataSaleTrendQuery saleTrendQuery) {
		return R.ok(productCountService.getShopSaleFavoritePage(saleTrendQuery));
	}

	@Operation(summary = "【后台】获取销售趋势图收藏列表数据")
	@GetMapping("/trend/favorite/excel")
	@ExportExcel(name = "趋势图收藏列表", sheet = "趋势图收藏列表")
	public List<ProductDataSaleFavoriteDTO> getShopSaleFavoritePageExcel(ProductDataSaleTrendQuery saleTrendQuery) {
		return productCountService.getShopSaleFavoritePage(saleTrendQuery);
	}
}
