/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.data.dto.ProductViewDTO;
import com.legendshop.data.entity.ProductView;
import org.mapstruct.Mapper;

/**
 * 商品访问记录(ProductView)转换器
 *
 * @author legendshop
 * @since 2021-03-24 17:25:27
 */
@Mapper
public interface ProductViewConverter extends BaseConverter<ProductView, ProductViewDTO> {
}
