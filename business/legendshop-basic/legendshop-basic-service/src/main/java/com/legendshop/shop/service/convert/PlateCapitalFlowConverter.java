/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.shop.dto.PlateCapitalFlowDTO;
import com.legendshop.shop.entity.PlateCapitalFlow;
import com.legendshop.shop.excel.PlateCapitalFlowExportDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 平台资金流水(PlateCapitalFlow)转换器
 *
 * @author legendshop
 * @since 2020-09-18 17:26:10
 */
@Mapper
public interface PlateCapitalFlowConverter extends BaseConverter<PlateCapitalFlow, PlateCapitalFlowDTO> {

	List<PlateCapitalFlowExportDTO> toExportDTO(List<PlateCapitalFlowDTO> plateCapitalFlowDtoList);
}
