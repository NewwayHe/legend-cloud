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
import com.legendshop.shop.dto.IndustryDirectoryDTO;
import com.legendshop.shop.entity.IndustryDirectory;
import org.mapstruct.Mapper;

/**
 * 行业目录(IndustryDirectory)转换器
 *
 * @author legendshop
 * @since 2021-03-09 13:53:14
 */
@Mapper
public interface IndustryDirectoryConverter extends BaseConverter<IndustryDirectory, IndustryDirectoryDTO> {
}

