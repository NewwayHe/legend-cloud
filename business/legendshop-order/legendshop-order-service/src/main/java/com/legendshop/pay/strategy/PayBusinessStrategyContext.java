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
import com.legendshop.pay.strategy.impl.EmptyPayBusinessOrderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 支付业务策略上下文
 *
 * @author legendshop
 */
@Slf4j
@Component
public class PayBusinessStrategyContext {

	@Autowired
	@Qualifier("payBusinessStrategyMap")
	private Map<String, PayBusinessStrategy> strategyMap;

	public R<CreatePayBO> executeCreatePrepay(CreatePayDTO createPayDTO) {
		log.info("strategyMap: {}", strategyMap);
		PayBusinessStrategy strategy = strategyMap.get(createPayDTO.getSettlementType());
		if (null == strategy) {
			strategy = new EmptyPayBusinessOrderStrategy();
		}
		return strategy.createPrepay(createPayDTO);
	}

	public R<PaymentSuccessDTO> executePay(PayParamsDTO payParamsDTO) {
		log.info("strategyMap: {}", strategyMap);
		PayBusinessStrategy strategy = strategyMap.get(payParamsDTO.getSettlementType());
		if (null == strategy) {
			strategy = new EmptyPayBusinessOrderStrategy();
		}
		return strategy.pay(payParamsDTO);
	}
}
