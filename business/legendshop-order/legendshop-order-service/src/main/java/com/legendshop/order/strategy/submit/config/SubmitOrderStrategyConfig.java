/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.submit.config;


import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.strategy.submit.SubmitOrderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 提交订单策略配置
 *
 * @author legendshop
 */
@Slf4j
@Configuration
public class SubmitOrderStrategyConfig {

	@Autowired
	@Qualifier("ordinarySubmitOrderStrategy")
	private SubmitOrderStrategy ordinarySubmitOrderStrategy;

	@Autowired
	@Qualifier("preSaleSubmitOrderStrategy")
	private SubmitOrderStrategy preSaleSubmitOrderStrategy;


	@Bean(name = "submitOrderStrategyMap")
	public Map<String, SubmitOrderStrategy> submitOrderStrategyMap() {
		Map<String, SubmitOrderStrategy> strategyMap = new HashedMap<>();
		strategyMap.put(OrderTypeEnum.ORDINARY.getValue(), ordinarySubmitOrderStrategy);
		strategyMap.put(OrderTypeEnum.PRE_SALE.getValue(), preSaleSubmitOrderStrategy);
		log.info("strategyMap: {}", strategyMap);
		return strategyMap;
	}

}
