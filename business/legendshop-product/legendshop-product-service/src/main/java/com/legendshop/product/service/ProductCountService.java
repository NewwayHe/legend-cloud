/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.dto.*;
import com.legendshop.product.query.*;

import java.util.List;

/**
 * 商品统计服务
 *
 * @author legendshop
 */
public interface ProductCountService {


	/**
	 * 获取商品SPU统计数
	 *
	 * @return ProductDataSpuDTO
	 */
	ProductDataSpuDTO getProductDataSpuCount();

	/**
	 * 获取商品SKU统计数
	 *
	 * @return ProductDataSkuDTO
	 */
	ProductDataSkuDTO getProductDataSkuCount();

	/**
	 * 获取在售商品SKU统计数
	 *
	 * @return ProductDataSkuOnSaleDTO
	 */
	ProductDataSkuOnSaleDTO getProductDataSkuOnSaleCount();

	/**
	 * 获取商品SPU访问量
	 *
	 * @return
	 */
	ProductDataSpuClickDTO getProductDataSpuClickCount();


	/**
	 * 查询分类下商品SKU总数的排序
	 */
	List<ProductDataSkuByCategoryDTO> getProductSkuByCategory();

	/**
	 * 商品统计搜索概况
	 *
	 * @param resource 来源
	 * @return
	 */
	List<ProductDataSearchDTO> getProductSearchData(String resource);

	/**
	 * 获取店铺list
	 *
	 * @return
	 */
	List<ProductDataShopDTO> getShopList();

	/**
	 * 获取商品list
	 *
	 * @return
	 */
	List<ProductDataGoodDTO> getGoodList(Long shopId);


	/**
	 * 根据店铺id和时间范围获取商品概况
	 *
	 * @param id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	ProductDataGoodsByShopDTO getShopGoodsCount(Long id, String startDate, String endDate);

	/**
	 * 获取商品访问折线图数据
	 *
	 * @param viewQuery 查询条件
	 * @return
	 */
	List<ProductDataViewLineDTO> getProductViewLine(ProductDataViewQuery viewQuery);

	/**
	 * 获取商品访问分页数据
	 *
	 * @param viewQuery 查询条件
	 * @return
	 */
	PageSupport<ProductDataViewPageDTO> getProductViewPage(ProductDataViewQuery viewQuery);

	/**
	 * 获取商品访问分页数据Excel
	 *
	 * @param viewQuery 查询条件
	 * @return
	 */
	List<ProductDataViewPageDTO> getProductViewPageExcel(ProductDataViewQuery viewQuery);

	/**
	 * 获取管理后台商品数据视图的分页支持对象的方法。
	 *
	 * @param viewQuery 商品数据视图查询对象
	 * @return 商品数据视图的分页支持对象
	 */
	PageSupport<AdminProductDataViewPageDTO> getAdminProductViewPage(ProductDataViewQuery viewQuery);

	/**
	 * 获取管理后台商品数据视图的Excel数据的方法。
	 *
	 * @param viewQuery 商品数据视图查询对象
	 * @return 商品数据视图的Excel数据列表
	 */
	List<AdminProductDataViewPageDTO> getAdminProductViewPageExcel(ProductDataViewQuery viewQuery);

	/**
	 * 获取商品分类关系树的方法。
	 *
	 * @param query 商品分类查询对象
	 * @return 商品分类关系树列表
	 */
	List<ProductDataCategoryDTO> getCategoryTree(ProductDataCategoryQuery query);

	/**
	 * 获取商品分类列表的方法。
	 *
	 * @param categoryQuery 商品分类查询对象
	 * @return 商品分类列表
	 */
	List<ProductDataCategoryDTO> getCategory(ProductDataCategoryQuery categoryQuery);

	/**
	 * 获取商品类目概况详细分页
	 *
	 * @param categoryQuery 查询参数
	 * @return
	 */
	PageSupport<ProductDataCategoryPageDTO> getCategoryDetailPage(ProductDataCategoryQuery categoryQuery);

	/**
	 * 获取商品类目概况详细分页Excel
	 *
	 * @param categoryQuery 查询参数
	 * @return
	 */
	List<ProductDataCategoryPageDTO> getCategoryDetailPageExcel(ProductDataCategoryQuery categoryQuery);

	/**
	 * 获取商品搜索折线图
	 *
	 * @param searchPicQuery 查询参数
	 * @return
	 */
	List<ProductDataSearchPicDTO> getSearchLine(ProductDataSearchPicQuery searchPicQuery);

