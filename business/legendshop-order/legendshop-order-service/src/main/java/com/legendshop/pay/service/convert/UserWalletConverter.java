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
import com.legendshop.pay.dto.UserWalletDTO;
import com.legendshop.pay.entity.UserWallet;
import org.mapstruct.Mapper;

/**
 * (UserWallet)转换器
 *
 * @author legendshop
 * @since 2021-03-13 14:09:48
 */
@Mapper
public interface UserWalletConverter extends BaseConverter<UserWallet, UserWalletDTO> {
}

