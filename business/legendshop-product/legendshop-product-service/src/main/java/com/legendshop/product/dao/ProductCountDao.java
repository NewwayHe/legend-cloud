/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.datasource.NonTable;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.dto.*;
import com.legendshop.product.query.*;

import java.util.Date;
import java.util.List;

/**
 * 商品数量统计报表Dao
 *
 * @author legendshop
 */
public interface ProductCountDao extends GenericDao<NonTable, Long> {

	/**
	 * 根据时间范围查询商品SPU数量
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param shopId
	 * @return
	 */
	Integer queryProdSpuCount(Date startDate, Date endDate, Long shopId);

	/**
	 * 根据时间范围查询商品SKU数量
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param shopId
	 * @return 商品SKU数量
	 */
	Integer queryProdSkuCount(Date startDate, Date endDate, Long shopId);

	/**
	 * 根据时间范围查询在售商品SKU数量
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param shopId
	 * @return 商品SKU数量
	 */
	Integer queryProdSkuSaleCount(Date startDate, Date endDate, Long shopId);

	/**
	 * 根据时间范围查询商品SPU访问数量
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param shopId
	 * @return 访问数量
	 */
	Integer queryProdViews(Date startDate, Date endDate, Long shopId);


	/**
	 * 查询分类下商品SKU总数的排序
	 *
	 * @param endDate 前一天23点59分59秒
	 * @param shopId
	 * @return
	 */
	List<ProductDataSkuByCategoryDTO> queryProdSkuByCategory(Date endDate, Long shopId);

	/**
	 * 查询店铺分类下商品SKU总数的排序
	 *
	 * @param endDate 前一天23点59分59秒
	 * @param shopId
	 * @return
	 */
	List<ProductDataSkuByCategoryDTO> queryShopProdSkuByCategory(Date endDate, Long shopId);

	/**
	 * 查询所有上线的店铺
	 *
	 * @return 所有上线的店铺
	 */
	List<ProductDataShopDTO> queryShopList();

	/**
	 * 查询所有未删除的商品
	 * @param shopId
	 * @return 所有未删除的商品
	 */
	List<ProductDataGoodDTO> queryGoodList(Long shopId);

	/**
	 * 根据店铺id和时间范围获取商品概况
	 *
	 * @param id        商家id
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return 商品概况
	 */
	ProductDataGoodsByShopDTO queryShopGoodsCount(Long id, Date startDate, Date endDate);

	/**
	 * 查询商品访问折线图数据 商品id必须存在
	 *
	 * @param viewQuery
	 * @return 折线图数据
	 */
	ProductDataViewLineDTO queryProductViewLineByProdId(ProductDataViewQuery viewQuery);

	/**
	 * 查询商品访问折线图数据 店铺id存在，商品id不存在
	 *
	 * @param viewQuery
	 * @return 折线图数据
	 */
	ProductDataViewLineDTO queryProductViewLineByShopId(ProductDataViewQuery viewQuery);

	/**
	 * 查询商品访问折线图数据 店铺id和商品id不存在
	 *
	 * @param viewQuery
	 * @return 折线图数据
	 */
	ProductDataViewLineDTO queryProductViewLineAll(ProductDataViewQuery viewQuery);

	/**
	 * 查询商品访问分页数据
	 *
	 * @param viewQuery 查询条件
	 * @return 分页数据
	 */
	PageSupport<ProductDataViewPageDTO> getProductViewPage(ProductDataViewQuery viewQuery);

	/**
	 * 查询商品访问分页数据Excel
	 *
	 * @param viewQuery 查询条件
	 * @return 分页数据
	 */
	List<ProductDataViewPageDTO> getProductViewPageExcel(ProductDataViewQuery viewQuery);

	PageSupport<AdminProductDataViewPageDTO> getAdminProductViewPage(ProductDataViewQuery viewQuery);

	List<AdminProductDataViewPageDTO> getAdminProductViewPageExcel(ProductDataViewQuery viewQuery);

