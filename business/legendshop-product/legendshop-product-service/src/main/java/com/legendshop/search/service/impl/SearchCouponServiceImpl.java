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
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.legendshop.activity.enums.CouponUseTypeEnum;
import com.legendshop.search.document.CouponDocument;
import com.legendshop.search.dto.CouponDocumentDTO;
import com.legendshop.search.properties.SearchProperties;
import com.legendshop.search.service.SearchCouponService;
import com.legendshop.search.service.converter.CouponDocumentConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Service
@Slf4j
@AllArgsConstructor
public class SearchCouponServiceImpl implements SearchCouponService {
	private final CouponDocumentConverter couponDocumentConverter;
	private final ElasticsearchTemplate elasticsearchTemplate;
	private final ElasticsearchClient client;
	private final SearchProperties searchProperties;


	/**
	 * @param skuIds 商品SKU Ids
	 * @param shopId 店铺Id
	 * @return
	 */
	@Override
	public List<CouponDocumentDTO> queryCouponBySkuIdsAndShopId(List<Long> skuIds, Long shopId) {

		return this.queryByUserTypeAndShopProviderFlag(skuIds, Collections.singletonList(shopId), true);

//		List<CouponDocumentDTO> couponDocumentDTOList = new ArrayList<>();
//		List<CouponDocumentDTO> couPonList1 = queryByUserTypeAndShopProviderFlag(skuIds, shopId, CouponUseTypeEnum.EXCLUDE.getValue(), true);
//		if(CollUtil.isNotEmpty(couPonList1)){
//			couponDocumentDTOList.addAll(couPonList1);
//		}
//		// TODO candy: 商品详情页只展示店铺发布的优惠券
////		List<CouponDocumentDTO> couPonList2 = queryByUserTypeAndShopProviderFlag(skuIds, shopId, CouponUseTypeEnum.EXCLUDE.getValue(), false);
////		if(CollUtil.isNotEmpty(couPonList2)){
////			couponDocumentDTOList.addAll(couPonList2);
////		}
//		List<CouponDocumentDTO> couPonList3 = queryByUserTypeAndShopProviderFlag(skuIds, shopId, CouponUseTypeEnum.GENERAL.getValue(), true);
//		if(CollUtil.isNotEmpty(couPonList3)){
//			couponDocumentDTOList.addAll(couPonList3);
//		}
////		List<CouponDocumentDTO> couPonList4 = queryByUserTypeAndShopProviderFlag(skuIds, shopId, CouponUseTypeEnum.GENERAL.getValue(), false);
////		if(CollUtil.isNotEmpty(couPonList4)){
////			couponDocumentDTOList.addAll(couPonList4);
////		}
//		List<CouponDocumentDTO> couPonList5 = queryByUserTypeAndShopProviderFlag(skuIds, shopId, CouponUseTypeEnum.INCLUDE.getValue(), true);
//		if(CollUtil.isNotEmpty(couPonList5)){
//			couponDocumentDTOList.addAll(couPonList5);
//		}
////		List<CouponDocumentDTO> couPonList6 = queryByUserTypeAndShopProviderFlag(skuIds, shopId, CouponUseTypeEnum.INCLUDE.getValue(), false);
////		if(CollUtil.isNotEmpty(couPonList6)){
////			couponDocumentDTOList.addAll(couPonList6);
////		}

//		return couponDocumentDTOList;
	}

