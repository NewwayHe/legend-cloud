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
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.*;
import com.legendshop.product.dto.*;
import com.legendshop.product.excel.ProductExportDTO;
import com.legendshop.product.excel.ProductRecycleBinExportDTO;
import com.legendshop.product.excel.StockExportDTO;
import com.legendshop.product.query.ProductDetailQuery;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.query.SkuQuery;
import com.legendshop.shop.dto.ShopDecorateProductQuery;

import java.util.List;

/**
 * 产品服务类接口.
 *
 * @author legendshop
 */
public interface ProductService {

	/**
	 * 根据商品ID获取商品信息的方法。
	 *
	 * @param productId 商品ID
	 * @return 商品DTO对象
	 */
	ProductDTO getDtoByProductId(Long productId);

	/**
	 * 根据商品ID获取在线商品信息的方法。
	 *
	 * @param productId 商品ID
	 * @return 在线商品DTO对象
	 */
	ProductDTO getOnlineDtoByProductId(Long productId);

	/**
	 * 根据商品id 获取 商品详情
	 *
	 * @param query
	 * @return
	 */
	R<ProductBO> views(ProductDetailQuery query);

	/**
	 * 根据商品id 获取 商品基本信息
	 *
	 * @param productId
	 * @return
	 */
	R<ProductBO> getBoByProductId(Long productId);


	/**
	 * 获取最近收藏的商品列表。
	 *
	 * @param amount  获取的商品数量
	 * @param userId  用户ID
	 * @return 最近收藏的商品列表
	 */
	List<ProductBO> getRencFavorProd(Integer amount, Long userId);

	/**
	 * 查询符合条件的商品导出列表。
	 *
	 * @param query 商品查询条件
	 * @return 符合条件的商品导出DTO列表
	 */
	List<ProductExportDTO> findExportProd(ProductQuery query);

	/**
	 * 处理商品违规下架。
	 *
	 * @param productIds 商品ID列表
	 * @param illegalOff 违规下架标识
	 * @param auditDTO   审核DTO对象
	 */
	void illegalOffProduct(List<Long> productIds, Integer illegalOff, AuditDTO auditDTO);

	/**
	 * 通用分页查询产品列表的方法。
	 *
	 * @param productQuery 产品查询对象
	 * @return 包含产品信息的分页支持对象
	 */
	PageSupport<ProductBO> queryProductListPage(ProductQuery productQuery);

	/**
	 * 获取产品分页列表的方法。
	 *
	 * @param productQuery 产品查询对象
	 * @return 包含产品信息的分页支持对象
	 */
	PageSupport<ProductBO> getPage(ProductQuery productQuery);

	/**
	 * 查看商品列表 - 带出商品营销活动信息，营销活动选择商品的弹框页面
	 *
	 * @param productQuery
	 * @return
	 */
	PageSupport<ProductBO> queryActivityInfoPage(ProductQuery productQuery);

	/**
	 * 查看商品列表 - 带出商品营销活动信息，积分商品活动选择sku的弹框页面
	 *
	 * @param productQuery
	 * @return
	 */
	PageSupport<ProductBO> queryIntegralInfoPage(ProductQuery productQuery);

	PageSupport<ProductDTO> queryProductOnLine(ProductQuery query);

	/**
	 * 获取es需要的商品列表
	 *
	 * @param productId
	 * @return
	 */
	ProductDTO getProductOnLineEsByProductId(Long productId);

	List<ProductDTO> queryProductOnLineEsByProductId(List<Long> productIds);

	PageSupport<ProductDTO> queryProductOnLineEs(ProductQuery query);

	/**
	 * 批量更新商品状态的方法。
	 *
	 * @param productDTO 包含商品信息的数据传输对象
	 * @return 批量更新商品状态的结果
	 */
	R batchUpdateStatus(ProductBranchDTO productDTO);

	/**
	 * 审核商品的方法。
	 *
	 * @param auditDTO 包含审核信息的数据传输对象
	 * @return 审核操作的结果
	 */
	R audit(AuditDTO auditDTO);

	/**
	 * 保存商品信息的方法。
	 *
	 * @param productDTO 包含商品信息的数据传输对象
	 * @return 保存商品操作的结果
	 */
	R save(ProductDTO productDTO);


	/**
	 * 更新商品
	 * {@see com.legendshop.product.service.DraftProductService#update(com.legendshop.product.dto.ProductDTO)}
	 *
	 * @param productDTO
	 * @return
	 */
	R update(ProductDTO productDTO);

	/**
	 * 获取店铺热销前三的商品列表。
	 *
	 * @param shopId 店铺 ID
	 * @return 店铺热销前三的商品列表
	 */
	List<HotSellProductBO> queryHotSellProdByShopId(Long shopId);


	/**
	 * 获取商家端指定商品详情
	 *
	 * @param id     商品 ID
	 * @param shopId 商家 ID
	 * @return 包含商家端商品详情的数据传输对象
	 */
	ProductDTO getShopProductById(Long id, Long shopId);


	/**
	 * 获取装修商品列表
	 *
	 * @param productQuery
	 * @return
	 */
	PageSupport<DecorateProductBO> queryDecorateProductList(ProductQuery productQuery);

