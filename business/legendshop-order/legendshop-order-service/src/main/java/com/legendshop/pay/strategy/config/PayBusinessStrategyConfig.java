/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.strategy.config;


import com.legendshop.pay.enums.SettlementTypeEnum;
import com.legendshop.pay.strategy.PayBusinessStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 提交订单策略配置
 *
 * @author legendshop
 */
@Slf4j
@Configuration
public class PayBusinessStrategyConfig {


	@Autowired
	@Qualifier("ordinaryOrderPayBusinessStrategy")
	private PayBusinessStrategy orderPayBusinessStrategy;

	@Autowired
	@Qualifier("rechargePayBusinessStrategy")
	private PayBusinessStrategy rechargePayBusinessStrategy;

	@Autowired
	@Qualifier("preSellDepositPayBusinessStrategy")
	private PayBusinessStrategy preSellDepositPayBusinessStrategy;

	@Autowired
	@Qualifier("preSellFinalPayBusinessStrategy")
	private PayBusinessStrategy preSellFinalPayBusinessStrategy;


	@Bean(name = "payBusinessStrategyMap")
	public Map<String, PayBusinessStrategy> payBusinessStrategyMap() {
		Map<String, PayBusinessStrategy> strategyMap = new HashMap<>(16);

		strategyMap.put(SettlementTypeEnum.ORDINARY_ORDER.getValue(), orderPayBusinessStrategy);
		strategyMap.put(SettlementTypeEnum.USER_RECHARGE.getValue(), rechargePayBusinessStrategy);
		strategyMap.put(SettlementTypeEnum.PRE_SALE_ORDER_DEPOSIT.getValue(), preSellDepositPayBusinessStrategy);
		strategyMap.put(SettlementTypeEnum.PRE_SALE_ORDER_FINAL.getValue(), preSellFinalPayBusinessStrategy);
		log.info("strategyMap: {}", strategyMap);
		return strategyMap;
	}

}
