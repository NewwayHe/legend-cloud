/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.entity.PaySettlement;
import com.legendshop.pay.handler.callback.pay.CallbackPayHandler;
import com.legendshop.pay.service.PaySettlementBusinessService;
import com.legendshop.pay.service.PaySettlementItemService;
import com.legendshop.pay.service.PaySettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySettlementBusinessServiceImpl implements PaySettlementBusinessService {

	final PaySettlementService paySettlementService;
	final PaySettlementItemService paySettlementItemService;
	final Map<String, CallbackPayHandler> payHandler;

	@Override
	public R<Void> paySuccessCompensateJobHandle() {

		List<PaySettlement> paySettlements = paySettlementService.queryPaySuccessfulButOrderUnPaid();
		if (CollUtil.isEmpty(paySettlements)) {
			log.info("paySuccessCompensate-JOB，未找到对应订单，处理完成");
			return R.ok();
		}

		CallbackPayHandler callbackPayHandler = payHandler.get("ALI_PAY");
		for (PaySettlement paySettlement : paySettlements) {
			log.info("paySuccessCompensate-JOB，开始处理支付订单: {}", paySettlement.getPaySettlementSn());
			List<PaySettlementItemDTO> paySettlementItemDTOS = paySettlementItemService.queryBySn(paySettlement.getPaySettlementSn());
			try {
				callbackPayHandler.callbackBusinessHandle(paySettlementItemDTOS);
			} catch (Exception e) {
				log.error("paySuccessCompensate-JOB，回调业务处理异常，{}", e.toString());
			}
			log.info("paySuccessCompensate-JOB，支付订单处理完成");
		}
		log.info("paySuccessCompensate-JOB，处理支付成功但处于待支付的订单完成");
		return R.ok();
	}
}
