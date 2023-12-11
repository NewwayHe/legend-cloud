/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.product.bo.ProductGroupBO;
import com.legendshop.product.bo.ProductGroupRelationBO;
import com.legendshop.product.dto.ProductGroupDTO;
import com.legendshop.product.query.ProductGroupQuery;
import com.legendshop.product.query.ProductGroupRelationQuery;

/**
 * The Class ProductGroupService.
 * 服务接口
 *
 * @author legendshop
 */
public interface ProductGroupService extends BaseService<ProductGroupDTO> {

	/**
	 * 根据商品组ID获取商品组信息的方法。
	 *
	 * @param id 商品组ID
	 * @return 商品组信息对象
	 */
	ProductGroupBO getProductGroup(Long id);

	/**
	 * 分页查询商品组列表的方法。
	 *
	 * @param productGroupQuery 商品组查询对象
	 * @return 商品组列表的分页支持对象
	 */
	PageSupport<ProductGroupBO> queryProductGroupListPage(ProductGroupQuery productGroupQuery);

	/**
	 * 分页查询商品组列表的方法。
	 *
	 * @param productGroupQuery 商品组查询对象
	 * @return 商品组列表的分页支持对象
	 */
	PageSupport<ProductGroupDTO> page(ProductGroupQuery productGroupQuery);

	/**
	 * 查看分组下的商品列表
	 *
	 * @param relationQuery
	 * @return
	 */
	PageSupport<ProductGroupRelationBO> queryProductList(ProductGroupRelationQuery relationQuery);

	/**
	 * 删除商品分组及其关联表关联数据
	 *
	 * @param id
	 * @return
	 */
	boolean delGroupAndRelation(Long id);

}
