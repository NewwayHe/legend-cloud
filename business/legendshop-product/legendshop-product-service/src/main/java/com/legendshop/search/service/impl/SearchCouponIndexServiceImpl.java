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
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.legendshop.activity.api.CouponApi;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.enums.ActivityEsTypeEnum;
import com.legendshop.activity.enums.CouponReceiveTypeEnum;
import com.legendshop.activity.enums.CouponStatusEnum;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.search.document.CouponDocument;
import com.legendshop.search.enmus.IndexTypeEnum;
import com.legendshop.search.properties.SearchProperties;
import com.legendshop.search.repository.CouponRepository;
import com.legendshop.search.service.IndexService;
import com.legendshop.search.service.SearchCouponIndexService;
import com.legendshop.search.service.SearchProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@Service
@Slf4j
@AllArgsConstructor
public class SearchCouponIndexServiceImpl implements SearchCouponIndexService, IndexService {

	private final CouponApi couponApi;
	private final CouponRepository couponRepository;
	private final SearchProperties searchProperties;
	private final SearchProductService searchProductService;
	private final ElasticsearchTemplate elasticsearchTemplate;
	private final ElasticsearchClient client;


	@Override
	public String initAllCouponIndex() throws IOException {
		log.info("开始重建所有优惠卷索引，开始时间：{}", System.currentTimeMillis());
		//创建索引前彻底删除索引,避免旧数据遗留导致数据结构冲突

		elasticsearchTemplate.indexOps(IndexCoordinates.of(elasticsearchTemplate.getIndexCoordinatesFor(CouponDocument.class).getIndexName())).delete();
		String couponIndexName = searchProperties.getCouponIndexName();

		log.info("删除优惠卷索引成功");

		CouponQuery query = new CouponQuery();
		query.setStatus(CouponStatusEnum.CONTAINS.getValue());
		query.setReceiveType(CouponReceiveTypeEnum.FREE.getValue());
		query.setShopProviderFlag(true);
		query.setPageSize(1000);
		int count = 0;

		while (true) {
			R<PageSupport<CouponDTO>> couponPageSupport = couponApi.queryCouponsByStatus(query);
			if (!couponPageSupport.success() || ObjectUtil.isEmpty(couponPageSupport.getData())) {
				log.info("初始化优惠卷索引失败");
				break;
			}
			List<CouponDocument> couponDocumentList = new ArrayList<>();
			PageSupport<CouponDTO> result = couponPageSupport.getData();
			List<CouponDTO> data = result.getResultList();
			if (CollUtil.isEmpty(data)) {
				log.info("没有需要创建索引的优惠卷");
				break;
			}

			for (CouponDTO couponDTO : data) {
				CouponDocument couponDocument = createCouponDocument(couponDTO);
				couponDocumentList.add(couponDocument);
			}

			count += couponDocumentList.size();
			log.info("转换优惠卷为es优惠卷：创建索引数量为：{}", couponDocumentList.size());
			couponRepository.saveAll(couponDocumentList);
			query.setCurPage(query.getCurPage() + 1);

			if (query.getCurPage() >= result.getPageCount()) {
				break;
			}
		}
		log.info("创建优惠卷引结束，结束时间：{}", System.currentTimeMillis());
		log.info("创建优惠卷引结束，创建条数：{}", count);
		return CommonConstants.OK;
	}

	@Override
	public Boolean delAllCouponIndex() {
		final IndexOperations indexOperations = this.elasticsearchTemplate.indexOps(CouponDocument.class);
		if (!indexOperations.exists()) {
			log.error("索引不存在");
			return Boolean.FALSE;
		}
		couponRepository.deleteAll();
		return Boolean.TRUE;
	}

	@Override
	public R<Void> initByCouponIdToCouponIndex(Long couponId) {
		R<CouponDTO> couponsR = couponApi.getCouponsById(couponId);
		if (!couponsR.getSuccess() || ObjectUtil.isEmpty(couponsR.getData())) {
			log.info("initByCouponIdToCouponIndex 根据ID重建优惠卷索引失败：优惠卷不存在");
			return R.fail("根据ID重建优惠卷索引失败：优惠卷不存在");
		}
		CouponDTO couponDTO = couponsR.getData();
		if (!couponDTO.getShopProviderFlag()) {
			log.info("initByCouponIdToCouponIndex 平台券不参与索引建立");
			return R.ok();
		}

		CouponDocument couponDocument = createCouponDocument(couponDTO);
		couponRepository.save(couponDocument);
		return R.ok();
	}

	/**
	 * 创建优惠卷索引文档对象
	 *
	 * @param data
	 * @return
	 */
	private CouponDocument createCouponDocument(CouponDTO data) {
		CouponDocument couponDocument = new CouponDocument();
		couponDocument.setId(ActivityEsTypeEnum.createId(data.getId(), ActivityEsTypeEnum.COUPON));
		couponDocument.setCouponId(data.getId());
		couponDocument.setStatus(data.getStatus());
		couponDocument.setAmount(data.getAmount());
		couponDocument.setShopId(data.getShopId());
		couponDocument.setMinPoint(data.getMinPoint());
		couponDocument.setReceiveEndTime(data.getReceiveEndTime());
		couponDocument.setRemark(data.getRemark());
		couponDocument.setSkuIds(data.getSkuIdList());
		couponDocument.setShopIds(data.getSelectShopId());
		couponDocument.setReceiveStartTime(data.getReceiveStartTime());
		couponDocument.setShopProviderFlag(data.getShopProviderFlag());
		couponDocument.setTitle(data.getTitle());
		couponDocument.setUseEndTime(data.getUseEndTime());
		couponDocument.setUseStartTime(data.getUseStartTime());
		couponDocument.setUseType(data.getUseType());
		couponDocument.setDesignatedUser(data.getDesignatedUser());
		return couponDocument;
	}

	@Override
	public R<Void> delByCouponIdToCouponIndex(Long couponId) {
		couponRepository.deleteById(ActivityEsTypeEnum.createId(couponId, ActivityEsTypeEnum.COUPON));
		return R.ok();
	}

	@Override
	public boolean isSupport(String indexType) {
		return IndexTypeEnum.COUPON_INDEX.name().equals(indexType);
	}

	@Override
	public Class<?> getSupportClass() {
		return CouponDocument.class;
	}

	/**
	 * 索引重建，修改。
	 *
	 * @param targetType
	 * @param targetId
	 * @return
	 */
	@Override
	public String initIndex(Integer targetType, List<Long> targetId) throws IOException {
		if (ObjectUtil.isEmpty(targetId) || targetId.size() == 0) {
			return initAllCouponIndex();
		} else {
			if (targetId.size() == 1) {
				initByCouponIdToCouponIndex(targetId.get(0));
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
			return delAllCouponIndex().toString();
		} else {
			if (targetId.size() == 1) {
				delByCouponIdToCouponIndex(targetId.get(0));
				return "OK";
			} else {
				//多个ID操作
				return "未支持";
			}
		}
	}

	@Override
	public String updateIndex(Integer targetType, List<Long> targetId) {
		return null;
	}
}
