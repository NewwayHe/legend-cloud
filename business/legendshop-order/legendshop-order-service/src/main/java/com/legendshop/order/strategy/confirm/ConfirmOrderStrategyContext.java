/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.confirm;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.ConfirmOrderDTO;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.strategy.confirm.impi.EmptyConfirmOrderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 确认订单策略上下文
 *
 * @author legendshop
 */
@Slf4j
@Component
public class ConfirmOrderStrategyContext {


	@Autowired
	@Qualifier("confirmOrderStrategyMap")
	private Map<String, ConfirmOrderStrategy> strategyMap;


	/**
	 * 下单检查以及组装商品信息
	 *
	 * @param confirmOrderDTO
	 * @return
	 */
	public R<ConfirmOrderBO> executeCheckStrategy(ConfirmOrderDTO confirmOrderDTO) {
		OrderTypeEnum orderTypeEnum = confirmOrderDTO.getOrderType();
		log.info("strategyMap: {}", strategyMap);

		ConfirmOrderStrategy strategy = strategyMap.get(orderTypeEnum.getValue());
		if (null == strategy) {
			strategy = new EmptyConfirmOrderStrategy();
		}
		return strategy.check(confirmOrderDTO);
	}


	/**
	 * 确认订单
	 *
	 * @param confirmOrderBo
	 * @return
	 */
	public R<ConfirmOrderBO> executeConfirmStrategy(ConfirmOrderBO confirmOrderBo) {
		OrderTypeEnum orderTypeEnum = confirmOrderBo.getType();
		log.info("strategyMap: {}", strategyMap);

		ConfirmOrderStrategy strategy = strategyMap.get(orderTypeEnum.getValue());
		if (null == strategy) {
			strategy = new EmptyConfirmOrderStrategy();
		}
		return strategy.confirm(confirmOrderBo);
	}

	/**
	 * 处理特殊业务
	 *
	 * @param confirmOrderBO
	 * @return
	 */
	public R<ConfirmOrderBO> executeSpecificBusinessStrategy(ConfirmOrderBO confirmOrderBO) {
		OrderTypeEnum orderTypeEnum = confirmOrderBO.getType();
		log.info("strategyMap: {}", strategyMap);

		ConfirmOrderStrategy strategy = strategyMap.get(orderTypeEnum.getValue());
		if (null == strategy) {
			strategy = new EmptyConfirmOrderStrategy();
		}
		return strategy.handleSpecificBusiness(confirmOrderBO);
	}
}
