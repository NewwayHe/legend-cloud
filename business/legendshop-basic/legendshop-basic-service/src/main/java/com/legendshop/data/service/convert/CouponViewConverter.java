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
import com.legendshop.data.dto.CouponViewDTO;
import com.legendshop.data.entity.CouponView;
import org.mapstruct.Mapper;

/**
 * (CouponView)转换器
 *
 * @author legendshop
 * @since 2022-04-06 11:49:51
 */
@Mapper
public interface CouponViewConverter extends BaseConverter<CouponView, CouponViewDTO> {
}

