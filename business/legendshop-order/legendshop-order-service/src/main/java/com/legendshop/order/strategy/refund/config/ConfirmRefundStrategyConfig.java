/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.refund.config;

import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.strategy.refund.ConfirmRefundStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 平台确认退款策略配置
 *
 * @author legendshop
 */
@Slf4j
@Configuration
public class ConfirmRefundStrategyConfig {


	@Autowired
	@Qualifier("ordinaryConfirmRefundStrategy")
	private ConfirmRefundStrategy ordinaryConfirmRefundStrategy;

	@Autowired
	@Qualifier("preSaleConfirmRefundStrategy")
	private ConfirmRefundStrategy preSaleConfirmRefundStrategy;


	@Bean(name = "confirmRefundStrategyMap")
	public Map<String, ConfirmRefundStrategy> confirmRefundStrategyMap() {
		Map<String, ConfirmRefundStrategy> strategyMap = new HashedMap<>();
		strategyMap.put(OrderTypeEnum.ORDINARY.getValue(), ordinaryConfirmRefundStrategy);
		strategyMap.put(OrderTypeEnum.PRE_SALE.getValue(), preSaleConfirmRefundStrategy);
		log.info("strategyMap: {}", strategyMap);
		return strategyMap;
	}
}
