/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.PaymentDTO;

/**
 * @author legendshop
 */
public interface PaymentService {

	R<String> payment(PaymentDTO payment);

}
