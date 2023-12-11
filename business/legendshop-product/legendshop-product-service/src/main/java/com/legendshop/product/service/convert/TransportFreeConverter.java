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
import com.legendshop.product.dto.TransFreeDTO;
import com.legendshop.product.entity.TransFree;
import org.mapstruct.Mapper;

/**
 * 条件包邮转换器
 *
 * @author legendshop
 */
@Mapper
public interface TransportFreeConverter extends BaseConverter<TransFree, TransFreeDTO> {
}
