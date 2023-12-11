/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import com.legendshop.activity.api.CouponApi;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.enums.CouponStatusEnum;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.api.*;
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.bo.ProductPropertyValueBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.*;
import com.legendshop.product.enums.ProductOnSaleWayEnum;
import com.legendshop.product.enums.ProductPropertySourceEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.search.document.CouponDocument;
import com.legendshop.search.document.ProductDocument;
import com.legendshop.search.document.ShopDocument;
import com.legendshop.search.dto.EsIndexDocumentDTO;
import com.legendshop.search.dto.IndexCountDTO;
import com.legendshop.search.dto.IndexDocumentDTO;
import com.legendshop.search.dto.Tag;
import com.legendshop.search.enmus.IndexTypeEnum;
import com.legendshop.search.enmus.ProductTargetTypeEnum;
import com.legendshop.search.properties.SearchProperties;
import com.legendshop.search.repository.ProductRepository;
import com.legendshop.search.service.IndexService;
import com.legendshop.search.service.SearchProductIndexService;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.query.SearchShopQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 索引操作接口实现
 *
 * @author legendshop
 */
@Service
@Slf4j
@AllArgsConstructor
public class SearchProductIndexServiceImpl implements SearchProductIndexService, IndexService {

	private final SkuApi skuApi;
	private final BrandApi brandApi;
	private final ProductApi productApi;
	private final CategoryApi categoryApi;
	private final ShopDetailApi shopDetailApi;
	private final SearchProperties searchProperties;
	private final ProductRepository productRepository;
	private final PreSellProductApi preSellProductApi;
	private final ElasticsearchTemplate elasticsearchTemplate;
	private final CouponApi couponApi;
	private final ElasticsearchClient client;


