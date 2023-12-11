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
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.product.bo.HotSellProductBO;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.bo.ProductPlatformBO;
import com.legendshop.product.dto.InventoryOperationsDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.entity.Product;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.shop.dto.ShopDecorateProductQuery;

import java.util.Date;
import java.util.List;

/**
 * 商品Dao
 *
 * @author legendshop
 */
public interface ProductDao extends GenericDao<Product, Long> {


	/**
	 * 更新商品评论得分和评论数的方法。
	 *
	 * @param scores    评论得分（累加）
	 * @param comments  评论数（直接更新）
	 * @param productId 商品ID
	 */
	void updateReviewScoresAndComments(Integer scores, Integer comments, Long productId);

	/**
	 * 更改商品状态的方法。
	 *
	 * @param status     商品状态
	 * @param productIds 商品ID列表
	 * @return 更新操作影响的行数
	 */
	int updateStatus(Integer status, List<Long> productIds);

	/**
	 * 更新商品库存的方法。
	 *
	 * @param productId 商品ID
	 */
	void updateProdStocks(Long productId);

	/**
	 * 批量更新商品库存的方法。
	 *
	 * @param prodIds 商品ID列表
	 */
	void batchUpdateProdStocks(List<Long> prodIds);

	/**
	 * 更新商品实际库存的方法。
	 *
	 * @param productId 商品ID
	 */
	void updateProdActualStocks(Long productId);

	/**
	 * 获取商品分页对象的方法。
	 *
	 * @param productQuery 商品查询对象
	 * @return 商品分页对象
	 */
	PageSupport<ProductBO> getProductPage(ProductQuery productQuery);

	/**
	 * 获取商品列表的方法。
	 *
	 * @param productQuery 商品查询对象
	 * @return 商品列表
	 */
	List<ProductBO> getProductList(ProductQuery productQuery);


	/**
	 * 商品管理  出售中的商品的商品导出
	 *
	 * @param productQuery
	 * @return
	 */
	List<ProductPlatformBO> getProductPlatformList(ProductQuery productQuery);

	/**
	 * 更新产品所有库存信息
	 *
	 * @param productId 产品ID
	 */
	void updateProductAllStocks(Long productId);

	/**
	 * 获取最近喜爱的产品
	 *
	 * @param amount 获取产品数量
	 * @param userId 用户ID
	 * @return 最近喜爱的产品列表
	 */
	List<Product> getRencFavorProduct(Integer amount, Long userId);

	/**
	 * 分页查询产品列表
	 *
	 * @param productQuery 产品查询条件
	 * @return 分页结果
	 */
	PageSupport<Product> queryProductListPage(ProductQuery productQuery);

	/**
	 * 分页查询商店装饰产品列表
	 *
	 * @param shopDecorateProductQuery 商店装饰产品查询条件
	 * @return 分页结果
	 */
	PageSupport<Product> queryProductListPage(ShopDecorateProductQuery shopDecorateProductQuery);

	/**
	 * 查询在线产品列表
	 *
	 * @param query 产品查询条件
	 * @return 在线产品分页结果
	 */
	PageSupport<Product> queryProductOnline(ProductQuery query);

	PageSupport<ProductDTO> queryProductOnlineEs(ProductQuery query);

	/**
	 * 批量更新审核状态
	 *
	 * @param idList   ID列表
	 * @param opStatus 审核状态
	 * @return 更新的记录数量
	 */
	int updateOpStatus(List<Long> idList, Integer opStatus);

	/**
	 * 根据店铺ID查询热销商品列表的方法。
	 *
	 * @param shopId 店铺ID
	 * @return 热销商品列表
	 */
	List<HotSellProductBO> queryHotSellProductByShopId(Long shopId);


	/**
	 * 根据商品id 获取 商品详情
	 *
	 * @param productId
	 * @return
	 */
	ProductBO getProductBO(Long productId);


	/**
	 * 根据商品id 获取 商品详情
	 *
	 * @param productIds
	 * @return
	 */
	List<ProductBO> getProductBO(List<Long> productIds);

	/**
	 * 批量更新库存
	 *
	 * @param ids
	 * @param stock
	 * @return
	 */
	Integer batchUpdateStock(List<Long> ids, Integer stock);

	/**
	 * 更新库存前检查更新后实际库存是小于0，返回小于0的id集合
	 *
	 * @param ids
	 * @param stock
	 * @return
	 */
	List<Long> batchCheckStock(List<Long> ids, Integer stock);


	/**
	 * 查看该运费模板被应用的数量
	 *
	 * @param transId
	 * @return
	 */
	Long getCountByTransId(Long transId);

	/**
	 * 根据ID集合获取商品列表
	 *
	 * @param productIds ID集合
	 * @param pageSize   数量
	 * @param orderBy    排序
	 * @return
	 */
	List<Product> getProductListByIds(List<Long> productIds, Integer pageSize, String orderBy);