	/**
	 * 获取装修商品列表
	 *
	 * @param shopDecorateProductQuery
	 * @return
	 */
	PageSupport<DecorateProductBO> queryDecorateProductList(ShopDecorateProductQuery shopDecorateProductQuery);

	/**
	 * 库存预警分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<SkuBO> stocksPage(ProductQuery query);

	/**
	 * 商品库存导出
	 *
	 * @param query
	 * @return
	 */
	List<StockExportDTO> stocksExport(ProductQuery query);

	/**
	 * 库存列表
	 *
	 * @param query
	 * @return
	 */
	List<SkuBO> stocksList(SkuQuery query);


	/**
	 * 根据skuIdList查询库存列表
	 *
	 * @param skuIdList
	 * @return
	 */
	List<SkuBO> queryStocksListBySkuIdList(List<Long> skuIdList);

	/**
	 * 矢量更新sku库存(更新sku库存)
	 *
	 * @param skuDTOList 库存数（会覆盖原有的销售库存数量,实际库存会根据新旧数据的差值进行增减）
	 * @return
	 */
	R batchUpdateSku(List<SkuUpdateSkuDTO> skuDTOList);

	/**
	 * 批量更新库存
	 *
	 * @param ids   skuId列表
	 * @param stock 库存数（会覆盖原有的销售库存数量,实际库存会根据新旧数据的差值进行增减）
	 * @return
	 */
	R batchUpdateStock(List<Long> ids, Integer stock);

	/**
	 * 根据商品idList刷新spu库存
	 *
	 * @param productIdList
	 * @return
	 */
	int updateProductStock(List<Long> productIdList);

	/**
	 * 根据id查询所有的商品
	 *
	 * @param productId
	 * @return
	 */
	List<ProductDTO> queryAllByIds(List<Long> productId);

	/**
	 * 查看该运费模板被应用的数量
	 *
	 * @param transId
	 * @return
	 */
	Long getCountByTransId(Long transId);

	/**
	 * 商品到点预约上架
	 *
	 * @param productId
	 */
	void appointOnline(Long productId);

	/**
	 * 根据商品分组ID获取商品列表
	 *
	 * @param productGroupId
	 * @param pageSize
	 * @return
	 */
	List<ProductDTO> getProductListByGroupId(Long productGroupId, Integer pageSize);

	/**
	 * 下线店铺的商品
	 *
	 * @param ids
	 */
	void offLineByShopIds(List<Long> ids);

	/**
	 * 通过商品id和对应审核状态统计商品的数量
	 *
	 * @param shopId
	 * @param opStatusEnum
	 * @return
	 */
	Long getProductCountByShopId(Long shopId, OpStatusEnum opStatusEnum);


	/**
	 * 批量更新商品删除状态
	 *
	 * @param productDTO
	 * @param shopId
	 * @return
	 */
	R batchUpdateDelStatus(ProductBatchDelDTO productDTO, Long shopId);

	/**
	 * 商品违规查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<ProductAccusationBO> accusationPage(ProductQuery query);

	/**
	 * 根据商品ID和购物车数量更新商品库存。
	 *
	 * @param id          商品ID
	 * @param basketCount 购物车数量
	 * @param shopId      商店ID
	 * @return 更新的结果（影响的行数）
	 */
	Integer updateStocksByProductId(Long id, Integer basketCount, Long shopId);

	/**
	 * 【商家】首页库存预警分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<SkuBO> stocksArmPage(ProductQuery query);

	/**
	 * 【后台】首页商品新增相关信息
	 *
	 * @return ProductNewCountBO
	 */
	ProductNewCountBO getIndexInfo();


	/**
	 * 违规商品上架
	 *
	 * @param auditDTO
	 * @return
	 */
	R productOnline(AuditDTO auditDTO);

	/**
	 * 导出商品回收站信息
	 *
	 * @param query
	 * @return
	 */
	List<ProductRecycleBinExportDTO> findExportRecycleBinProd(ProductQuery query);

	/**
	 * 预售结束处理
	 *
	 * @param productId
	 */
	void preSellFinish(Long productId);

	/**
	 * 审核列表
	 *
	 * @param query
	 * @return
	 */
	PageSupport<ProductAuditBO> auditPage(ProductQuery query);

	PageSupport<ProductBO> getProductPage(ProductQuery productQuery);

	/**
	 * 根据skuId获取商品
	 *
	 * @param skuIdList
	 * @return
	 */
	R<List<ProductBO>> queryProductBySkuIdList(List<Long> skuIdList);


	/**
	 * 商品管理  出售中的商品的商品导出
	 *
	 * @param query
	 * @return
	 */
	List<ProductPlatformExportDTO> findExportPlatformProd(ProductQuery query);

	Long getIndexCount();

	/**
	 * 根据品牌id 获得商品数量
	 *
	 * @param id
	 * @return
	 */
	Long getBrandId(Long id);

	/**
	 * 批量编辑
	 *
	 * @param productDTOList
	 */
	void updateProductList(List<ProductDTO> productDTOList);

	/**
	 * 商品批量编辑
	 *
	 * @param productDTOList
	 * @return
	 */
	R batchEditProduct(List<ProductDTO> productDTOList);
}
