/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.refund;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.ConfirmRefundDTO;
import com.legendshop.order.entity.OrderRefundReturn;
import com.legendshop.order.strategy.refund.impl.EmptyConfirmRefundStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 平台确认退款策略上下文
 *
 * @author legendshop
 */
@Slf4j
@Component
public class ConfirmRefundStrategyContext {

	@Autowired
	@Qualifier("confirmRefundStrategyMap")
	private Map<String, ConfirmRefundStrategy> strategyMap;

	public R executeStrategy(ConfirmRefundDTO confirmRefundDTO) {
		log.info("strategyMap: {}", strategyMap);
		ConfirmRefundStrategy strategy = strategyMap.get(confirmRefundDTO.getOrderType());
		if (null == strategy) {
			strategy = new EmptyConfirmRefundStrategy();
		}

		return strategy.confirmRefund(confirmRefundDTO);
	}

	public R<Void> executeRefundHandlerStrategy(OrderRefundReturn orderRefundReturn) {
		log.info("strategyMap: {}", strategyMap);
		ConfirmRefundStrategy strategy = strategyMap.get(orderRefundReturn.getOrderType());
		if (null == strategy) {
			strategy = new EmptyConfirmRefundStrategy();
		}
		return strategy.refundHandler(orderRefundReturn);
	}

	public R<Void> callbackStrategy(String orderType, String refundSn) {
		log.info("strategyMap: {}", strategyMap);
		ConfirmRefundStrategy strategy = strategyMap.get(orderType);
		if (null == strategy) {
			strategy = new EmptyConfirmRefundStrategy();
		}
		return strategy.refundCallback(refundSn);
	}
}
