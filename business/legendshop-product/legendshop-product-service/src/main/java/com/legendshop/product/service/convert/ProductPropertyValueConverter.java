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
import com.legendshop.product.bo.ProductPropertyValueBO;
import com.legendshop.product.dto.ProductPropertyValueDTO;
import com.legendshop.product.entity.ProductPropertyValue;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface ProductPropertyValueConverter extends BaseConverter<ProductPropertyValue, ProductPropertyValueDTO> {

	/**
	 * to bo
	 *
	 * @param productPropertyValue
	 * @return
	 */
	ProductPropertyValueBO convert2BO(ProductPropertyValue productPropertyValue);

	/**
	 * to bo List
	 *
	 * @param productPropertyValues
	 * @return
	 */
	List<ProductPropertyValueBO> convert2BoList(List<ProductPropertyValue> productPropertyValues);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ProductPropertyValueBO> convert2BoPageList(PageSupport<ProductPropertyValue> ps);
}
