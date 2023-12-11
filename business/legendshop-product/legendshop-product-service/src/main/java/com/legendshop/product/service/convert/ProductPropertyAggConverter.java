/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.convert;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.product.bo.ProductPropertyAggBO;
import com.legendshop.product.dto.ProductPropertyAggDTO;
import com.legendshop.product.entity.ProductPropertyAgg;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface ProductPropertyAggConverter extends BaseConverter<ProductPropertyAgg, ProductPropertyAggDTO> {

	/**
	 * to bo
	 *
	 * @param productPropertyAgg
	 * @return
	 */
	ProductPropertyAggBO convert2BO(ProductPropertyAgg productPropertyAgg);

	/**
	 * to bo List
	 *
	 * @param productPropertyAggList
	 * @return
	 */
	List<ProductPropertyAggBO> convert2BoList(List<ProductPropertyAgg> productPropertyAggList);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ProductPropertyAggBO> convert2BoPageList(PageSupport<ProductPropertyAgg> ps);
}