	/**
	 * 获取商品搜索概况
	 *
	 * @param searchPicQuery 查询参数
	 * @return
	 */
	PageSupport<ProductDataSearchPicDTO> getSearchDetailPage(ProductDataSearchPicQuery searchPicQuery);

	/**
	 * 获取商品搜索概况Excel
	 *
	 * @param searchPicQuery 查询参数
	 * @return
	 */
	List<ProductDataSearchPicDTO> getSearchDetailPageExcel(ProductDataSearchPicQuery searchPicQuery);

	/**
	 * 获取商家端商品SPU统计数
	 *
	 * @return ProductDataSpuDTO
	 */
	ProductDataSpuDTO getShopSpuCount(Long shopId);

	/**
	 * 获取商家端商品SKU统计数
	 *
	 * @return ProductDataSkuDTO
	 */
	ProductDataSkuDTO getShopSkuCount(Long shopId);

	/**
	 * 获取商家端在售商品SKU统计数
	 *
	 * @return ProductDataSkuOnSaleDTO
	 */
	ProductDataSkuOnSaleDTO getShopSkuOnSaleCount(Long shopId);

	/**
	 * 获取商家端商品SPU访问量
	 *
	 * @return
	 */
	ProductDataSpuClickDTO getShopSpuClickCount(Long shopId);

	/**
	 * 获取商家端分类下商品SKU总数的排序
	 */
	List<ProductDataSkuByCategoryDTO> getShopSkuByCategory(Long shopId);

	/**
	 * 获取商家端商品访问折线图数据
	 *
	 * @param viewQuery 查询条件
	 * @return
	 */
	List<ProductDataViewLineDTO> getShopViewLine(ProductDataViewQuery viewQuery);

	/**
	 * 获取商家端商品分类关系树
	 *
	 * @return
	 */
	List<ProductDataCategoryDTO> getShopCategoryTree(ProductDataCategoryQuery query);

	/**
	 * 获取商家端商品分类列表
	 *
	 * @param categoryQuery
	 * @return
	 */
	List<ProductDataCategoryDTO> getShopCategory(ProductDataCategoryQuery categoryQuery);

	/**
	 * 获取商品类目概况详细分页
	 *
	 * @param categoryQuery 查询参数
	 * @return
	 */
	PageSupport<ProductDataCategoryPageDTO> getShopCategoryDetailPage(ProductDataCategoryQuery categoryQuery);


	/**
	 * 获取商品类目概况详细分页Excel
	 *
	 * @param categoryQuery 查询参数
	 * @return
	 */
	List<ProductDataCategoryPageDTO> getShopCategoryDetailPageExcel(ProductDataCategoryQuery categoryQuery);

	/**
	 * 获取商品销售排行分页
	 *
	 * @param productDataSaleQuery
	 * @return
	 */
	PageSupport<ProductDataShopSaleDTO> getShopSaleDetailPage(ProductDataSaleQuery productDataSaleQuery);

	/**
	 * 获取商品销售排行分页Excel
	 *
	 * @param productDataSaleQuery
	 * @return
	 */
	List<ProductDataShopSaleDTO> getShopSaleDetailPageExcel(ProductDataSaleQuery productDataSaleQuery);

	/**
	 * 获取商品销售排行分页
	 *
	 * @param productDataSaleQuery
	 * @return
	 */
	PageSupport<AdminProductDataShopSaleDTO> getAdminShopSaleDetailPage(ProductDataSaleQuery productDataSaleQuery);

	/**
	 * 获取商品销售排行分页Excel
	 *
	 * @param productDataSaleQuery
	 * @return
	 */
	List<AdminProductDataShopSaleDTO> getAdminShopSaleDetailPageExcel(ProductDataSaleQuery productDataSaleQuery);

	/**
	 * 获取商品销售趋势图
	 *
	 * @param trendQuery
	 * @return
	 */
	List<ProductDataGoodSaleDTO> getShopSaleTrendLine(ProductDataSaleTrendQuery trendQuery);

	/**
	 * 获取商品销售趋势成交分页
	 *
	 * @param trendQuery
	 * @return
	 */
	List<ProductDataSaleDealDTO> getShopSaleDealPage(ProductDataSaleTrendQuery trendQuery);

	/**
	 * 获取商品销售趋势访问列表
	 *
	 * @param trendQuery
	 * @return
	 */
	List<ProductDataSaleViewDTO> getShopSaleViewPage(ProductDataSaleTrendQuery trendQuery);

	/**
	 * 获取商品销售趋势收藏列表
	 *
	 * @param trendQuery
	 * @return
	 */
	List<ProductDataSaleFavoriteDTO> getShopSaleFavoritePage(ProductDataSaleTrendQuery trendQuery);


}
