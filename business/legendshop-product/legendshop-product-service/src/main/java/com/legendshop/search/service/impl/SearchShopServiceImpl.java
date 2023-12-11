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
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.legendshop.search.document.ShopDocument;
import com.legendshop.search.dto.ShopDocumentDTO;
import com.legendshop.search.service.SearchShopService;
import com.legendshop.search.service.converter.ShopDocumentConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author legendshop
 */
@Service
@Slf4j
@AllArgsConstructor
public class SearchShopServiceImpl implements SearchShopService {
	private final ShopDocumentConverter shopDocumentConverter;
	private final ElasticsearchTemplate elasticsearchTemplate;
	private final ElasticsearchClient client;

	@Override
	public ShopDocumentDTO getShopById(Long shopId) {
		final IndexOperations shopIndex = elasticsearchTemplate.indexOps(ShopDocument.class);

		if (!shopIndex.exists()) {
			log.error("商家营销索引不存在，请重建");
			return null;
		}
		SearchResponse<ShopDocument> response = null;
		try {
			response = client.search(s -> s
							.index(shopIndex.getIndexCoordinates().getIndexName())
							.query(q -> q
									.bool(b -> b
											.must(m -> m
													.match(m1 -> m1
															.field("shopId")
															.query(shopId)
													))))
					, ShopDocument.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Hit<ShopDocument>> hits = response.hits().hits();
		if (CollUtil.isEmpty(hits)) {
			return null;
		}
		return shopDocumentConverter.to(hits.get(0).source());
	}
}
