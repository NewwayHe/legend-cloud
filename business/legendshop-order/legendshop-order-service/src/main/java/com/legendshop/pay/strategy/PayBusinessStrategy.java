/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.strategy;


import com.legendshop.common.core.constant.R;
import com.legendshop.pay.bo.CreatePayBO;
import com.legendshop.pay.dto.CreatePayDTO;
import com.legendshop.pay.dto.PayParamsDTO;
import com.legendshop.pay.dto.PaymentSuccessDTO;

/**
 * 支付业务策略
 *
 * @author legendshop
 */
public interface PayBusinessStrategy {

	/**
	 * 发起预支付，构建收银台信息
	 *
	 * @param createPayDTO
	 * @return
	 */
	R<CreatePayBO> createPrepay(CreatePayDTO createPayDTO);


	/**
	 * 确认支付（选择支付方式，创建支付单据，调用第三方发起真实支付）
	 *
	 * @param payParamsDTO
	 * @return
	 */
	R<PaymentSuccessDTO> pay(PayParamsDTO payParamsDTO);
}
