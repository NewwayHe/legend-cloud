/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service;


import com.legendshop.search.dto.IndexCountDTO;

import java.io.IOException;
import java.util.List;

/**
 * @author legendshop
 */
public interface SearchProductIndexService {

	/**
	 * 初始化所有商品索引
	 *
	 * @return
	 */
	String initAllProductIndex();

	/**
	 * 删除所有索引
	 *
	 * @return
	 */
	Boolean delAllProductIndex();

	/**
	 * 根据商品id重建索引
	 *
	 * @return
	 */
	String initByProductIdIndex(Long productId);

	/**
	 * 根据商品id批量重建索引
	 *
	 * @return
	 */
	String initByProductIdListIndex(List<Long> productIds);

	/**
	 * 根据商品id删除索引
	 *
	 * @return
	 */
	String delByProductIdIndex(Long productId);


	IndexCountDTO reIndexCount() throws IOException;
}
