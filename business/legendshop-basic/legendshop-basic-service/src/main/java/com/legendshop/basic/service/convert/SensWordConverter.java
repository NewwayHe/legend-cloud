/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.convert;

import com.legendshop.basic.dto.SensWordDTO;
import com.legendshop.basic.entity.SensWord;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

/**
 * 敏感字过滤表(SensWord)转换器
 *
 * @author legendshop
 * @since 2021-06-30 14:19:31
 */
@Mapper
public interface SensWordConverter extends BaseConverter<SensWord, SensWordDTO> {
}
