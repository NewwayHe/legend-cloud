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
import com.legendshop.order.dto.OrderImportLogisticsDTO;
import com.legendshop.order.entity.OrderImportLogistics;
import org.mapstruct.Mapper;

/**
 * (OrderImportLogistics)转换器
 *
 * @author legendshop
 * @since 2022-04-25 14:13:35
 */
@Mapper
public interface OrderImportLogisticsConverter extends BaseConverter<OrderImportLogistics, OrderImportLogisticsDTO> {
}

