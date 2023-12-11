/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.repository;

import com.legendshop.search.document.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品查询Repository
 *
 * @author legendshop
 */
@Repository
public interface ProductRepository extends ElasticsearchRepository<ProductDocument, Long> {
	/**
	 * 根据分类ID查询商品文档的方法。
	 *
	 * @param status           商品状态
	 * @param globalFirstCatId 全局一级分类ID
	 * @param globalSecondCatId 全局二级分类ID
	 * @param globalThirdCatId 全局三级分类ID
	 * @return 符合条件的商品文档列表
	 */
	List<ProductDocument> findByStatusAndGlobalFirstCatIdOrGlobalSecondCatIdOrGlobalThirdCatId(
			Integer status,
			Long globalFirstCatId,
			Long globalSecondCatId,
			Long globalThirdCatId
	);

	/**
	 * 根据商家分类ID查询
	 *
	 * @param shopFirstCatId
	 * @param shopSecondCatId
	 * @param shopThirdCatId
	 * @return
	 */
	List<ProductDocument> findByShopFirstCatIdOrShopSecondCatIdOrShopThirdCatId(Long shopFirstCatId, Long shopSecondCatId, Long shopThirdCatId);

	/**
	 * 根据id集合查询
	 *
	 * @param productIdList
	 * @return
	 */
	List<ProductDocument> findByProductIdIn(List<Long> productIdList);

	/**
	 * 根据商品ID集合查询商品文档的方法。
	 *
	 * @param productIdList 商品ID集合
	 * @param sort          排序对象
	 * @return 符合条件的商品文档列表
	 */
	List<ProductDocument> findAllByProductId(List<Long> productIdList, Sort sort);


	/**
	 * 根据商品id查询分页
	 *
	 * @param productIdList
	 * @param pageable
	 * @return
	 */
	Page<ProductDocument> findByProductIdIn(List<Long> productIdList, Pageable pageable);
}
