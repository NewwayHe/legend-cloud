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
import com.legendshop.product.bo.ProductPropertyAggParamBO;
import com.legendshop.product.dto.ProductPropertyAggParamDTO;
import com.legendshop.product.entity.ProductPropertyAggParam;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface ProductPropertyAggParamConverter extends BaseConverter<ProductPropertyAggParam, ProductPropertyAggParamDTO> {

	/**
	 * to bo
	 *
	 * @param productPropertyAggParam
	 * @return
	 */
	ProductPropertyAggParamBO convert2BO(ProductPropertyAggParam productPropertyAggParam);
}
