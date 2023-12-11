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
import com.legendshop.order.dto.OrderImportLogisticsDetailDTO;
import com.legendshop.order.entity.OrderImportLogisticsDetail;
import com.legendshop.order.excel.OrderImportLogisticsExportDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * (OrderImportLogisticsDetail)转换器
 *
 * @author legendshop
 * @since 2022-04-25 14:12:18
 */
@Mapper
public interface OrderImportLogisticsDetailConverter extends BaseConverter<OrderImportLogisticsDetail, OrderImportLogisticsDetailDTO> {

	List<OrderImportLogisticsExportDTO> toExport(List<OrderImportLogisticsDetail> entityList);
}

