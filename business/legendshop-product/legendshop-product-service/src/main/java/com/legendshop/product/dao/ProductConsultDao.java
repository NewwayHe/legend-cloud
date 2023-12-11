/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.dto.ProductConsultDTO;
import com.legendshop.product.entity.ProductConsult;
import com.legendshop.product.query.ProductConsultQuery;

/**
 * 商品咨询Dao
 *
 * @author legendshop
 */
public interface ProductConsultDao extends GenericDao<ProductConsult, Long> {

	/**
	 * 分页查询商品咨询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<ProductConsultDTO> queryProductConsultPage(ProductConsultQuery query);

	/**
	 * 分页查询用户端的商品咨询（已回复）
	 *
	 * @param query
	 * @return
	 */
	PageSupport<ProductConsultDTO> queryUserProductConsultPage(ProductConsultQuery query);


}
