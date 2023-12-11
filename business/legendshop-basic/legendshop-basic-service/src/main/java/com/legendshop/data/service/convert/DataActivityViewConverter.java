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
import com.legendshop.data.dto.DataActivityViewDTO;
import com.legendshop.data.entity.DataActivityView;
import org.mapstruct.Mapper;

/**
 * (DataActivityView)转换器
 *
 * @author legendshop
 * @since 2021-07-07 16:14:24
 */
@Mapper
public interface DataActivityViewConverter extends BaseConverter<DataActivityView, DataActivityViewDTO> {
}