	@Override
	public String initAllProductIndex() {
		log.info("开始创建商品索引，开始时间：{}", System.currentTimeMillis());
		ProductQuery query = new ProductQuery();
		query.setCurPage(1);
		query.setPageSize(100);
		int count = 0;
		IndexOperations indexOperations = elasticsearchTemplate.indexOps(ProductDocument.class);
		if (indexOperations.exists()) {
			PageSupport<ProductDTO> result = productApi.queryProductOnLineEs(query).getData();

			if (result.getTotal() == 0) {
				log.info("创建商品索引错误：没有商品！");
				return "创建商品索引错误：没有商品！";
			}
			String indexName = elasticsearchTemplate.getIndexCoordinatesFor(ProductDocument.class).getIndexName();
			log.info("索引名称：" + indexName);
			//创建索引前彻底删除索引,避免旧数据遗留导致数据结构冲突
			elasticsearchTemplate.indexOps(IndexCoordinates.of(elasticsearchTemplate.getIndexCoordinatesFor(ProductDocument.class).getIndexName())).delete();
			log.info("删除商品索引！");

		} else {
			//没有索引创建索引
			boolean esResult = elasticsearchTemplate.indexOps(IndexCoordinates.of(elasticsearchTemplate.getIndexCoordinatesFor(ProductDocument.class).getIndexName())).create();
		}
		while (true) {
			PageSupport<ProductDTO> result = productApi.queryProductOnLineEs(query).getData();

			if (result.getTotal() == 0) {
				log.info("创建商品索引错误：没有商品！");
				return "创建商品索引错误：没有商品！";
			}
			List<ProductDTO> productList = result.getResultList();
			log.info("查询商品服务：商品ID是：{}", productList.stream().map(ProductDTO::getId).collect(Collectors.toList()));
			// 创建ProductDocument集合
			List<ProductDocument> productDocumentList = new ArrayList<>(productList.size());
			count += productList.size();
			// 遍历productList
			for (ProductDTO product : productList) {
				ProductDocument productDocument = null;
				try {
					productDocument = createProductDocument(product);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (ObjectUtil.isNull(productDocument)) {
					continue;
				}
				productDocumentList.add(productDocument);
			}
			if (productDocumentList.size() <= 0) {
				log.info("没有需要创建索引的商品");
				return "没有需要创建索引的商品";
			} else {
				log.info("转换商品为es商品：创建索引数量为：{}", productDocumentList.size());
				try {
					productRepository.saveAll(productDocumentList);
				} catch (Exception e) {
					e.printStackTrace();
					productRepository.saveAll(productDocumentList);
				}
			}
			query.setCurPage(query.getCurPage() + 1);
			if (result.getCurPageNO() >= result.getPageCount()) {
				break;
			}
		}


		log.info("创建商品索引结束，结束时间：{}", System.currentTimeMillis());
		log.info("创建商品索引结束，创建条数productList.size()：{}", count);

		return CommonConstants.OK;
	}


	@Override
	public Boolean delAllProductIndex() {
		final IndexOperations indexOperations = this.elasticsearchTemplate.indexOps(ProductDocument.class);
		if (!indexOperations.exists()) {
			log.error("索引不存在");
			return Boolean.FALSE;
		}

		productRepository.deleteAll();
		return Boolean.TRUE;
	}

	@Override
	public String initByProductIdIndex(Long productId) {
		ProductDTO productResult = productApi.getProductOnLineEsByProductId(productId).getData();
		if (productResult == null) {
			log.info("初始化商品失败：商品不存在");
			return "初始化商品失败：商品不存在";
		}
		ProductDocument productDocument;

		try {
			productDocument = createProductDocument(productResult);
		} catch (Exception e) {
			log.error("初始化商品索引失败：商品id为：{}", productId);
			return "初始化商品索引失败：商品id为：" + productId;
		}
		if (ObjectUtil.isNull(productDocument)) {
			return "创建商品索引失败:创建productDocument错误";
		}
		if (ProductStatusEnum.PROD_ONLINE.value().equals(productResult.getStatus())) {
			productRepository.save(productDocument);
			log.info("创建商品索引成功：ProductId:{}", productId);
		} else {
			productRepository.delete(productDocument);
			log.info("删除商品索引成功，商品状态不在线：ProductId:{}", productId);
			return "删除商品索引成功，商品状态不在线：ProductId:" + productId;
		}
		return CommonConstants.OK;
	}

	@Override
	public String initByProductIdListIndex(List<Long> productIds) {
		List<ProductDTO> productList = productApi.queryProductOnLineEsByProductId(productIds).getData();
		int count = 0;
		if (productList.size() == 0) {
			log.info("创建商品索引错误：没有商品！");
			return "创建商品索引错误：没有商品！";
		}
		log.info("查询商品服务：商品集合是：{}", productList);

		IndexOperations indexOperations = elasticsearchTemplate.indexOps(ProductDocument.class);
		if (!indexOperations.exists()) {
			log.info("正在重建索引，发现商品索引不存在");
			boolean createFlag = indexOperations.create();
			log.info("索引创建完成，{}", createFlag);
			boolean putMappingFlag = indexOperations.putMapping(indexOperations.createMapping());
			log.info("索引映射完成，{}", putMappingFlag);
		}

		// 创建ProductDocument集合
		List<ProductDocument> productDocumentList = new ArrayList<>(productList.size());
		count += productList.size();
		// 遍历productList
		for (ProductDTO product : productList) {
			ProductDocument productDocument = null;
			try {
				productDocument = createProductDocument(product);
			} catch (Exception e) {
				log.error("createProductDocument error", e);
			}

			if (ObjectUtil.isNull(productDocument)) {
				continue;
			}
			productDocumentList.add(productDocument);
		}
		if (productDocumentList.size() <= 0) {
			log.info("没有需要创建索引的商品");
			return "没有需要创建索引的商品";
		} else {
			try {
				productRepository.saveAll(productDocumentList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("创建商品索引结束，结束时间：{}", System.currentTimeMillis());
		log.info("创建商品索引结束，创建条数：{}", count);
		return CommonConstants.OK;
	}

	@Override
	public String delByProductIdIndex(Long productId) {
		ProductDTO product = productApi.getDtoByProductId(productId).getData();
		if (product == null) {
			log.info("初始化商品失败：商品不存在");
			return "初始化商品失败：商品不存在";
		}
		productRepository.deleteById(productId);
		return CommonConstants.OK;
	}

	@Override
	public IndexCountDTO reIndexCount() throws IOException {
		IndexCountDTO indexCountDTO = new IndexCountDTO();
		IndexDocumentDTO documentDTO = new IndexDocumentDTO();
		SearchShopQuery searchShopQuery = new SearchShopQuery();
		//获取上架商品数量
		Long productIndexCount = productApi.getProductIndexCount();
		documentDTO.setProductDocumentCount(productIndexCount);
		//获取店铺数量
		Long allShop = shopDetailApi.searchAllShop(searchShopQuery);
		if (ObjectUtil.isNull(allShop)) {
			documentDTO.setShopDocumentCount(0L);
		} else {
			documentDTO.setShopDocumentCount(allShop);
		}

		//获取进行中优惠券数量数量
		CouponQuery couponQuery = new CouponQuery();
		couponQuery.setStatus(CouponStatusEnum.CONTAINS.getValue());
		couponQuery.setShopProviderFlag(Boolean.TRUE);
		R<PageSupport<CouponDTO>> coupons = couponApi.queryCouponsByStatus(couponQuery);
		PageSupport<CouponDTO> couponData = coupons.getData();
		List<CouponDTO> couponDataList = couponData.getResultList();
		if (CollUtil.isEmpty(couponDataList)) {
			documentDTO.setCouponDocumentCount(0L);
		} else {
			long couponDataCount = couponDataList.stream().count();
			documentDTO.setCouponDocumentCount(couponDataCount);
		}

		//查询商品等索引
		EsIndexDocumentDTO esDocumentDTO = calculateIndexCount();
		indexCountDTO.setIndexDocumentDTO(documentDTO);
		indexCountDTO.setEsIndexDocumentDTO(esDocumentDTO);
		return indexCountDTO;
	}

	/**
	 * 计算索引的内容的数目
	 *
	 * @return
	 */
	private EsIndexDocumentDTO calculateIndexCount() throws IOException {

		EsIndexDocumentDTO esDocumentDTO = new EsIndexDocumentDTO();

		//1. 获取es店铺数量
		IndexOperations shopIndex = elasticsearchTemplate.indexOps(ShopDocument.class);

		if (shopIndex.exists()) {
			// ES8
			HitsMetadata<ShopDocument> hitsMetadata = null;
			try {
				BoolQuery boolQuery = BoolQuery.of(b -> b);
				SearchResponse<ShopDocument> search = client.search(s -> s
								.index(searchProperties.getShopIndexName())
								.query(q -> q
										.bool(boolQuery))
						, ShopDocument.class);

				log.info("1. 查询店铺 query = {}", boolQuery);

				hitsMetadata = search.hits();

			} catch (Exception e) {
				e.printStackTrace();
				esDocumentDTO.setShopDocumentCount(0L);
			}

			if (hitsMetadata != null && hitsMetadata.total() != null && hitsMetadata.total().value() > 0) {
				//只有索引有数据时才进行计算
				esDocumentDTO.setShopDocumentCount(hitsMetadata.hits().stream().distinct().count());
			} else {
				esDocumentDTO.setShopDocumentCount(0L);
			}

		} else {
			esDocumentDTO.setShopDocumentCount(0L);
		}

		//3. 查询优惠券
		IndexOperations couponIndex = elasticsearchTemplate.indexOps(CouponDocument.class);

		// ES8 todo 需要使用ES8 新版API重写
		if (couponIndex.exists()) {
			Query query = Query.of(q -> q
					.bool(b -> b
							.must(m -> m
									.match(m1 -> m1
											.field("status")
											.query(ProductOnSaleWayEnum.ONSALE.getValue())))));

			SearchResponse<CouponDocument> response = null;
			try {
				log.info("3. 查询优惠券 query = {}", query);
				response = client.search(s -> s
								.index(couponIndex.getIndexCoordinates().getIndexName())
								.query(query)
						, CouponDocument.class);
			} catch (Exception e) {
				e.printStackTrace();
				esDocumentDTO.setCouponDocumentCount(0L);
			}

			if (ObjectUtil.isNotEmpty(response.hits().total().value())) {
				long totalHits = response.hits().total().value();
				//只有索引有数据时才进行计算
				if (totalHits > 0) {
					esDocumentDTO.setCouponDocumentCount(response.hits().hits().stream().distinct().count());
				} else {
					esDocumentDTO.setCouponDocumentCount(0L);
				}
			} else {
				esDocumentDTO.setCouponDocumentCount(0L);
			}

		} else {
			esDocumentDTO.setCouponDocumentCount(0L);
		}

		//4. 获取es上架商品数量
		IndexOperations productIndex = elasticsearchTemplate.indexOps(ProductDocument.class);

		if (productIndex.exists()) {

			SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder();
			searchRequestBuilder
					.index(productIndex.getIndexCoordinates().getIndexName())
					.query(q -> q
							.bool(b -> b
									.must(m -> m
											.match(m1 -> m1
													.field("status")
													.query(ProductOnSaleWayEnum.ONSALE.getValue())
											))));

			SearchRequest searchRequest = searchRequestBuilder.build();


			log.info("搜索商品条件{}", searchRequest.query());
			HitsMetadata<ProductDocument> hitsMetadata = null;

			try {
				hitsMetadata = client.search(searchRequest, ProductDocument.class).hits();
			} catch (Exception e) {
				e.printStackTrace();
				esDocumentDTO.setProductDocumentCount(0L);
			}

			if (ObjectUtil.isNotEmpty(hitsMetadata)) {
				long totalHits = hitsMetadata.total().value();
				//只有索引有数据时才进行计算
				if (totalHits > 0) {
					esDocumentDTO.setProductDocumentCount(hitsMetadata.hits().stream().count());
				} else {
					esDocumentDTO.setProductDocumentCount(0L);
				}
			}
		} else {
			esDocumentDTO.setProductDocumentCount(0L);
		}

		return esDocumentDTO;
	}

	/**
	 * 根据Product创建ProductDocument
	 */
	private ProductDocument createProductDocument(ProductDTO product) throws IOException {
		//构建对象
		ProductDocument productDocument = new ProductDocument();

		// 查询店铺是否存在
		Long shopId = product.getShopId();
		R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(shopId);
		if (!shopDetailResult.success() || ObjectUtil.isNull(shopDetailResult.getData())) {
			log.info("商品信息错误，查询不到商品商家信息，shopId为：{}", shopId);
			return null;
		}
		ShopDetailDTO shopDetail = shopDetailResult.getData();
		// 查询sku信息
		List<SkuBO> skuBOList = this.skuApi.getSkuByProduct(product.getId()).getData();
		// 查询商品分类信息
		String categoryName = this.categoryApi.getCategoryNameById(product.getGlobalFirstCatId()).getData();
		// 根据分类id查询参数的信息
		//List<ProductPropertyBO> productPropertyBOS = productPropertyService.queryParamByCategoryId(product.getGlobalFirstCatId());
		// 查询分销信息
		if (skuBOList == null || categoryName == null) {
			log.info("创建商品索引错误：查询错误，商品没有sku或商品没有分类，商品ID：{}", product.getId());
			return null;
		}

		BigDecimal discount = null;
		BigDecimal reducePrice = null;
		// 限时折扣标记
		Boolean limitDiscountFlag = false;
		//打折标记 ture 打折 false 减免
		Boolean discountFlag = false;
		//准备显示折扣SKU集合
		List<Long> skuIds = new ArrayList<>();

		List<Tag> tagList = new ArrayList<>();

		if (ObjectUtil.isNotEmpty(product.getCommissionRatio()) && product.getCommissionRatio().compareTo(BigDecimal.ZERO) > 0) {
			productDocument.setRatio(product.getCommissionRatio());
		}

		//取第一个有效sku的价格
		Optional<SkuBO> skuOptional = skuBOList.stream().min(Comparator.comparing(SkuBO::getPrice));
		BigDecimal price = skuOptional.map(SkuBO::getPrice).orElse(BigDecimal.ZERO);
		BigDecimal originalPrice = skuOptional.map(SkuBO::getOriginalPrice).orElse(BigDecimal.ZERO);
		// 处理参数
		Map<String, List<Long>> productParameterMap = buildProductParameterMap(product.getParameter(), product.getParamGroup());


		// 写入商品分类Id
//		CategoryBO category = categoryService.getById(product.getGlobalFirstCatId());
//
//		if (category == null) {
//			log.info("查询的分类为空，不进行索引创建！分类Id为：{}", product.getGlobalFirstCatId());
//			return null;
//		}
		//	Long oneCategoryId = categoryService.getParentIdByCategoryId(category.getParentId());
		if (ObjectUtil.isNotNull(product.getGlobalFirstCatId())) {
			productDocument.setGlobalFirstCatId(product.getGlobalFirstCatId());
		}
		if (ObjectUtil.isNotNull(product.getGlobalSecondCatId())) {
			productDocument.setGlobalSecondCatId(product.getGlobalSecondCatId());
		}
		if (ObjectUtil.isNotNull(product.getGlobalThirdCatId())) {
			productDocument.setGlobalThirdCatId(product.getGlobalThirdCatId());
		}


		if (ObjectUtil.isNotNull(product.getShopFirstCatId())) {
			productDocument.setShopFirstCatId(product.getShopFirstCatId());
		}
		if (ObjectUtil.isNotNull(product.getShopSecondCatId())) {
			productDocument.setShopSecondCatId(product.getShopSecondCatId());
		}
		if (ObjectUtil.isNotNull(product.getShopThirdCatId())) {
			productDocument.setShopThirdCatId(product.getShopThirdCatId());
		}
		productDocument.setPrice(price);
		productDocument.setBrief(product.getBrief());
		productDocument.setProductId(product.getId());
		productDocument.setOriginalPrice(originalPrice);
		productDocument.setProductPic(product.getPic());
		productDocument.setProductNameSuggest(product.getName());
		productDocument.setCreateTime(product.getCreateTime());
		productDocument.setComments(Long.valueOf(Optional.ofNullable(product.getComments()).orElse(0)));
		productDocument.setSpecification(product.getSpecification());
		productDocument.setDeliveryType(product.getDeliveryType());
		productDocument.setTransId(product.getTransId());
		// 设置商品品牌信息
		if (ObjectUtil.isNotEmpty(product.getBrandId())) {
			productDocument.setBrandId(product.getBrandId());
			List<Long> brandList = new ArrayList<>();
			brandList.add(product.getBrandId());
			R<List<BrandBO>> brandR = brandApi.getBrandByIds(brandList);
			if (brandR.success() && brandR.getData().size() > 0) {
				BrandBO brandBO = brandR.getData().get(0);
				productDocument.setBrandName(brandBO.getBrandName());
				productDocument.setBrandPic(brandBO.getBrandPic());
			}
		}
		productDocument.setPreSellFlag(product.getPreSellFlag());
		if (product.getPreSellFlag()) {
			R<PreSellProductDTO> PreSellProductDTOR = preSellProductApi.getByProductId(product.getId());
			if (PreSellProductDTOR.success() && ObjectUtil.isNotEmpty(PreSellProductDTOR.getData())) {
				productDocument.setPreSellProductMessage(PreSellProductDTOR.getData());
			}
		}
		productDocument.setVideo(product.getVideo());
		productDocument.setContent(product.getContent());
		productDocument.setStatus(product.getStatus());
		productDocument.setOpStatus(product.getOpStatus());
		productDocument.setDelStatus(product.getDelStatus());
		productDocument.setParamGroup(product.getParamGroup());
		productDocument.setUserParamGroup(product.getUserParamGroup());
		productDocument.setParameter(product.getParameter());
		productDocument.setUserParameter(product.getUserParameter());

		productDocument.setProductPics(product.getImg());
		productDocument.setImgPath(product.getImgPath());
		productDocument.setMainSpecificationId(product.getMainSpecificationId());
		productDocument.setSpecificationStyle(product.getSpecificationStyle());
		ActivityProductDTO activityProductDTO = new ActivityProductDTO();
		activityProductDTO.setSpecification(product.getSpecification());
		activityProductDTO.setSkuBOS(skuBOList);

		activityProductDTO.setSpecification(product.getParameter());

		// 设置商品SKU 集合
		if (limitDiscountFlag) {
			if (discountFlag && ObjectUtil.isNotEmpty(discount)) {
				productDocument.setDiscountPrice(productDocument.getPrice().multiply(discount.divide(new BigDecimal(10), 6, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN));
				if (ObjectUtil.isNotEmpty(skuIds)) {
					for (SkuBO skuBO : skuBOList) {
						if (skuIds.contains(0L) || skuIds.contains(skuBO.getId())) {
							skuBO.setDiscountPrice(skuBO.getPrice().multiply(discount.divide(new BigDecimal(10), 6, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN));
						}
					}
				} else {
					for (SkuBO skuBO : skuBOList) {
						skuBO.setDiscountPrice(skuBO.getPrice().multiply(discount.divide(new BigDecimal(10), 6, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN));
					}
				}

			}
			if (!discountFlag && ObjectUtil.isNotEmpty(reducePrice)) {
				BigDecimal subtract = productDocument.getPrice().subtract(reducePrice);
				productDocument.setDiscountPrice(subtract.compareTo(BigDecimal.ZERO) > 0 ? subtract : BigDecimal.ZERO);
				if (ObjectUtil.isNotEmpty(skuIds)) {
					for (SkuBO skuBO : skuBOList) {
						if (skuIds.contains(0L) || skuIds.contains(skuBO.getId())) {
							BigDecimal skuSubtract = skuBO.getPrice().subtract(reducePrice);
							skuBO.setDiscountPrice(skuSubtract.compareTo(BigDecimal.ZERO) > 0 ? skuSubtract : BigDecimal.ZERO);
						}

					}
				} else {
					for (SkuBO skuBO : skuBOList) {
						BigDecimal skuSubtract = skuBO.getPrice().subtract(reducePrice);
						skuBO.setDiscountPrice(skuSubtract.compareTo(BigDecimal.ZERO) > 0 ? skuSubtract : BigDecimal.ZERO);
					}
				}
			}
		}

		// 添加商家id和商家名称
		productDocument.setShopId(shopId);
		productDocument.setShopName(shopDetail.getShopName());
		productDocument.setShopStatus(shopDetail.getStatus());

		productDocument.setSkuList(skuBOList);
		productDocument.setProductNameSuggest(product.getName());
		productDocument.setCreateTime(product.getCreateTime());


		productDocument.setProductName(product.getName());
		// 写入商品关键字 --（关键字组成：商品名称+商品分类名称）
		productDocument.setKeyword(product.getName() + " " + categoryName + " " + shopDetail.getShopName());
		productDocument.setEv(productParameterMap);
		productDocument.setViews(product.getViews());
		productDocument.setBuys(product.getBuys());
		productDocument.setMultiple(1L);
		productDocument.setDistType(1);

		return productDocument;
	}


	/**
	 * 处理商品参数
	 *
	 * @param productParameter
	 * @return
	 */
	private Map<String, List<Long>> buildProductParameterMap(String productParameter, String paramGroup) {
//		log.info("构建商品参数为：{}", productParameter);
		if ((StrUtil.isBlank(productParameter) && StrUtil.isBlank(paramGroup)) || (!JSONUtil.isJson(productParameter) && !JSONUtil.isJson(paramGroup))) {
			return null;
		}

		List<ProductPropertyBO> productPropertyList = JSONUtil.toList(JSONUtil.parseArray(productParameter), ProductPropertyBO.class);
		List<ParamPropertyGroupDTO> paramPropertyList = JSONUtil.toList(JSONUtil.parseArray(paramGroup), ParamPropertyGroupDTO.class);

		if (ArrayUtil.isEmpty(productPropertyList) && CollUtil.isEmpty(paramPropertyList)) {
			return null;
		}

		Map<String, List<Long>> productParameterMap = new LinkedHashMap<>();
		/*解析系统参数*/
		for (int i = 0; i < productPropertyList.size(); i++) {
			ProductPropertyBO property = productPropertyList.get(i);
			String paramId = property.getId().toString();
			List<ProductPropertyValueBO> prodPropList = property.getProdPropList();
			if (ObjectUtil.isNull(paramId) || ArrayUtil.isEmpty(prodPropList) || ProductPropertySourceEnum.USER.getValue().equals(property.getSource())) {
				continue;
			}
			for (int j = 0; j < prodPropList.size(); j++) {
				ProductPropertyValueBO value = prodPropList.get(j);
				Long valueId = value.getId();
				if (ObjectUtil.isNull(valueId) || !value.getSelectFlag()) {
					continue;
				}
				if (CollUtil.isNotEmpty(productParameterMap.get(paramId))) {
					productParameterMap.get(paramId).add(valueId);
				} else {
					List<Long> valueList = new ArrayList<>();
					valueList.add(valueId);
					productParameterMap.put(paramId, valueList);
				}
			}
		}

		/*解析系统参数组*/
		for (int i = 0; i < paramPropertyList.size(); i++) {
			String paramId = paramPropertyList.get(i).getId().toString();
			List<ProductPropertyValueDTO> propertyValueList = paramPropertyList.get(i).getProdPropList();
			if (ObjectUtil.isNull(paramId) || ArrayUtil.isEmpty(propertyValueList)) {
				continue;
			}
			for (int j = 0; j < propertyValueList.size(); j++) {
				ProductPropertyValueDTO valueDTO = propertyValueList.get(j);
				Long valueId = valueDTO.getId();
				if (ObjectUtil.isNull(valueId) || !valueDTO.getSelectFlag()) {
					continue;
				}
				if (CollUtil.isNotEmpty(productParameterMap.get(paramId))) {
					if (productParameterMap.get(paramId).contains(valueId)) {
						continue;
					}
					productParameterMap.get(paramId).add(valueId);
				} else {
					List<Long> valueList = new ArrayList<>();
					valueList.add(valueId);
					productParameterMap.put(paramId, valueList);
				}
			}
		}
		return productParameterMap;
	}


	@Override
	public boolean isSupport(String indexType) {
		return IndexTypeEnum.PRODUCT_INDEX.name().equals(indexType);
	}

	@Override
	public Class<?> getSupportClass() {
		return ProductDocument.class;
	}

	/**
	 * 索引重建，修改。
	 *
	 * @param targetType
	 * @param targetId
	 * @return
	 */
	@Override
	public String initIndex(Integer targetType, List<Long> targetId) {
		if (ObjectUtil.isEmpty(targetId) || targetId.size() == 0) {
			return initAllProductIndex();
		} else {
			if (targetId.size() == 1) {
				initByProductIdIndex(targetId.get(0));
				return "OK";
			} else {
				//多个ID操作
				return "未支持";
			}
		}
	}

	/**
	 * 删除索引
	 *
	 * @param targetType
	 * @param targetId
	 * @return
	 */
	@Override
	public String delIndex(Integer targetType, List<Long> targetId) {
		if (ObjectUtil.isEmpty(targetId) || targetId.size() == 0) {
			return delAllProductIndex().toString();
		} else {
			if (targetId.size() == 1) {
				delByProductIdIndex(targetId.get(0));
				return "OK";
			} else {
				//多个ID操作
				return "未支持";
			}
		}
	}

	@Override
	public String updateIndex(Integer targetType, List<Long> targetId) {
		ProductTargetTypeEnum targetTypeEnum = ProductTargetTypeEnum.fromCode(targetType);
		if (targetTypeEnum == null) {
			return "未知类型，暂不支持";
		}

		if (CollUtil.isEmpty(targetId)) {
			return "商品ID为空";
		}
		BulkRequest.Builder br = new BulkRequest.Builder();

		if (targetTypeEnum == ProductTargetTypeEnum.STATISTICS) {
			R<List<ProductDTO>> productResult = productApi.queryAllByIds(targetId);

			for (ProductDTO productDTO : Optional.ofNullable(productResult.getData()).orElse(Collections.emptyList())) {

				Map<String, JsonData> params = new HashMap<>(16);
				params.put("buys", JsonData.of(productDTO.getBuys()));
				params.put("views", JsonData.of(productDTO.getViews()));
				params.put("comments", JsonData.of(productDTO.getComments()));


				br.operations(op -> op
						.update(u -> u
								.id(String.valueOf(productDTO.getId()))
								.index(searchProperties.getProductIndexName())
								.action(a -> a
										.script(s -> s
												.inline(i -> i
														.source("ctx._source.buys = params.buys;" +
																"ctx._source.views = params.views;" +
																"ctx._source.comments = params.comments;")
														.params(params))))

						)
				);
			}
		}

		BulkResponse bulk = null;
		try {
			bulk = client.bulk(br.build());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "OK";
	}
}
