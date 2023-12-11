/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.convert;

import com.legendshop.activity.bo.CouponUserBO;
import com.legendshop.activity.dto.CouponUserDTO;
import com.legendshop.activity.entity.CouponUser;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface CouponUserConverter extends BaseConverter<CouponUser, CouponUserDTO> {

	/**
	 * 将 CouponUser 对象转换为 CouponUserBO 对象的方法。
	 *
	 * @param couponUser CouponUser 对象
	 * @return 对应的 CouponUserBO 对象
	 */
	CouponUserBO toBO(CouponUser couponUser);
}
