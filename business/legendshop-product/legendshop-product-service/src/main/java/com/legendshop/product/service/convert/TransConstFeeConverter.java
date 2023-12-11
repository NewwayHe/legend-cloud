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
import com.legendshop.product.dto.TransConstFeeDTO;
import com.legendshop.product.entity.TransConstFee;
import org.mapstruct.Mapper;

/**
 * 固定运费(TransConstFee)转换器
 *
 * @author legendshop
 * @since 2020-09-07 14:43:46
 */
@Mapper
public interface TransConstFeeConverter extends BaseConverter<TransConstFee, TransConstFeeDTO> {
}
