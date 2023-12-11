/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.convert;

import com.legendshop.pay.dto.PaymentDTO;
import com.legendshop.pay.dto.PaymentFromDTO;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface PaymentConverter {

	/**
	 * 转换成付款参数
	 *
	 * @param paymentFromDTO
	 * @return
	 */
	PaymentDTO convert2PaymentDTO(PaymentFromDTO paymentFromDTO);
}
