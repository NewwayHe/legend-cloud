/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.convert;

import com.legendshop.basic.dto.ExportExcelTaskDTO;
import com.legendshop.basic.entity.ExportExcelTask;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ExportExcelTaskConverter
 * @date 2022/4/26 13:40
 * @description：
 */
@Mapper
public interface ExportExcelTaskConverter extends BaseConverter<ExportExcelTask, ExportExcelTaskDTO> {
}
