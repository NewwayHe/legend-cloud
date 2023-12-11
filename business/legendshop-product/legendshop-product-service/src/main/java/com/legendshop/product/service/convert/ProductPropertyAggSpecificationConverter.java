/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.product.bo.ProductPropertyAggSpecificationBO;
import com.legendshop.product.dto.ProductPropertyAggSpecificationDTO;
import com.legendshop.product.entity.ProductPropertyAggSpecification;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface ProductPropertyAggSpecificationConverter extends BaseConverter<ProductPropertyAggSpecification, ProductPropertyAggSpecificationDTO> {

	/**
	 * to bo
	 *
	 * @param productPropertyAggSpecification
	 * @return
	 */
	ProductPropertyAggSpecificationBO convert2BO(ProductPropertyAggSpecification productPropertyAggSpecification);
}
