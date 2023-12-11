/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.submit;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.strategy.submit.impl.EmptySubmitOrderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 提交订单策略上下文
 *
 * @author legendshop
 */
@Slf4j
@Component
public class SubmitOrderStrategyContext {

	@Autowired
	@Qualifier("submitOrderStrategyMap")
	private Map<String, SubmitOrderStrategy> strategyMap;

	public R<Object> executeStrategy(ConfirmOrderBO confirmOrderBO) {
		OrderTypeEnum orderTypeEnum = confirmOrderBO.getType();
		log.info("strategyMap: {}", strategyMap);
		SubmitOrderStrategy strategy = strategyMap.get(orderTypeEnum.getValue());
		if (null == strategy) {
			strategy = new EmptySubmitOrderStrategy();
		}
		return strategy.submit(confirmOrderBO);
	}
}