	/**
	 * 根据商品ID获取在线商品的方法。
	 *
	 * @param productId 商品ID
	 * @return 在线商品DTO
	 */
	ProductDTO getOnlineById(Long productId);

	/**
	 * 计算每个商品的购买数
	 *
	 * @param numberList
	 * @return
	 */
	List<ProductDTO> calculateBuys(List<String> numberList);

	/**
	 * 更新商品购买数量的方法。
	 *
	 * @param id   商品ID
	 * @param buys 新的购买数量
	 * @return 更新是否成功的布尔值
	 */
	Boolean updateBuys(Long id, Integer buys);

	/**
	 * 更新商品浏览数
	 *
	 * @param productId
	 */
	void updateProductViews(Long productId);

	/**
	 * 更新商品配送类型的方法。
	 *
	 * @param deliveryType 配送类型
	 * @param productId    商品ID
	 */
	void updateProductDeliveryType(Integer deliveryType, Long productId);

	/**
	 * 查询店铺下线的商品ID列表的方法。
	 *
	 * @param shopIds 店铺ID列表
	 * @return 下线商品的ID列表
	 */
	List<Long> queryIdByShopId(List<Long> shopIds);

	/**
	 * 通过商品id和对应审核状态统计商品的数量
	 *
	 * @param shopId
	 * @param opStatusEnum
	 * @return
	 */
	Long getProductCountByShopId(Long shopId, OpStatusEnum opStatusEnum);

	/**
	 * 更新删除状态
	 *
	 * @param delStatus
	 * @param productList
	 */
	void updateDelStatus(Integer delStatus, List<Product> productList);


	/**
	 * 根据商品ID更新商品库存信息（包括购物车数量）的方法。
	 *
	 * @param id          商品ID
	 * @param basketCount 购物车数量
	 * @param shopId      店铺ID
	 * @return 更新是否成功的整数值
	 */
	Integer updateStocksByProductId(Long id, Integer basketCount, Long shopId);
	/**
	 * 减库存
	 *
	 * @param productId
	 * @param stock
	 * @return
	 */
	boolean reduceStock(Long productId, Integer stock);

	/**
	 * 加回库存
	 *
	 * @param productId
	 * @param stock
	 * @return
	 */
	boolean addBackStock(Long productId, Integer stock);

	/**
	 * 新增发布商品数量
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return Integer-新增发布商品数量
	 */
	Integer getNewProduct(Date startDate, Date endDate);

	/**
	 * 新增商品订单数量
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return Integer-新增商品订单数量
	 */
	Integer getNewOrder(Date startDate, Date endDate);

	/**
	 * 新增售后订单数量
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return Integer-新增售后订单数量
	 */
	Integer getNewRefundOrder(Date startDate, Date endDate);

	/**
	 * 新增举报商品数量
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return Integer-新增举报商品数量
	 */
	Integer getNewAccusationProduct(Date startDate, Date endDate);

	/**
	 * 新增商品咨詢数量
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return Integer-新增商品咨詢数量
	 */
	Integer getNewReferProduct(Date startDate, Date endDate);

	/**
	 * 后台分页查找
	 *
	 * @param productQuery
	 * @return
	 */
	PageSupport<ProductBO> getPage(ProductQuery productQuery);

	/**
	 * 根据skuId获取商品
	 *
	 * @param skuIdList
	 * @return
	 */
	List<Product> queryProductBySkuIdList(List<Long> skuIdList);

	/**
	 * 更新商品和审核备注信息
	 *
	 * @param idList
	 * @param opStatus
	 * @param auditOpinion
	 * @return
	 */
	Integer updateOpStatusAndAuditOpinion(List<Long> idList, Integer opStatus, String auditOpinion);


	/**
	 * 商品下架
	 *
	 * @param productIds
	 * @param opStatus
	 * @return
	 */
	Integer illegalOffProduct(List<Long> productIds, Integer opStatus);

	/**
	 * 更新实际商品库存的方法。
	 *
	 * @param actualList 实际库存操作DTO列表
	 * @return 更新操作结果数组
	 */
	int[] updateActualProductStocks(List<InventoryOperationsDTO> actualList);

	/**
	 * 根据商品Id获取商品
	 *
	 * @param productId
	 * @return
	 */
	Product getProductOnLineEsByProductId(Long productId);

	/**
	 * 根据商品Id获取商品
	 *
	 * @param productIds
	 * @return
	 */
	List<Product> queryProductOnLineEsByProductId(List<Long> productIds);

	/**
	 * 审核分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<ProductBO> auditPage(ProductQuery query);

	/**
	 * 查找在线的商品数量
	 * @return
	 */
	Long getIndexCount();

	/**
	 * 根据品牌id 获得商品数量
	 *
	 * @param id
	 * @return
	 */
	List<Product> getBrandId(Long id);

	List<Long> getAdamin();

	/**
	 * 根据商品名称获取商品的方法。
	 *
	 * @param productName 商品名称
	 * @return 商品对象
	 */
	Product getProduct(String productName);
}
