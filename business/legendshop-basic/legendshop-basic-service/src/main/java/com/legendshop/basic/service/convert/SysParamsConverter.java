/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.convert;

import com.legendshop.basic.dto.SysParamsDTO;
import com.legendshop.basic.entity.SysParams;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

/**
 * (SysParams)转换器
 *
 * @author legendshop
 * @since 2020-08-28 12:00:46
 */
@Mapper
public interface SysParamsConverter extends BaseConverter<SysParams, SysParamsDTO> {
}
