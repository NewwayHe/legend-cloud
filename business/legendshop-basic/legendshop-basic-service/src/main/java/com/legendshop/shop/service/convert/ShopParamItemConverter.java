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
import com.legendshop.shop.dto.ShopParamItemDTO;
import com.legendshop.shop.entity.ShopParamItem;
import org.mapstruct.Mapper;

/**
 * 商家配置项(ShopParamItem)转换器
 *
 * @author legendshop
 * @since 2020-11-03 11:03:08
 */
@Mapper
public interface ShopParamItemConverter extends BaseConverter<ShopParamItem, ShopParamItemDTO> {
}
