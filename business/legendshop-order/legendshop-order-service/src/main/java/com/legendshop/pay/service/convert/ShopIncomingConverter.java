/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.pay.dto.ShopIncomingDTO;
import com.legendshop.pay.entity.ShopIncoming;
import org.mapstruct.Mapper;

/**
 * 商家进件表(ShopIncoming)转换器
 *
 * @author legendshop
 * @since 2021-03-12 09:27:59
 */
@Mapper
public interface ShopIncomingConverter extends BaseConverter<ShopIncoming, ShopIncomingDTO> {
}
