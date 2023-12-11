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
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.LongTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.LongTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import com.legendshop.activity.enums.ActivityEsTypeEnum;
import com.legendshop.activity.enums.CouponDesignateEnum;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.data.constants.AmqpConst;
import com.legendshop.data.dto.SearchHistoryDTO;
import com.legendshop.order.api.OrderApi;
import com.legendshop.order.dto.ShopOrderCountDTO;
import com.legendshop.product.api.*;
import com.legendshop.product.bo.*;
import com.legendshop.product.enums.*;
import com.legendshop.search.document.ProductDocument;
import com.legendshop.search.dto.*;
import com.legendshop.search.properties.SearchProperties;
import com.legendshop.search.repository.ProductRepository;
import com.legendshop.search.service.SearchCouponService;
import com.legendshop.search.service.SearchProductService;
import com.legendshop.search.service.converter.ProductDocumentConverter;
import com.legendshop.search.vo.SearchRequest;
import com.legendshop.search.vo.SearchResult;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品搜索接口实现
 *
 * @author legendshop
 */
@Service
@Slf4j
@AllArgsConstructor
public class SearchProductServiceImpl implements SearchProductService {

	private final AmqpSendMsgUtil amqpSendMsgUtil;
	private final BrandApi brandApi;
	private final CategoryApi categoryApi;
	private final ProductDocumentConverter converter;

	private final ProductRepository productRepository;
	private final ProductGroupApi productGroupApi;
	private final FavoriteProductApi favoriteProductApi;
	private final ProductPropertyApi productPropertyApi;
	private final ElasticsearchTemplate elasticsearchTemplate;
	private final ProductPropertyValueApi productPropertyValueApi;
	private final ProductGroupRelationApi productGroupRelationApi;
	private final OrderApi orderApi;
	private final SearchCouponService searchCouponService;
	private final PropertyAggBrandApi propertyAggBrandApi;
	private final ElasticsearchClient client;
	private final SearchProperties searchProperties;


	private final ElasticsearchOperations elasticsearchOperations;

