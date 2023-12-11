/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.confirm.config;


import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.strategy.confirm.ConfirmOrderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 确认订单策略配置
 *
 * @author legendshop
 */
@Slf4j
@Configuration
public class ConfirmOrderStrategyConfig {


	@Autowired
	@Qualifier("ordinaryConfirmOrderStrategy")
	private ConfirmOrderStrategy ordinaryConfirmOrderStrategy;

	@Autowired
	@Qualifier("preSaleConfirmOrderStrategy")
	private ConfirmOrderStrategy preSaleConfirmOrderStrategy;


	@Bean(name = "confirmOrderStrategyMap")
	public Map<String, ConfirmOrderStrategy> confirmOrderStrategyMap() {
		Map<String, ConfirmOrderStrategy> strategyMap = new HashedMap<>();
		strategyMap.put(OrderTypeEnum.ORDINARY.getValue(), ordinaryConfirmOrderStrategy);
		strategyMap.put(OrderTypeEnum.PRE_SALE.getValue(), preSaleConfirmOrderStrategy);

		log.info("strategyMap: {}", strategyMap);
		return strategyMap;
	}

}
