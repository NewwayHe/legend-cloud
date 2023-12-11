/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.strategy.impl;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.pay.bo.CreatePayBO;
import com.legendshop.pay.dto.CreatePayDTO;
import com.legendshop.pay.dto.PayParamsDTO;
import com.legendshop.pay.dto.PaymentFromDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预存款充值支付业务策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public class RechargePayBusinessStrategy extends AbstractPayBusinessStrategy {

	@Override
	public R<CreatePayBO> createPrepay(CreatePayDTO createPayDTO) {
		return null;
	}

	@Override
	public R<PaymentFromDTO> initPaymentFrom(PayParamsDTO payParamsDTO) {
		return null;
	}

	@Override
	protected StringBuilder extendSubject(List<OrderDTO> orderList, StringBuilder subject) {
		return null;
	}

}
