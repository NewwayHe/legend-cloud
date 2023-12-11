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
import com.legendshop.pay.dto.BankCodeDTO;
import com.legendshop.pay.entity.BankCode;
import org.mapstruct.Mapper;

/**
 * 银行编码(BankCode)转换器
 *
 * @author legendshop
 * @since 2021-04-07 09:56:30
 */
@Mapper
public interface BankCodeConverter extends BaseConverter<BankCode, BankCodeDTO> {
}
