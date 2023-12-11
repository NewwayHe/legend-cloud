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
import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.ProductConsultDTO;
import com.legendshop.product.query.ProductConsultQuery;

/**
 * 商品咨询服务
 *
 * @author legendshop
 */
public interface ProductConsultService {
	/**
	 * 新增商品咨询
	 *
	 * @param productConsultDTO
	 * @return
	 */
	R<Long> addConsult(ProductConsultDTO productConsultDTO);

	/**
	 * 用户端商品咨询分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<ProductConsultDTO> getUserProductConsultPage(ProductConsultQuery query);


	/**
	 * 商品咨询上下线
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	R<Integer> offlineById(Long id, Integer status);

	/**
	 * 商品咨询删除
	 *
	 * @param id
	 * @return
	 */
	R<Integer> deleteById(Long id);


	/**
	 * 商品咨询回复
	 *
	 * @param id
	 * @param content
	 * @return
	 */
	R<Integer> replyById(Long id, String content);

	/**
	 * 商品咨询查看
	 *
	 * @param query
	 * @return
	 */
	PageSupport<ProductConsultDTO> getProductConsultCenterPage(ProductConsultQuery query);
}
