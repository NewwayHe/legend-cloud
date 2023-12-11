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
import com.legendshop.shop.dto.AdvertiseCountDTO;
import com.legendshop.shop.entity.AdvertiseCount;
import org.mapstruct.Mapper;

/**
 * (AdvertiseCount)转换器
 *
 * @author legendshop
 * @since 2022-04-27 17:33:12
 */
@Mapper
public interface AdvertiseCountConverter extends BaseConverter<AdvertiseCount, AdvertiseCountDTO> {
}

