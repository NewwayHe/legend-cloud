/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.search.document.ShopDocument;
import com.legendshop.search.enmus.IndexTypeEnum;
import com.legendshop.search.repository.ShopRepository;
import com.legendshop.search.service.IndexService;
import com.legendshop.search.service.SearchShopIndexService;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.SearchShopDTO;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.query.SearchShopQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@Service
@Slf4j
@AllArgsConstructor
public class SearchShopIndexServiceImpl implements SearchShopIndexService, IndexService {

	private final ShopRepository shopRepository;
	private final ShopDetailApi shopDetailApi;
	private final ElasticsearchTemplate elasticsearchTemplate;

	@Override
	public String initAllShopIndex() {
		log.info("开始重建所有店铺索引，开始时间：{}", System.currentTimeMillis());

		//创建索引前彻底删除索引,避免旧数据遗留导致数据结构冲突
		String indexName = elasticsearchTemplate.getIndexCoordinatesFor(ShopDocument.class).getIndexName();
		elasticsearchTemplate.indexOps(IndexCoordinates.of(indexName)).delete();
		log.info("删除索引，名字：" + indexName);

		SearchShopQuery query = new SearchShopQuery();
		query.setCurPage(1);
		query.setPageSize(10);
		int count = 0;
		while (true) {
			R<PageSupport<SearchShopDTO>> shopListR = shopDetailApi.searchShopEs(query);
			if (!shopListR.success() || shopListR.getData().getResultList().size() <= 0) {
				log.info("初始化店铺索引失败");
				return "初始化店铺索引失败";
			}
			PageSupport<SearchShopDTO> result = shopListR.getData();
			List<SearchShopDTO> data = result.getResultList();
			log.info("查询店铺服务：店铺集合是：{", data + "}");
			count += data.size();
			List<ShopDocument> shopDocuments = new ArrayList<>();
			for (SearchShopDTO searchShopDTO : data) {
				ShopDocument shopDocument = createShopDocument(searchShopDTO);
				shopDocuments.add(shopDocument);
			}
			if (shopDocuments.size() <= 0) {
				log.info("没有需要创建索引的店铺");
				return "没有需要创建索引的店铺";
			} else {
				log.info("转换店铺为es店铺：创建索引数量为：{}", shopDocuments.size());
				shopRepository.saveAll(shopDocuments);
			}
			query.setCurPage(query.getCurPage() + 1);
			if (result.getCurPageNO() >= result.getPageCount()) {
				break;
			}
		}
		log.info("创建店铺索引结束，结束时间：{}", System.currentTimeMillis());
		log.info("创建店铺索引结束，创建条数shopList.size()：{}", count);
		return CommonConstants.OK;
	}


	@Override
	public Boolean delAllShopIndex() {
		final IndexOperations indexOperations = this.elasticsearchTemplate.indexOps(ShopDocument.class);
		if (!indexOperations.exists()) {
			log.error("索引不存在");
			return Boolean.FALSE;
		}
		shopRepository.deleteAll();
		return Boolean.TRUE;
	}

	@Override
	public String initByShopIdToShopIndex(Long shopId) {


		SearchShopQuery searchShopQuery = new SearchShopQuery();
		searchShopQuery.setShopId(shopId);
		List<SearchShopDTO> resultList = shopDetailApi.searchShopEs(searchShopQuery).getData().getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			log.info("重建店铺索引失败：店铺不存在");
			return "重建店铺索引失败：店铺不存在";
		}
		if (resultList.size() > 1) {
			log.info("存在多个shopId为" + shopId + "的店铺,重建索引失败");
			return "存在多个shopId为" + shopId + "的店铺,重建索引失败";
		}
		ShopDocument shopDocument = createShopDocument(resultList.get(0));
		shopRepository.save(shopDocument);
		return CommonConstants.OK;
	}

	@Override
	public String delByShopIdToShopIdIndex(Long shopId) {
		ShopDetailDTO shopDetailDTO = shopDetailApi.getById(shopId).getData();
		if (shopDetailDTO == null) {
			log.info("删除店铺索引失败：店铺不存在");
			return "删除店铺索引失败：店铺不存在";
		}
		shopRepository.deleteById(shopId);
		return CommonConstants.OK;
	}

	private ShopDocument createShopDocument(SearchShopDTO shop) {
		ShopDocument shopDocument = new ShopDocument();
		shopDocument.setShopId(shop.getShopId());
		shopDocument.setShopName(shop.getShopName());
		shopDocument.setShopType(shop.getShopType());
		shopDocument.setApplyForType(shop.getApplyForType());
		shopDocument.setPic(shop.getShopAvatar());
		shopDocument.setShopCommAvg(shop.getShopCommAvg());
		shopDocument.setDvyTypeAvg(shop.getDvyTypeAvg());
		shopDocument.setProductCommentAvg(shop.getProductCommentAvg());
		// 设置综合评分
		BigDecimal score = (shop.getDvyTypeAvg().add(shop.getShopCommAvg()).add(shop.getProductCommentAvg())).divide(new BigDecimal("3"), 2, RoundingMode.DOWN);
		shopDocument.setScore(score);
		return shopDocument;
	}

	/**
	 * 支持的类型
	 *
	 * @param indexType
	 * @return
	 */
	@Override
	public boolean isSupport(String indexType) {
		return IndexTypeEnum.SHOP_INDEX.name().equals(indexType);
	}

	@Override
	public Class<?> getSupportClass() {
		return ShopDocument.class;
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
			return initAllShopIndex();
		} else {
			if (targetId.size() == 1) {
				return initByShopIdToShopIndex(targetId.get(0));
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
			return delAllShopIndex().toString();
		} else {
			if (targetId.size() == 1) {
				return delByShopIdToShopIdIndex(targetId.get(0));
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
