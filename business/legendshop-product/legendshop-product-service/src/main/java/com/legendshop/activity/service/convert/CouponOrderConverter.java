/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.convert;

import com.legendshop.activity.dto.CouponOrderDTO;
import com.legendshop.activity.entity.CouponOrder;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

/**
 * (CouponOrder)转换器
 *
 * @author legendshop
 * @since 2022-03-25 10:45:35
 */
@Mapper
public interface CouponOrderConverter extends BaseConverter<CouponOrder, CouponOrderDTO> {
}