	/**
	 * 更具使用类型和 店铺标识来查找优惠券 共六中查询
	 *
	 * @param skuIds           当前skuId列表可用的优惠券
	 * @param shopIds          当前店铺列表可用的优惠券
	 * @param shopProviderFlag true 商家 false 平台
	 * @return
	 */
	@Override
	public List<CouponDocumentDTO> queryByUserTypeAndShopProviderFlag(List<Long> skuIds, List<Long> shopIds, Boolean shopProviderFlag) {

		final IndexOperations couPonIndex = elasticsearchTemplate.indexOps(CouponDocument.class);
		if (!couPonIndex.exists()) {
			log.error("优惠券索引不存在或没有数据，请重建");
			return Collections.emptyList();
		}
		// 获取通用券
		Query generalType = TermQuery.of(t -> t
				.field("useType")
				.value(CouponUseTypeEnum.GENERAL.getValue())
		)._toQuery();
		// 获取指定券
		Query includeType = TermQuery.of(t -> t
				.field("useType")
				.value(CouponUseTypeEnum.INCLUDE.getValue())
		)._toQuery();
		// 获取排除券
		Query excludeType = TermQuery.of(t -> t
				.field("useType")
				.value(CouponUseTypeEnum.EXCLUDE.getValue())
		)._toQuery();
		//
		TermQuery byShopProviderFlag = TermQuery.of(t -> t
				.field("shopProviderFlag")
				.value(shopProviderFlag)
		);
		//
		TermsQuery byShopIds = TermsQuery.of(t -> t
				.field("shopIds")
				.terms(t2 -> t2
						.value(shopIds.stream().map(FieldValue::of).collect(Collectors.toList()))
				)
		);
		//
		TermsQuery byShopId = TermsQuery.of(t -> t
				.field("shopId")
				.terms(t2 -> t2
						.value(shopIds.stream().map(FieldValue::of).collect(Collectors.toList()))
				)
		);
		//
		TermsQuery bySkuIds = TermsQuery.of(t -> t
				.field("skuIds")
				.terms(t2 -> t2
						.value(skuIds.stream().map(FieldValue::of).collect(Collectors.toList()))
				)
		);


		BoolQuery.Builder includeBuilder = new BoolQuery.Builder();
		includeBuilder.must(includeType);

		BoolQuery.Builder excludeBuilder = new BoolQuery.Builder();
		excludeBuilder.must(excludeType);

		BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();


		if (shopProviderFlag != null) {

			boolQueryBuilder.must(m -> m.term(byShopProviderFlag));

			// 只查询满足的商家券
			if (shopProviderFlag) {
				if (CollUtil.isNotEmpty(shopIds)) {
					boolQueryBuilder.must(m -> m.terms(byShopId));
				}
				//指定商品
				if (CollUtil.isNotEmpty(skuIds)) {
					includeBuilder.must(m -> m.terms(bySkuIds));
					excludeBuilder.mustNot(m -> m.terms(bySkuIds));
				}

			}
			// 只查询满足的平台券
			else {
				// 指定店铺
				if (CollUtil.isNotEmpty(shopIds)) {
					includeBuilder.must(m -> m.terms(byShopIds));
					excludeBuilder.mustNot(m -> m.terms(byShopIds));
				}
			}

		} else {
			//指定商品
			if (CollUtil.isNotEmpty(skuIds)) {
				includeBuilder.must(m -> m.terms(bySkuIds));
				excludeBuilder.mustNot(m -> m.terms(bySkuIds));
			}
			//指定店铺
			if (CollUtil.isNotEmpty(shopIds)) {
				// 指定券
				includeBuilder
						.must(m -> m
								.bool(b -> b
										.should(s -> s.terms(byShopId))
										.should(s1 -> s1.terms(byShopIds))));

				// 排除券
				excludeBuilder
						.must(m -> m
								.bool(b -> b
										.should(s -> s.terms(byShopId))
										.should(s1 -> s1
												.bool(b2 -> b2
														.mustNot(m2 -> m2
																.terms(byShopIds))))));

			}

		}

		boolQueryBuilder
				.filter(f -> f
						.bool(b2 -> b2
								.should(generalType)
								.should(s -> s
										.bool(includeBuilder.build()))
								.should(s1 -> s1
										.bool(excludeBuilder.build()))
						)
				);

		SearchResponse<CouponDocument> search = null;
		try {
			search = client.search(s -> s
							.index(searchProperties.getCouponIndexName())
							.query(q -> q
									.bool(boolQueryBuilder.build())
							)
					, CouponDocument.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Hit<CouponDocument>> hits = search.hits().hits();

		return hits.stream()
				.map(Hit::source)
				.map(couponDocumentConverter::to)
				.distinct()
				.collect(Collectors.toList());


	}

	/**
	 * 更具使用类型和 店铺标识来查找优惠券 共六中查询
	 *
	 * @param userType         CouponUseTypeEnum
	 * @param shopProviderFlag true 商家 false 平台
	 * @return
	 */
	private List<CouponDocumentDTO> queryByUserTypeAndShopProviderFlag(List<Long> skuIds, Long shopId, Integer userType, Boolean shopProviderFlag) throws IOException {

		final IndexOperations couPonIndex = elasticsearchTemplate.indexOps(CouponDocument.class);
		if (!couPonIndex.exists()) {
			log.error("优惠券索引不存在或没有数据，请重建");
			return Collections.emptyList();
		}

		if (!CouponUseTypeEnum.isExist(userType)) {
			return Collections.emptyList();
		}
		// 创建匹配商家提供者标志的查询
		Query matchShopProviderFlag = MatchQuery.of(m -> m
				.field("shopProviderFlag")
				.query(shopProviderFlag)
		)._toQuery();

		Query matchUseType_exclude = MatchQuery.of(m -> m
				.field("useType")
				.query(CouponUseTypeEnum.EXCLUDE.getValue())
		)._toQuery();

		Query matchUseType_general = MatchQuery.of(m -> m
				.field("useType")
				.query(CouponUseTypeEnum.GENERAL.getValue())
		)._toQuery();

		Query matchUseType_include = MatchQuery.of(m -> m
				.field("useType")
				.query(CouponUseTypeEnum.INCLUDE.getValue())
		)._toQuery();

		Query matchShopIds = MatchQuery.of(m -> m
				.field("shopIds")
				.query(shopId)
		)._toQuery();


		BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
		boolQueryBuilder.must(matchShopProviderFlag);

		//排除
		if (CouponUseTypeEnum.EXCLUDE.getValue().equals(userType)) {
			if (shopProviderFlag) {
				boolQueryBuilder.must(matchUseType_exclude);
				skuIds.forEach(e -> {
					// 必须不包含这个sku
					boolQueryBuilder.mustNot(m -> m
							.match(m2 -> m2
									.field("skuIds")
									.query(e)));
				});
			} else {
				boolQueryBuilder.must(matchUseType_exclude);
				boolQueryBuilder.mustNot(matchShopIds);
			}
		}
		//通用
		if (CouponUseTypeEnum.GENERAL.getValue().equals(userType)) {
			boolQueryBuilder.must(matchUseType_general);
		}
		// 指定商品
		if (CouponUseTypeEnum.INCLUDE.getValue().equals(userType)) {
			boolQueryBuilder.must(matchUseType_include);

			if (shopProviderFlag) {
				skuIds.forEach(e -> {
					boolQueryBuilder.should(s -> s
							.match(m -> m
									.field("skuIds")
									.query(e)));
				});
			} else {
				boolQueryBuilder.should(matchShopIds);
			}
			// 预防should不生效
			boolQueryBuilder.minimumShouldMatch("1");
		}


		SearchResponse<CouponDocument> search = client.search(s -> s
						.index(searchProperties.getCouponIndexName())
						.query(q -> q
								.bool(boolQueryBuilder.build()))
				, CouponDocument.class);

		List<Hit<CouponDocument>> hits = search.hits().hits();

		if (hits.size() == 0) {
			return Collections.emptyList();
		}
		return hits.stream()
				.map(Hit::source)
				.map(couponDocumentConverter::to)
				.distinct()
				.collect(Collectors.toList());


	}
}
