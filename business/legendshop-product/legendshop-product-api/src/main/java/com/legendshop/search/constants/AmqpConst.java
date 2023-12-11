/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.constants;

/**
 * 消息队列常量
 *
 * @author legendshop
 */
public interface AmqpConst {

	// ------------------------ 搜索索引分隔符-------------------------------------

	String INDEX_EXCHANGE = "legendshop.index.exchange";


	String INDEX_CREATE_QUEUE = "legendshop.index.create.queue";
	String INDEX_CREATE_ROUTING_KEY = "index.create";

	String INDEX_DELETE_QUEUE = "legendshop.index.delete.queue";
	String INDEX_DELETE_ROUTING_KEY = "index.delete";

	String INDEX_DELETE_BY_CATEGORY_QUEUE = "legendshop.index.delete.by.category.queue";
	String INDEX_DELETE_BY_CATEGORY_ROUTING_KEY = "index.delete.by.category";

	String INDEX_CREATE_BY_SHOP_QUEUE = "legendshop.index.create.by.shop.queue";
	String INDEX_CREATE_BY_SHOP_ROUTING_KEY = "shop.index.create";

	//------------------------搜索索引结束分隔符------------------------------------


	//----------------重建索引分隔符--------------------------------------

	String REBUILD_ES_INDEX_QUEUE = "rebuild_es_index_queue";

	String REBUILD_ES_INDEX_ROUTING_KEY = "rebuild_es_index_routing_key";

	//----------------重建索引分隔符--------------------------------------


}
