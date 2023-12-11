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
import com.legendshop.data.dto.DataUserPurchasingDTO;
import com.legendshop.data.entity.DataUserPurchasing;
import org.mapstruct.Mapper;

/**
 * (DataUserPurchasing)转换器
 *
 * @author legendshop
 * @since 2021-03-23 15:41:19
 */
@Mapper
public interface DataUserPurchasingConverter extends BaseConverter<DataUserPurchasing, DataUserPurchasingDTO> {
}
