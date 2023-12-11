/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import com.legendshop.search.properties.SearchProperties;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author legendshop
 */
@Configuration
public class IndexDeleteTemplate {

	@Autowired
	private SearchProperties searchProperties;

	@Autowired
	RestClient restClient;

	@Autowired
	ElasticsearchClient elasticsearchClient;

	public boolean deleteIndex(String indexName) {
		boolean flag = false;
		try {

			DeleteRequest deleteRequest = DeleteRequest.of(s -> s.index(indexName));

			DeleteResponse delete = elasticsearchClient.delete(deleteRequest);
			flag = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}


	@Deprecated
	public boolean deleteAllIndex() {
		boolean flag = false;
		flag = deleteIndex(searchProperties.getCouponIndexName());
		if (!flag) {
			return false;
		}
		flag = deleteIndex(searchProperties.getActivityIndexName());
		if (!flag) {
			return false;
		}
		flag = deleteIndex(searchProperties.getShopIndexName());
		if (!flag) {
			return false;
		}
		flag = deleteIndex(searchProperties.getProductIndexName());
		return flag;
	}

}
