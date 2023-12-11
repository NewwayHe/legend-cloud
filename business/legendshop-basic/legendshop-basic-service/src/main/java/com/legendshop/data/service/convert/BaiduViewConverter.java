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
import com.legendshop.data.dto.BaiduViewDTO;
import com.legendshop.data.entity.BaiduView;
import org.mapstruct.Mapper;

/**
 * 百度（移动）统计记录(BaiduView)转换器
 *
 * @author legendshop
 * @since 2021-06-19 17:32:13
 */
@Mapper
public interface BaiduViewConverter extends BaseConverter<BaiduView, BaiduViewDTO> {
}
