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
import com.legendshop.product.dto.ProductPropertyParamGroupDTO;
import com.legendshop.product.entity.ProductPropertyParamGroup;
import org.mapstruct.Mapper;

/**
 * 参数组-参数属性转换器
 *
 * @author legendshop
 */
@Mapper
public interface ProductPropertyParamGroupConverter extends BaseConverter<ProductPropertyParamGroup, ProductPropertyParamGroupDTO> {

}
