/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.pay.dto.PaymentDTO;
import com.legendshop.pay.handler.pay.PaymentHandler;
import com.legendshop.pay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	final Map<String, PaymentHandler> paymentHandlerMap;

	@Override
	public R<String> payment(PaymentDTO payment) {
		String visitSource = payment.getVisitSource().name();
		if (SysParamNameEnum.SIMULATE_PAY.name().equals(payment.getPayTypeId()) || SysParamNameEnum.FREE_PAY.name().equals(payment.getPayTypeId())) {
			visitSource = "";
		}
		PaymentHandler paymentHandler = this.paymentHandlerMap.get(visitSource + "_" + payment.getPayTypeId());
		if (null == paymentHandler) {
			log.warn("[ payment ] : 错误的支付类型 source：{},payType:{}", visitSource, payment.getPayTypeId());
			throw new BusinessException("错误的支付类型！");
		}
		return paymentHandler.payment(payment);
	}
}