	/**
	 * 根据类目名模糊查询类目
	 *
	 * @param name
	 * @return 类目
	 */
	List<CategoryBO> queryCategoryByName(String name);

	/**
	 * 根据类目名模糊查询类目
	 *
	 * @param query
	 * @return 类目
	 */
	List<CategoryBO> queryCategory(ProductDataCategoryQuery query);

	/**
	 * 根据类目id查询类目
	 *
	 * @param id
	 * @return
	 */
	List<CategoryBO> queryCategoryById(Long id);


	/**
	 * 查询所有顶层分类节点的方法。
	 *
	 * @param num   父节点ID
	 * @param query 分类查询对象
	 * @return 商品数据分类DTO
	 */
	ProductDataCategoryDTO queryTopCategory(Long num, ProductDataCategoryQuery query);

	/**
	 * 查询所有顶层分类节点的方法。
	 *
	 * @param query 分类查询对象
	 * @return 商品数据分类DTO列表
	 */
	List<ProductDataCategoryDTO> queryTopCategory(ProductDataCategoryQuery query);

	/**
	 * 根据顶层分类节点查询二级分类节点的方法。
	 *
	 * @param num   父节点ID
	 * @param query 分类查询对象
	 * @return 商品数据分类DTO列表
	 */
	List<ProductDataCategoryDTO> querySecondCategory(Long num, ProductDataCategoryQuery query);

	/**
	 * 根据顶层分类节点查询二级分类节点的方法。
	 *
	 * @param num   分类节点ID
	 * @param query 分类查询对象
	 * @return 商品数据分类DTO
	 */
	ProductDataCategoryDTO getSecondCategory(Long num, ProductDataCategoryQuery query);

	/**
	 * 根据二级分类节点查询三级分类节点的方法。
	 *
	 * @param num   父节点ID
	 * @param query 分类查询对象
	 * @return 商品数据分类DTO列表
	 */
	List<ProductDataCategoryDTO> queryThirdCategory(Long num, ProductDataCategoryQuery query);

	/**
	 * 根据二级分类节点查询三级分类节点的方法。
	 *
	 * @param num   分类节点ID
	 * @param query 分类查询对象
	 * @return 商品数据分类DTO
	 */
	ProductDataCategoryDTO getThirdCategory(Long num, ProductDataCategoryQuery query);


	/**
	 * 查询商品类目概况详细分页
	 *
	 * @param categoryQuery 查询参数
	 * @return
	 */
	PageSupport<ProductDataCategoryPageDTO> queryCategoryDetailPage(ProductDataCategoryQuery categoryQuery);

	/**
	 * 查询商品类目概况详细分页Excel
	 *
	 * @param categoryQuery 查询参数
	 * @return
	 */
	List<ProductDataCategoryPageDTO> queryCategoryDetailPageExcel(ProductDataCategoryQuery categoryQuery);

	/**
	 * 根据终端类型查询商品统计搜索概况
	 *
	 * @param terminal
	 * @return
	 */
	List<ProductDataSearchDTO> querySearchData(String terminal);

	/**
	 * 获取商品搜索折线图
	 *
	 * @param searchPicQuery 查询参数
	 * @return
	 */
	List<ProductDataSearchPicDTO> getSearchLine(ProductDataSearchPicQuery searchPicQuery);


	/**
	 * 获取当前搜索数据
	 *
	 * @param searchPicQuery
	 * @return
	 */
	PageSupport<ProductDataSearchPicDTO> getSearchDetailPicture(ProductDataSearchPicQuery searchPicQuery);

	/**
	 * 获取当前搜索数据Excel
	 *
	 * @param searchPicQuery
	 * @return
	 */
	List<ProductDataSearchPicDTO> getSearchDetailPictureExcel(ProductDataSearchPicQuery searchPicQuery);

