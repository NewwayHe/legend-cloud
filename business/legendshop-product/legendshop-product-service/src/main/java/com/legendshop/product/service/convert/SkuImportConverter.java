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
import com.legendshop.product.dto.ProductImportDetailDTO;
import com.legendshop.product.dto.SkuImportDTO;
import org.mapstruct.Mapper;

/**
 * (OrderImportLogistics)转换器
 *
 * @author legendshop
 * @since 2022-04-25 14:13:35
 */
@Mapper
public interface SkuImportConverter extends BaseConverter<SkuImportDTO, ProductImportDetailDTO> {
}

