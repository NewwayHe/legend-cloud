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
import com.legendshop.data.dto.ShopViewDTO;
import com.legendshop.data.entity.ShopView;
import org.mapstruct.Mapper;

/**
 * (ShopView)转换器
 *
 * @author legendshop
 * @since 2021-06-17 13:43:43
 */
@Mapper
public interface ShopViewConverter extends BaseConverter<ShopView, ShopViewDTO> {
}
