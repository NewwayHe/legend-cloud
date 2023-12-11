/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import com.legendshop.pay.dto.PaySettlementDTO;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.handler.business.CallbackBusinessHandler;
import com.legendshop.pay.service.PaySettlementItemService;
import com.legendshop.pay.service.PaySettlementService;
import com.legendshop.pay.service.PaymentBusinessService;
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
public class PaymentBusinessServiceImpl implements PaymentBusinessService {

	final PaySettlementService paySettlementService;

	final PaySettlementItemService paySettlementItemService;

	final Map<String, CallbackBusinessHandler> callbackBusinessHandlerMap;

	@Override
	public void callbackBusinessHandle(PaySettlementItemDTO item) {
		// 用于支付单类型查询
		PaySettlementDTO settlement = this.paySettlementService.getBySn(item.getPaySettlementSn());
		String useType = settlement.getUseType();
		// 回调业务处理
		CallbackBusinessHandler callbackBusinessHandler = this.callbackBusinessHandlerMap.get(useType + "_CALLBACK");
		callbackBusinessHandler.handler(settlement);
	}
}