	/**
	 * 根据关键字查询同比搜索数据
	 *
	 * @param searchPicQuery
	 * @return
	 */
	Integer getSearchDetailLastYear(ProductDataSearchPicQuery searchPicQuery);

	/**
	 * 根据关键字查询环比搜索数据
	 *
	 * @param searchPicQuery
	 * @return
	 */
	Integer getSearchDetailLastMonth(ProductDataSearchPicQuery searchPicQuery);

	/**
	 * 查询商品销售排行分页
	 *
	 * @param productDataSaleQuery
	 * @return
	 */
	PageSupport<ProductDataShopSaleDTO> getShopSaleDetailPage(ProductDataSaleQuery productDataSaleQuery);

	/**
	 * 查询商品销售排行分页Excel
	 *
	 * @param productDataSaleQuery
	 * @return
	 */
	List<ProductDataShopSaleDTO> getShopSaleDetailPageExcel(ProductDataSaleQuery productDataSaleQuery);

	/**
	 * 查询商品销售排行分页
	 *
	 * @param productDataSaleQuery
	 * @return
	 */
	PageSupport<AdminProductDataShopSaleDTO> getAdminShopSaleDetailPage(ProductDataSaleQuery productDataSaleQuery);

	/**
	 * 查询商品销售排行分页Excel
	 *
	 * @param productDataSaleQuery
	 * @return
	 */
	List<AdminProductDataShopSaleDTO> getAdminShopSaleDetailPageExcel(ProductDataSaleQuery productDataSaleQuery);

	/**
	 * 查询商品销售趋势图
	 *
	 * @param trendQuery
	 * @return
	 */
	ProductDataGoodSaleDTO getShopSaleTrendLine(ProductDataSaleTrendQuery trendQuery);

	/**
	 * 获取商品销售趋势分页
	 *
	 * @param trendQuery
	 * @return
	 */
	PageSupport<ProductDataSaleDealDTO> getShopSaleDealPage(ProductDataSaleTrendQuery trendQuery);

	/**
	 * 获取商品销售趋势
	 *
	 * @param trendQuery
	 * @return
	 */
	List<ProductDataSaleDealDTO> getShopSaleDealList(ProductDataSaleTrendQuery trendQuery);

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

	/**
	 * 获取店铺一级类目列表
	 *
	 * @param query
	 * @return
	 */
	List<ProductDataCategoryDTO> queryShopTopCategory(ProductDataCategoryQuery query);

	/**
	 * 获取店铺一级类目列表
	 * @param num
	 * @param query
	 * @return
	 */
	ProductDataCategoryDTO queryShopTopCategory(Long num, ProductDataCategoryQuery query);

	/**
	 * 获取店铺二级类目列表
	 * @param num
	 * @param query
	 * @return
	 */
	List<ProductDataCategoryDTO> queryShopSecondCategory(Long num, ProductDataCategoryQuery query);

	/**
	 * 获取店铺三级类目列表
	 * @param num
	 * @param query
	 * @return
	 */
	List<ProductDataCategoryDTO> queryShopThirdCategory(Long num, ProductDataCategoryQuery query);

	/**
	 * 获取店铺类目分页
	 *
	 * @param categoryQuery
	 * @return
	 */
	PageSupport<ProductDataCategoryPageDTO> queryShopCategoryDetailPage(ProductDataCategoryQuery categoryQuery);

	/**
	 * 导出店铺类目信息
	 *
	 * @param categoryQuery
	 * @return
	 */
	List<ProductDataCategoryPageDTO> queryShopCategoryDetailPageExcel(ProductDataCategoryQuery categoryQuery);

	/**
	 * 商家类目根据类目名称搜索
	 *
	 * @param query
	 * @return
	 */
	List<CategoryBO> queryShopCategoryByName(ProductDataCategoryQuery query);

	/**
	 * 根据类目id获取类目列表
	 *
	 * @param id
	 * @param shopId
	 * @return
	 */
	List<CategoryBO> queryShopCategoryById(Long id, Long shopId);
}
