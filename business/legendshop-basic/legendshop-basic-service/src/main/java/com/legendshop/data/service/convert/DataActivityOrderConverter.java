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
import com.legendshop.data.dto.DataActivityOrderDTO;
import com.legendshop.data.entity.DataActivityOrder;
import org.mapstruct.Mapper;

/**
 * 活动订单表(DataActivityOrder)转换器
 *
 * @author legendshop
 * @since 2021-07-16 14:49:55
 */
@Mapper
public interface DataActivityOrderConverter extends BaseConverter<DataActivityOrder, DataActivityOrderDTO> {
}
