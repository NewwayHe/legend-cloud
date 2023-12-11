/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.order.dto.OrderLogisticsDTO;
import com.legendshop.order.entity.OrderLogistics;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface OrderLogisticsConverter extends BaseConverter<OrderLogistics, OrderLogisticsDTO> {
}
