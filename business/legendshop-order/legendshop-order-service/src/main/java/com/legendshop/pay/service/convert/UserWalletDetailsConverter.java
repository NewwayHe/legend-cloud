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
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.entity.UserWalletDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 用户钱包收支记录详情(UserWalletDetails)转换器
 *
 * @author legendshop
 * @since 2021-03-13 14:44:01
 */
@Mapper
public interface UserWalletDetailsConverter extends BaseConverter<UserWalletDetails, UserWalletDetailsDTO> {

	@Override
	@Mapping(target = "availableAdmount", source = "afterAmount")
	UserWalletDetailsDTO to(UserWalletDetails var1);
}

