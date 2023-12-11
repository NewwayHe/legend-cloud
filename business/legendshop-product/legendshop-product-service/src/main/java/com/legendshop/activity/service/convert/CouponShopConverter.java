/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.convert;

import com.legendshop.activity.dto.CouponShopDTO;
import com.legendshop.activity.entity.CouponShop;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface CouponShopConverter extends BaseConverter<CouponShop, CouponShopDTO> {

}
