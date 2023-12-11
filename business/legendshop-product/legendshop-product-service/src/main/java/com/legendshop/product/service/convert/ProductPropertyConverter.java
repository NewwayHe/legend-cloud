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
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.entity.ProductProperty;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface ProductPropertyConverter extends BaseConverter<ProductProperty, ProductPropertyDTO> {

	/**
	 * to bo
	 *
	 * @param productProperty
	 * @return
	 */
	ProductPropertyBO convert2BO(ProductProperty productProperty);

	/**
	 * to bo List
	 *
	 * @param productPropertyList
	 * @return
	 */
	List<ProductPropertyBO> convert2BoList(List<ProductProperty> productPropertyList);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ProductPropertyBO> convert2BoPageList(PageSupport<ProductProperty> ps);

	/**
	 * bo to entity
	 *
	 * @param newProperty
	 * @return
	 */
	ProductProperty bo2Entity(ProductPropertyBO newProperty);

	List<ProductPropertyBO> dtoListConver2boList(List<ProductPropertyDTO> queryUserPropByProductId);

}
