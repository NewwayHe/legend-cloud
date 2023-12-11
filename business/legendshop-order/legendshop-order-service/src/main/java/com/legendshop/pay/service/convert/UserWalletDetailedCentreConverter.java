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
import com.legendshop.pay.dto.UserWalletDetailedCentreDTO;
import com.legendshop.pay.entity.UserWalletDetailedCentre;
import org.mapstruct.Mapper;

/**
 * 用户钱包详情中间表(UserWalletDetailedCentre)转换器
 *
 * @author legendshop
 * @since 2022-04-27 13:56:52
 */
@Mapper
public interface UserWalletDetailedCentreConverter extends BaseConverter<UserWalletDetailedCentre, UserWalletDetailedCentreDTO> {
}