	/**
	 * 商品搜索
	 */
	@Override
	public SearchResult<ProductDocumentDTO> searchProduct(SearchRequest request, Long userId) {

		//搜索数据记录,不管有没有，先记录
		sendSearchLogMessage(request, userId);

		final IndexOperations indexOperations = elasticsearchTemplate.indexOps(ProductDocument.class);
		if (!indexOperations.exists()) {
			log.error("索引不存在，请重建");
			return null;
		}
		log.info("查询索引: {}", JSONUtil.toJsonStr(indexOperations.getSettings()));
		// 创建查询构建器
		co.elastic.clients.elasticsearch.core.SearchRequest.Builder searchBuilder = new co.elastic.clients.elasticsearch.core.SearchRequest.Builder();
		// 1、构建查询条件
		// 1.1.对搜索的结果进行过滤，返回页面需要参数
		searchBuilder
				.index(searchProperties.getProductIndexName())
				.source(s -> s
						.filter(f -> f
								.includes("productId", "keyword", "productName", "productName_suggest", "globalFirstCatId", "globalSecondCatId", "globalThirdCatId", "price", "originalPrice", "views", "buys", "comments", "brief", "skus", "ev", "brandId", "shopName", "shopId", "productPic", "multiple", "tagList", "preSellProductMessage", "discountPrice", "skuList", "ratio")
						));
		// 1.2.基本查询
		BoolQuery.Builder basicQueryBuilder = new BoolQuery.Builder();
		// 基本查询条件
		if (StrUtil.isBlank(request.getKey())) {
			basicQueryBuilder.must(m -> m.matchAll(m1 -> m1));
		} else {
			basicQueryBuilder.must(m -> m
					.bool(b -> b
							.should(s -> s
									.matchPhrase(m1 -> m1
											.field("productName").query(request.getKey()).boost(3f)))
							.should(s1 -> s1
									.matchPhrase(m2 -> m2
											.field("shopName").query(request.getKey())))
							.should(s2 -> s2
									.matchPhrase(m3 -> m3
											.field("brandName").query(request.getKey())))

					));


			// 分词查询
//			shouldQuery.should(QueryBuilders.matchQuery("productName", request.getKey()));
//			shouldQuery.should(QueryBuilders.matchQuery("shopName", request.getKey()));
//			shouldQuery.should(QueryBuilders.matchQuery("brandName", request.getKey()));

		}
//
		BoolQuery.Builder filterQueryBuilder = this.buildBasicQueryWithFilter(request);
		BoolQuery basicQuery = basicQueryBuilder.filter(filterQueryBuilder.build()._toQuery()).build();
		// 1.3 构建权重查询，短语匹配到的搜索词，求和模式累加权重分// TODO
		searchBuilder
				.query(q -> q
						.bool(basicQuery));

		// 构建高亮查询
		List<String> highlightFieldList = new ArrayList<>();
		highlightFieldList.add("productName");
		highlightFieldList.add("keyword");
		Highlight.Builder highlightBuilder = new Highlight.Builder();
		for (String s : highlightFieldList) {
			highlightBuilder.fields(s, hf -> hf.preTags("<em class='highlight'>").postTags("</em>").requireFieldMatch(true));
		}
		// 名字高亮
		searchBuilder.highlight(highlightBuilder.build());


		// 1.3 添加排序条件
		if (StrUtil.isNotBlank(request.getSortBy())) {
			// 添加排序字段
			SortOrder sort = SortOrder.Asc;
			if (request.getDescending()) {
				sort = SortOrder.Desc;
			}
			SortOrder finalSort = sort;
			searchBuilder.sort(so -> so
					.field(f -> f
							.field(request.getSortBy())
							.order(finalSort)));
			// 添加默认排序
			searchBuilder.sort(so -> so
					.field(f -> f
							.field("_score")
							.order(SortOrder.Desc)));

		}

		// 1.4、分页
		searchBuilder.from(request.getCurPage() - 1).size(request.getSize());

		/**
		 * 聚合
		 * {
		 *   "_source": "globalFirstCatId",
		 *   "aggs": {
		 *     "categoryAgg":{
		 *       "terms": {
		 *          "field": "globalFirstCatId",
		 *          "include": ["17873"]
		 *       }
		 *     }
		 *   }
		 * }
		 */
		// 1.5、分类聚合
		String categoryAggName = "categoryAgg";
		TermsAggregation.Builder globalFirstCatAgg = new TermsAggregation.Builder();
		globalFirstCatAgg
				.field("globalFirstCatId")
				.size(5000);
		if (ObjectUtil.isNotEmpty(request.getCategoryId())) {
			List<String> categoryIdList = Convert.toList(String.class, request.getCategoryId());
			globalFirstCatAgg
					.include(t -> t.terms(categoryIdList));
		}
		searchBuilder.aggregations(categoryAggName, a -> a
				.terms(globalFirstCatAgg.build()));

		// 1.6、品牌聚合
		String brandAggName = "brandAgg";
		TermsAggregation.Builder brandAgg = new TermsAggregation.Builder();
		brandAgg
				.field("brandId")
				.size(5000);
		if (CollUtil.isNotEmpty(request.getBrandId())) {
			List<String> brandIdList = Convert.toList(String.class, request.getBrandId());
			brandAgg
					.include(i -> i.terms(brandIdList));
		}
		searchBuilder.aggregations(brandAggName, a -> a
				.terms(brandAgg.build()));

		co.elastic.clients.elasticsearch.core.SearchRequest searchRequest = searchBuilder.build();


//		log.info("搜索条件，{}", searchQuery.toString());
		log.info("搜索条件，{}", searchRequest.query());
		log.info("过滤条件，{}", searchRequest.source().filter());
		log.info("高亮条件，{}", searchRequest.highlight().highlightQuery());

		log.info("查询开始！date:{}", System.currentTimeMillis());
		// 2、查询结果
		SearchResponse<ProductDocument> aggResponse = null;
		try {
			aggResponse = client.search(searchRequest, ProductDocument.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HitsMetadata<ProductDocument> aggHitsMeta = aggResponse.hits();
		log.info("查询结束！date:{}", System.currentTimeMillis());
//		log.info(String.valueOf(aggResult.getSearchHits().get(0).getHighlightFields().toString()));
		// 3、解析结果
		// 3.1、总条数和总页数
		long total = aggHitsMeta.total().value();
		long totalPage = (total + request.getSize() - 1) / request.getSize();

		log.info("查询总数，:共{} 条数据", total);
		if (total <= 0) {
			return null;
		}
		log.info("转换对象！date:{}", System.currentTimeMillis());
		//转换对象
		List<ProductDocumentDTO> productDocumentList = aggHitsMeta.hits().stream()
				.map(Hit::source)
				.map(converter::to)
				.collect(Collectors.toList());

		//包含聚合结果的 Map 对象
		Map<String, Aggregate> aggregations = aggResponse.aggregations();
		//获取分类的聚合结果。
		Aggregate categoryAggregate = aggregations.get(categoryAggName);
		Aggregate brandAggregate = aggregations.get(brandAggName);


		log.info("解析商品分类！date:{}", System.currentTimeMillis());
		// 3.2、解析商品分类
		List<SearchCategoryDTO> categories = getCategoryAgg(categoryAggregate);
		log.info("解析品牌开始！date:{}", System.currentTimeMillis());
		// 3.3、解析品牌
		List<BrandBO> brands = new ArrayList<>();
		if (ObjectUtil.isEmpty(request.getBrandId())) {
			if (ObjectUtil.isNotEmpty(request.getCategoryId())) {
				brands = getBrandAgg(request.getCategoryId());
			} else {
				brands = getBrandAgg(brandAggregate);
			}
		}
		log.info("解析品牌完成！date:{}", System.currentTimeMillis());
		//进行搜索，搜索完成后对参数进行聚合展示

		// 3.4、处理参数
		//确定分类了可以去进行规格参数的聚合
		List<Map<String, Object>> specs = getSpecifications(categories, basicQuery, request.getEv());
		// 对高亮的结果进行处理
		aggResponse.hits().hits().forEach(hit -> {
			Map<String, List<String>> highlightFieldsMap = hit.highlight();
			ProductDocument productDocument = hit.source();
			if (CollUtil.isNotEmpty(highlightFieldsMap)) {
				// 高亮先去掉
//				if (highlightFieldsMap.containsKey("productName")) {
//					content.setProductName(highlightFieldsMap.get("productName").get(0));
//				}
				if (highlightFieldsMap.containsKey("keyword")) {
					productDocument.setKeyword(highlightFieldsMap.get("keyword").get(0));
				}
			}

		});


		List<Long> productIds = new ArrayList<>();
		/*查找用户所有的收藏商品*/
		if (ObjectUtil.isNotEmpty(userId)) {
			productIds = favoriteProductApi.queryProductIdByUserId(userId).getData();
		}
		log.info("查询结束！date:{}", System.currentTimeMillis());

		/*鉴别用户是否收藏*/
		for (ProductDocumentDTO productDocumentDTO : productDocumentList) {
			if (CollUtil.isNotEmpty(productIds) && productIds.contains(productDocumentDTO.getProductId())) {
				productDocumentDTO.setFavourite(true);
			}
		}

		// 获取用户店铺下单信息
		Map<Long, Long> shopOrderCountMap = new HashMap<>(16);
		if (null != userId) {
			R<List<ShopOrderCountDTO>> shopOrderCountResult = orderApi.getShopOrderCountExceptRefundSuccess(productDocumentList.stream().map(ProductDocumentDTO::getShopId).collect(Collectors.toList()));
			shopOrderCountMap = Optional.ofNullable(shopOrderCountResult.getData()).orElse(Collections.emptyList()).stream().collect(Collectors.toMap(ShopOrderCountDTO::getShopId, ShopOrderCountDTO::getCount));
		}
		log.info("查询结束！date:{}", System.currentTimeMillis());
		for (ProductDocumentDTO productDocumentDTO : productDocumentList) {
			List<Tag> tags = new ArrayList<>();

			// 过渡重复标签
			List<Tag> tagList = Tag.disposeTagList(productDocumentDTO.getTagList());
			if (CollUtil.isNotEmpty(tagList)) {
				tags.addAll(tagList);
			}
			productDocumentDTO.setTagList(tags);

			// 获取当前商品的优惠券
			List<CouponDocumentDTO> couponDocumentList = searchCouponService.queryByUserTypeAndShopProviderFlag(productDocumentDTO.getSkuList().stream().map(SkuBO::getId).collect(Collectors.toList()), Collections.singletonList(productDocumentDTO.getShopId()), true);

			// 如果存在未指定用户的通用券，则直接加标签
			if (couponDocumentList.stream().anyMatch(e -> CouponDesignateEnum.NONE.getValue().equals(e.getDesignatedUser()))) {
				Tag tag = new Tag();
				tag.setId(productDocumentDTO.getProductId());
				tag.setType(ActivityEsTypeEnum.COUPON.getValue());
				tag.setContent("券");
				tags.add(tag);

				// 否则只有登录用户才需要判断是否有指定用户券
			} else if (userId != null) {
				// 获取当前用户在当前店铺的下单数
				Long orderCount = shopOrderCountMap.get(productDocumentDTO.getShopId());

				// 新用户
				if (orderCount == null || orderCount == 0L) {
					if (couponDocumentList.stream().anyMatch(e -> CouponDesignateEnum.SHOP_NEW_USER.getValue().equals(e.getDesignatedUser()))) {
						Tag tag = new Tag();
						tag.setId(productDocumentDTO.getProductId());
						tag.setType(ActivityEsTypeEnum.COUPON.getValue());
						tag.setContent("券");
						tags.add(tag);
					}
				} else {
					// 老用户
					if (couponDocumentList.stream().anyMatch(e -> CouponDesignateEnum.SHOP_OLD_USER.getValue().equals(e.getDesignatedUser()))) {
						Tag tag = new Tag();
						tag.setId(productDocumentDTO.getProductId());
						tag.setType(ActivityEsTypeEnum.COUPON.getValue());
						tag.setContent("券");
						tags.add(tag);
					}
				}
			}
		}
		log.info("查询结束！date:{}", System.currentTimeMillis());
//		// 获取商品优惠券 TODO 暂时不用这方案处理
//		List<Long> skuIds = productDocumentList.stream().map(ProductDocumentDTO::getSkuList).flatMap(List::stream).map(SkuBO::getId).collect(Collectors.toList());
//		List<CouponDocumentDTO> couponDocumentList = searchCouponService.queryByUserTypeAndShopProviderFlag(skuIds, null, true);
//		Map<Long, List<CouponDocumentDTO>> couponMap = couponDocumentList.stream().collect(Collectors.groupingBy(CouponDocumentDTO::getShopId));
//
//		// 获取用户店铺下单信息
//		Map<Long, Long> shopOrderCountMap = new HashMap<>();
//		if (null != userId) {
//			R<List<ShopOrderCountDTO>> shopOrderCountResult = orderClient.getShopOrderCountExceptRefundSuccess(productDocumentList.stream().map(ProductDocumentDTO::getShopId).collect(Collectors.toList()));
//			shopOrderCountMap = Optional.ofNullable(shopOrderCountResult.getData()).orElse(Collections.emptyList()).stream().collect(Collectors.toMap(ShopOrderCountDTO::getShopId, ShopOrderCountDTO::getCount));
//		}
//		Map<Long, Long> finalShopOrderCountMap = shopOrderCountMap;
//
//		productDocumentList.stream().collect(Collectors.groupingBy(ProductDocumentDTO::getShopId)).forEach((shopId, productDocuments) -> {
//			// 获取优惠券列表
//			if (couponMap.containsKey(shopId)) {
//				List<CouponDocumentDTO> couponList = couponMap.get(shopId);
//				Map<Integer, List<CouponDocumentDTO>> designatedCouponMap = couponList.stream().collect(Collectors.groupingBy(CouponDocumentDTO::getDesignatedUser));
//
//				// 如果用户未登录，只需要未指定用户券
//				if (null == userId) {
//					List<CouponDocumentDTO> couponDocumentDTOList = designatedCouponMap.get(CouponDesignateEnum.NONE.getValue());
//
//					if (CollUtil.isNotEmpty(couponDocumentDTOList)) {
//						for (ProductDocumentDTO productDocument : productDocuments) {
//
//						}
//					}
//				} else {
//
//				}
//			}
//		});

		if (ObjectUtil.isNotEmpty(request.getCategoryId())) {
			categories = new ArrayList<>();
		}

		return new SearchResult<>(total, totalPage, productDocumentList, categories, brands, specs, request.getSize(), request.getCurPage());

//		return null;
	}

	private List<BrandBO> getBrandAgg(Long id) {
		List<BrandBO> brandBOS = new ArrayList<>();
//		if (CollUtil.isEmpty(id)) {
//			return brandBOS;
//		}

		R<PageSupport<BrandBO>> brandByIds = this.propertyAggBrandApi.getBrandByIds(id);
		if (ObjectUtil.isEmpty(brandByIds.getData())) {
			return brandBOS;
		}
		brandBOS = brandByIds.getData().getResultList();
		log.info("类目对应的品牌{}", brandBOS);
		return brandBOS;
	}

	@Override
	public ProductDocumentDTO searchProductById(Long productId) {
		final IndexOperations indexOperations = elasticsearchTemplate.indexOps(ProductDocument.class);
		if (!indexOperations.exists()) {
			log.error("索引不存在，请重建");
			return null;
		}

		Optional<ProductDocument> optional = productRepository.findById(productId);
		return optional.map(converter::to).orElse(null);
	}

	@Override
	public List<ProductDocumentDTO> searchProductListByIds(List<Long> productIdList) {
		if (CollUtil.isEmpty(productIdList)) {
			return Collections.emptyList();
		}

		final IndexOperations indexOperations = elasticsearchTemplate.indexOps(ProductDocument.class);
		if (!indexOperations.exists()) {
			log.error("索引不存在，请重建");
			return Collections.emptyList();
		}

		return converter.to(productRepository.findByProductIdIn(productIdList));
	}

	@Override
	public List<ProductDocumentDTO> searchProductListByIds(List<Long> productIdList, String orderBy, int count) {
		Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime"));
		if (StrUtil.isNotBlank(orderBy)) {
			sort = Sort.by(new Sort.Order(Sort.Direction.DESC, orderBy));
		}
		List<ProductDocument> productDocumentList = productRepository.findAllByProductId(productIdList, sort);
		if (count != 0) {
			productDocumentList = productDocumentList.stream().limit(count).collect(Collectors.toList());
		}
		return converter.to(productDocumentList);
	}

	@Override
	public List<ProductDocumentDTO> searchProductByCategoryId(Long categoryId, int pageSize) {
//		List<ProductDocument> productDocumentList = productRepository.findByGlobalFirstCatIdOrGlobalSecondCatIdOrGlobalThirdCatId(categoryId, categoryId, categoryId);
//		if (pageSize != 0) {
//			productDocumentList = productDocumentList.stream().limit(pageSize).collect(Collectors.toList());
//		}
//		return converter.to(productDocumentList);
		SearchRequest request = new SearchRequest();
		request.setStatus(ProductStatusEnum.PROD_ONLINE.getValue());
		request.setOpStatus(OpStatusEnum.PASS.getValue());
		request.setDelStatus(ProductDelStatusEnum.PROD_NORMAL.value());
		request.setShopStatus(ShopDetailStatusEnum.ONLINE.getStatus());
		request.setCategoryId(categoryId);
		request.setPageSize(pageSize);
		SearchResult<ProductDocumentDTO> result = this.searchProduct(request, null);
		if (ObjectUtil.isNull(result)) {
			return null;
		}
		return result.getResultList();
	}

	@Override
	public SearchResult<ProductDocumentDTO> searchProductListByIds(List<Long> productIdList, SearchRequest request) {
		PageRequest pageRequest = PageRequest.of(request.getCurPage() - 1, request.getSize());
		Page<ProductDocument> productDocumentList = productRepository.findByProductIdIn(productIdList, pageRequest);
//		//转换对象
//		List<ProductDocumentDTO> list = productDocumentList.stream()
//				.map(SearchHit::getContent)
//				.map(converter::to)
//				.collect(Collectors.toList());
		// 3.1、总条数和总页数

		List<ProductDocumentDTO> list = converter.to(productDocumentList.toList());
		long total = productDocumentList.getTotalPages();
		long totalPage = (total + request.getSize() - 1) / request.getSize();
		return new SearchResult<>(total, totalPage, list, request.getSize(), request.getCurPage());
	}

	@Override
	public List<ProductDocumentDTO> getProductByGroup(Long prodGroupId, int count) {

		ProductGroupBO productGroup = productGroupApi.getProductGroup(prodGroupId).getData();
		if (ObjectUtil.isNull(productGroup)) {
			return null;
		}
		// 获取分组关联商品ID集合
		List<Long> productIds = productGroupRelationApi.getProductIdListByGroupId(prodGroupId).getData();
		if (ObjectUtil.isEmpty(productIds)) {
			return null;
		}

		// 获取分组排序条件
		String orderBy;
		if (ProductGroupTypeEnum.SYSTEM.value().equals(productGroup.getType())) {
			//获取系统定义分组的排序条件
			String conditional = productGroup.getConditional();
			ProductGroupConditionalBO productGroupConditionalBO = JSONUtil.toBean(conditional, ProductGroupConditionalBO.class);
			orderBy = productGroupConditionalBO.getSortBy();
		} else {
			orderBy = productGroup.getSort();
		}
		return this.searchProductListByIds(productIds, orderBy, count);
	}

	@Override
	public SearchResult<ProductDocumentDTO> getProductPageListByGroup(Long prodGroupId, Integer curPageNO) {

		ProductGroupBO productGroup = productGroupApi.getProductGroup(prodGroupId).getData();
		if (ObjectUtil.isNull(productGroup)) {
			return null;
		}
		// 获取分组关联商品ID集合
		List<Long> productIds = productGroupRelationApi.getProductIdListByGroupId(prodGroupId).getData();
		if (ObjectUtil.isEmpty(productIds)) {
			return null;
		}
		// 获取分组排序条件
		String orderBy;
		if (ProductGroupTypeEnum.SYSTEM.value().equals(productGroup.getType())) {
			//获取系统定义分组的排序条件
			String conditional = productGroup.getConditional();
			ProductGroupConditionalBO productGroupConditionalBO = JSONUtil.toBean(conditional, ProductGroupConditionalBO.class);
			orderBy = productGroupConditionalBO.getSortBy();
		} else {
			orderBy = productGroup.getSort();
		}
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.setCurPage(curPageNO);
		searchRequest.setSortBy(orderBy);
		return this.searchProductListByIds(productIds, searchRequest);
	}

	@Override
	public List<ProductDocumentDTO> searchShopProductByCategoryId(Long categoryId, Integer pageSize) {
		List<ProductDocument> productDocumentList = productRepository.findByShopFirstCatIdOrShopSecondCatIdOrShopThirdCatId(categoryId, categoryId, categoryId);
		if (pageSize != 0) {
			productDocumentList = productDocumentList.stream().filter(e -> e.getStatus().equals(ProductStatusEnum.PROD_ONLINE.getValue())).limit(pageSize).collect(Collectors.toList());
		}
		return converter.to(productDocumentList);
	}

	@Override
	public List<ProductDocumentDTO> queryByTagId(Long tagId) {
		co.elastic.clients.elasticsearch.core.SearchRequest.Builder searchBuilder = new co.elastic.clients.elasticsearch.core.SearchRequest.Builder();
		searchBuilder.query(q -> q
				.bool(b -> b
						.must(m -> m
								.match(m1 -> m1
										.field("tagList.id")
										.query(tagId)))));

		SearchResponse<ProductDocument> response = null;
		try {
			response = client.search(searchBuilder.build(), ProductDocument.class);
		} catch (IOException e) {
			e.printStackTrace();
		}


		return response.hits().hits().stream()
				.map(Hit::source)
				.map(converter::to)
				.distinct()
				.collect(Collectors.toList());
	}

//	private List<Map<String, Object>> buildProductParam(List<ProductDocumentDTO> list) {
//
//		if (CollUtil.isEmpty(list)) {
//			log.warn("搜索结果为空，不需要聚合");
//			return null;
//		}
//		List<Map<String, Object>> productParamMap = new LinkedList<>();
//
//		for (ProductDocumentDTO productDocumentDTO : list) {
//			Map<String, List<Long>> ev = productDocumentDTO.getEv();
//			if (CollUtil.isEmpty(ev)) {
//				log.warn("商品参数为空，没有商品参数聚合");
//				return null;
//			}
//
//
//		}
//		return list;
//	}


	/**
	 * 构建基本查询条件
	 *
	 * @param request the request
	 * @return QueryBuilder
	 */
	private BoolQuery.Builder buildBasicQueryWithFilter(SearchRequest request) {

		BoolQuery.Builder filterQueryBuilder = new BoolQuery.Builder();

		if (ObjectUtil.isNotNull(request.getStatus())) {
			filterQueryBuilder.must(m -> m.term(t -> t.field("status").value(request.getStatus())));
		}
		if (ObjectUtil.isNotNull(request.getOpStatus())) {
			filterQueryBuilder.must(m -> m.term(t -> t.field("opStatus").value(request.getOpStatus())));
		}
		if (ObjectUtil.isNotNull(request.getDelStatus())) {
			filterQueryBuilder.must(m -> m.term(t -> t.field("delStatus").value(request.getDelStatus())));
		}
		if (ObjectUtil.isNotNull(request.getShopStatus())) {
			filterQueryBuilder.must(m -> m.term(t -> t.field("shopStatus").value(request.getShopStatus())));
		}

		if (ObjectUtil.isNotNull(request.getCategoryId())) {
			filterQueryBuilder.must(m -> m
					.multiMatch(m2 -> m2
							.query(request.getCategoryId().toString())
							.fields("globalFirstCatId", "globalSecondCatId", "globalThirdCatId")
					));
		}


		if (ObjectUtil.isNotNull(request.getShopCategoryId())) {
			filterQueryBuilder.must(m -> m
					.multiMatch(m2 -> m2
							.query(request.getShopCategoryId().toString())
							.fields("shopFirstCatId", "shopSecondCatId", "shopThirdCatId")
					));
		}

		if (CollUtil.isNotEmpty(request.getPriceInterval())) {

			RangeQuery.Builder priceBuild = new RangeQuery.Builder().field("price");
			if (request.getPriceInterval().size() >= 1 && ObjectUtil.isNotNull(request.getPriceInterval().get(0))) {
				priceBuild.gte(JsonData.of(request.getPriceInterval().get(0)));

			}
			if (request.getPriceInterval().size() == 2 && ObjectUtil.isNotNull(request.getPriceInterval().get(1))) {
				priceBuild.lte(JsonData.of(request.getPriceInterval().get(1)));
			}
			Query price = priceBuild.build()._toQuery();


			RangeQuery.Builder discountPriceBuild = new RangeQuery.Builder().field("discountPrice");
			if (request.getPriceInterval().size() >= 1 && ObjectUtil.isNotNull(request.getPriceInterval().get(0))) {
				discountPriceBuild.gte(JsonData.of(request.getPriceInterval().get(0)));
			}
			if (request.getPriceInterval().size() == 2 && ObjectUtil.isNotNull(request.getPriceInterval().get(1))) {
				discountPriceBuild.lte(JsonData.of(request.getPriceInterval().get(1)));
			}
			Query discountPrice = discountPriceBuild.build()._toQuery();

			filterQueryBuilder.must(m -> m
					.bool(b -> b
							.should(price)
							.should(discountPrice)));
		}
		if (CollUtil.isNotEmpty(request.getBrandId())) {
			filterQueryBuilder.must(m -> m
					.terms(t -> t
							.field("brandId")
							.terms(t1 -> t1.value(request.getBrandId().stream().map(FieldValue::of).collect(Collectors.toList())))
					));
		}

		if (ObjectUtil.isNotEmpty(request.getShopId())) {
			filterQueryBuilder.must(m -> m
					.term(t -> t
							.field("shopId")
							.value(request.getShopId())));

//			if (request.getShopIds().size() > 1) {
//				for (Long shopId : request.getShopIds()) {
//					filterQueryBuilder.should(QueryBuilders.termQuery("shopId", shopId));
//				}
//			} else {
//				filterQueryBuilder.must(QueryBuilders.termQuery("shopId", request.getShopIds().get(0)));
//			}
		}

		if (ObjectUtil.isNotEmpty(request.getDistType())) {
			filterQueryBuilder.must(m -> m
					.term(t -> t
							.field("distType")
							.value(request.getDistType())));
		}

		// 需要对预售商品进行特殊搜索
		BoolQuery.Builder preSellQueryBuilder = new BoolQuery.Builder();

		// 非预售商品搜索
		preSellQueryBuilder.should(s -> s
				.term(t -> t
						.field("preSellFlag")
						.value(Boolean.FALSE)
				));

		// 定金预售搜索
		BoolQuery.Builder depositBuilder = new BoolQuery.Builder();
		depositBuilder
				.must(m -> m
						.term(t -> t
								.field("preSellProductMessage.payPctType")
								.value(PreSellPayType.DEPOSIT.value())))
				.must(m1 -> m1
						.range(r -> r
								.field("preSellProductMessage.depositStart")
								.lte(JsonData.of(DateUtil.currentSeconds()))))
				.must(m2 -> m2
						.range(r1 -> r1
								.field("preSellProductMessage.depositEnd")
								.gte(JsonData.of(DateUtil.currentSeconds()))));


		// 全款搜索
		BoolQuery.Builder fullAmountBuilder = new BoolQuery.Builder();
		fullAmountBuilder
				.must(m -> m
						.term(t -> t
								.field("preSellProductMessage.payPctType")
								.value(PreSellPayType.FULL_AMOUNT.value())));

		depositBuilder
				.must(m -> m
						.range(r -> r
								.field("preSellProductMessage.preSaleStart")
								.lte(JsonData.of(DateUtil.currentSeconds()))))
				.must(m1 -> m1
						.range(r1 -> r1
								.field("preSellProductMessage.preSaleEnd")
								.gte(JsonData.of(DateUtil.currentSeconds()))));


		preSellQueryBuilder
				.should(depositBuilder.build()._toQuery())
				.should(fullAmountBuilder.build()._toQuery());

		filterQueryBuilder.must(preSellQueryBuilder.build()._toQuery());


		// 过滤参数条件构建器  kId1_optionsId11|optionsId12;kId2_optionsId21|optionsId22;kId3_optionsId3
		String evStr = request.getEv();
		if (StrUtil.isNotBlank(evStr)) {
			// 整理过滤条件
			List<String> paramList = StrUtil.split(evStr, StringConstant.SEMICOLON);
			for (String s : paramList) {
				List<String> param = StrUtil.split(s, StringConstant.UNDERLINE);
				String paramId = param.get(0);
				List<String> valueArray = StrUtil.split(param.get(1), StringConstant.VERTICAL_BAR);
				if (valueArray.size() > 1) {
					for (String value : valueArray) {
						filterQueryBuilder.should(s1 -> s1
								.term(t -> t
										.field("ev." + paramId)
										.value(value)));
					}
				} else {
					filterQueryBuilder.must(m -> m
							.term(t -> t
									.field("ev." + paramId)
									.value(valueArray.get(0))));
				}
			}
		}
		return filterQueryBuilder;
	}

	/**
	 * 获取分类聚合
	 *
	 * @param terms
	 * @return
	 */
	private List<SearchCategoryDTO> getCategoryAgg(Aggregate terms) {
		List<SearchCategoryDTO> searchCategoryDTO = new ArrayList<>();
		//解析terms获取聚合的分类ids
		List<Long> categoryIds = terms.lterms().buckets().array().stream()
				.map(LongTermsBucket::key)
				.distinct()
				.collect(Collectors.toList());
		if (CollUtil.isEmpty(categoryIds)) {
			return searchCategoryDTO;
		}

		// 获取分类名称
		List<CategoryBO> categoryList = this.categoryApi.getCategoryByIds(categoryIds).getData();
		if (CollUtil.isEmpty(categoryList)) {
			log.warn("分类聚合数据为空");
			return searchCategoryDTO;
		}
		return categoryList.stream().map(c -> new SearchCategoryDTO(c.getId(), c.getName())).collect(Collectors.toList());
	}


	/**
	 * 获取品牌聚合
	 *
	 * @param terms
	 * @return
	 */
	private List<BrandBO> getBrandAgg(Aggregate terms) {
		List<BrandBO> brandBOS = new ArrayList<>();
		if (terms.isUmterms() || !terms.isLterms()) {
			log.info("没有聚合到品牌");
			return brandBOS;
		}

		//解析terms获取聚合的品牌ids
		List<Long> brandIds = terms.lterms().buckets().array()
				.stream()
				.map(LongTermsBucket::key)
				.collect(Collectors.toList());

		if (CollUtil.isEmpty(brandIds)) {
			return brandBOS;
		}
		brandBOS = this.brandApi.getBrandByIds(brandIds).getData();
		log.info("聚合品牌{}", brandBOS);
		return brandBOS;
	}

	/**
	 * 封装商品参数
	 *
	 * @param categoryBOList
	 * @param basicQuery
	 * @return
	 */
//	private List<Map<String, Object>> getSpecification(List<SearchCategoryDTO> categoryBOList, QueryBuilder basicQuery, String evStr) {
//		List<Map<String, Object>> specs = new ArrayList<>();
//		if (CollUtil.isEmpty(categoryBOList)) {
//			return specs;
//		}
//
//		// 过滤参数条件构建器,获取所有参数id  kId1_optionsId11|optionsId12;kId2_optionsId21|optionsId22;kId3_optionsId3
//		List<Long> paramIdList = new ArrayList<>();
//		if (StrUtil.isNotBlank(evStr)) {
//			// 整理过滤条件
//			String[] paramList = StrUtil.split(evStr, StringConstant.SEMICOLON);
//			for (String s : paramList) {
//				String[] param = StrUtil.split(s, StringConstant.UNDERLINE);
//				paramIdList.add(Long.parseLong(param[0]));
//			}
//		}
//		Map<Long, ProductPropertyBO> specMap = new HashMap<>();
//		List<Long> idList = categoryBOList.stream().map(SearchCategoryDTO::getCategoryId).collect(Collectors.toList());
//		log.info("根据分类查询商品参数！date:{}", System.currentTimeMillis());
//		// 1、根据分类查询商品参数
//		List<ProductPropertyBO> specResp = productPropertyClient.queryParamByCategoryIds(idList, null).getData().stream()
//				.filter(productProperty -> !productProperty.getType().equals(ProductPropertyAttributeTypeEnum.PARAMETER.getValue())).collect(Collectors.toList());
//		// 获取重复的商品参数
//		List<ProductPropertyBO> repeatSpecList = specResp.stream().filter(e -> specMap.containsKey(e.getId())).collect(Collectors.toList());
//		if (CollUtil.isNotEmpty(repeatSpecList)) {
//			specResp.removeAll(repeatSpecList);
//		}
//		specResp.forEach(e -> specMap.put(e.getId(), e));
//
//		// 2、开始聚合
//		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
//		// 3、在原有的搜索结果进行聚合
//		queryBuilder.withQuery(basicQuery);
//
//		for (ProductPropertyBO prop : specResp) {
//			if (!paramIdList.contains(prop.getId())) {
//				queryBuilder.addAggregation(AggregationBuilders.terms(prop.getId().toString()).field("ev." + prop.getId()));
//			}
//		}
//		// 4、获取聚合结果
//
//		AggregatedPage<ProductDocument> aggResult =
//					(AggregatedPage<ProductDocument>) this.productRepository.search(queryBuilder.build());
//
//
//			List<Long> ids=null;
//			log.info("specResp！date:{}", System.currentTimeMillis());
//			for (ProductPropertyBO prop : specResp) {
//				if (paramIdList.contains(prop.getId())) {
//					continue;
//				}
//				Aggregations aggregations = aggResult.getAggregations();
//				Aggregation aggregation = aggregations.get(prop.getId().toString());
//				if (aggregation instanceof UnmappedTerms || aggregation instanceof ParsedStringTerms) {
//					continue;
//				}
//				ParsedLongTerms longTerms = (ParsedLongTerms) aggregation;
//				if (CollUtil.isEmpty(longTerms.getBuckets())) {
//					continue;
//				}
//				Map<String, Object> map = new HashMap<>();
//				map.put("k", new SearchProductParamDTO(prop.getId(), prop.getPropName()));
//				List<Long> valueIdList = longTerms.getBuckets().stream()
//						.map(bucket -> Long.valueOf(bucket.getKeyAsString()))
//						.collect(Collectors.toList());
//				List<ProductPropertyValueBO> valueBOList = productPropertyValueClient.getProductPropertyValue(valueIdList).getData();
//				List<SearchProductParamValueDTO> paramNameList = valueBOList
//						.stream()
//						.map(value -> new SearchProductParamValueDTO(value.getId(), value.getName()))
//						.collect(Collectors.toList());
//				map.put("options", paramNameList);
//				specs.add(map);
//		}
//		log.info("specResp！date:{}", System.currentTimeMillis());
//		return specs;
//	}

	/**
	 * 封装商品参数
	 *
	 * @param categoryBOList
	 * @param basicQuery
	 * @return
	 */
	private List<Map<String, Object>> getSpecifications(List<SearchCategoryDTO> categoryBOList, BoolQuery basicQuery, String evStr) {
		List<Map<String, Object>> specs = new ArrayList<>();
		if (CollUtil.isEmpty(categoryBOList)) {
			return specs;
		}

		// 过滤参数条件构建器,获取所有参数id  kId1_optionsId11|optionsId12;kId2_optionsId21|optionsId22;kId3_optionsId3
		List<Long> paramIdList = new ArrayList<>();
		if (StrUtil.isNotBlank(evStr)) {
			// 整理过滤条件
			List<String> paramList = StrUtil.split(evStr, StringConstant.SEMICOLON);
			for (String s : paramList) {
				List<String> param = StrUtil.split(s, StringConstant.UNDERLINE);
				paramIdList.add(Long.parseLong(param.get(0)));
			}
		}

		Map<Long, ProductPropertyBO> specMap = new HashMap<>(16);
		for (SearchCategoryDTO searchCategoryDTO : categoryBOList) {
			if (ObjectUtil.isEmpty(searchCategoryDTO.getCategoryId())) {
				continue;
			}
			// 1、根据分类查询商品参数
			List<ProductPropertyBO> specResp = productPropertyApi.queryParamByCategoryId(searchCategoryDTO.getCategoryId(), null).getData().stream()
					.filter(productProperty -> !productProperty.getType().equals(ProductPropertyAttributeTypeEnum.PARAMETER.getValue())).collect(Collectors.toList());
			if (CollUtil.isEmpty(specResp)) {
				log.warn("查询规格参数出错，该类目下没有关联的参数shopCategoryId={}", searchCategoryDTO.getCategoryId());
				continue;
			}
			log.info("specResp{}", specResp);
			// 获取重复的商品参数
			List<ProductPropertyBO> repeatSpecList = specResp.stream().filter(e -> specMap.containsKey(e.getId())).collect(Collectors.toList());
			if (CollUtil.isNotEmpty(repeatSpecList)) {
				specResp.removeAll(repeatSpecList);
			}
			specResp.forEach(e -> specMap.put(e.getId(), e));

			// 2、开始聚合
			co.elastic.clients.elasticsearch.core.SearchRequest.Builder searchBuilder = new co.elastic.clients.elasticsearch.core.SearchRequest.Builder();
			// 3、在原有的搜索结果进行聚合
			searchBuilder
					.query(q -> q.bool(basicQuery));

			log.info("specResp{}", specResp);
			for (ProductPropertyBO prop : specResp) {
				if (!paramIdList.contains(prop.getId())) {
					searchBuilder.aggregations(prop.getId().toString(), a -> a
							.terms(t -> t
									.field("ev." + prop.getId())));
				}
			}

			// 4、获取聚合结果

//			SearchHits<ProductDocument> aggResult = elasticsearchOperations.search(queryBuilder.build(), ProductDocument.class);
			SearchResponse<ProductDocument> response = null;
			try {
				response = client.search(searchBuilder.build(), ProductDocument.class);
			} catch (IOException e) {
				e.printStackTrace();
			}

//			Aggregations aggregations = aggResult.getAggregations();

			Map<String, Aggregate> aggregations = Objects.requireNonNull(response.aggregations());
			for (ProductPropertyBO prop : specResp) {
				if (paramIdList.contains(prop.getId())) {
					continue;
				}
				Aggregate aggregate = aggregations.get(prop.getId().toString());
				if (aggregate.isUmterms() || aggregate.isSterms()) {
					continue;
				}
				LongTermsAggregate lterms = aggregate.lterms();
				if (CollUtil.isEmpty(lterms.buckets().array())) {
					continue;
				}
				Map<String, Object> map = new HashMap<>(16);
				map.put("k", new SearchProductParamDTO(prop.getId(), prop.getPropName()));
				List<Long> valueIdList = lterms.buckets().array().stream()
						.map(LongTermsBucket::key)
						.collect(Collectors.toList());

				List<ProductPropertyValueBO> valueBOList = productPropertyValueApi.getProductPropertyValue(valueIdList).getData();
				List<SearchProductParamValueDTO> paramNameList = valueBOList
						.stream()
						.map(value -> new SearchProductParamValueDTO(value.getId(), value.getName()))
						.collect(Collectors.toList());
				map.put("options", paramNameList);
				specs.add(map);
			}
		}
		return specs;
	}

	/**
	 * 异步调用搜索记录日志
	 *
	 * @param request
	 * @param userId
	 */
	private void sendSearchLogMessage(SearchRequest request, Long userId) {
		HttpServletRequest http = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		SearchHistoryDTO searchHistoryEntity = new SearchHistoryDTO();
		searchHistoryEntity.setUserId(userId);
		searchHistoryEntity.setCreateTime(new Date());
		searchHistoryEntity.setWord(request.getKey());
		searchHistoryEntity.setRemoteIP(http.getRemoteAddr());
		searchHistoryEntity.setUserAgent(http.getHeader("user-agent"));
		String source = http.getHeader(RequestHeaderConstant.SOURCE_KEY);
		searchHistoryEntity.setSource(source);

		//发送mq消息给data服务写搜索记录
		amqpSendMsgUtil.convertAndSend(AmqpConst.LEGENDSHOP_DATA_EXCHANGE, AmqpConst.LEGENDSHOP_DATA_SEARCH_LOG_ROUTING_KEY, searchHistoryEntity);

	}


}
