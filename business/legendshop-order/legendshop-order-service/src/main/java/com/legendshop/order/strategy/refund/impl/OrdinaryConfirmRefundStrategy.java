/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.refund.impl;

import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.ConfirmRefundDTO;
import com.legendshop.order.entity.OrderItem;
import com.legendshop.order.entity.OrderRefundReturn;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.service.OrderItemService;
import com.legendshop.order.strategy.refund.ConfirmRefundStrategy;
import com.legendshop.product.api.StockApi;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 普通订单退款策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public class OrdinaryConfirmRefundStrategy extends BaseConfirmRefundStrategy implements ConfirmRefundStrategy {

	@Autowired
	private StockApi stockApi;


	@Autowired
	private OrderItemService orderItemService;

	@Getter
	@Setter
	private OrderTypeEnum orderTypeEnum = OrderTypeEnum.ORDINARY;


	@Override
	public R confirmRefund(ConfirmRefundDTO confirmRefundDTO) {
		log.info("进入普通订单退款策略, params: {}", JSONUtil.toJsonStr(confirmRefundDTO));
		return super.confirmRefund(confirmRefundDTO);
	}

	@Override
	public void specialBusiness(OrderRefundReturn orderRefundReturn) {

	}

	@Override
	protected void releaseInventory(OrderItem orderItem, Long activityId) {
		stockApi.makeUpStocks(orderItem.getSkuId(), orderItem.getBasketCount());
	}


}
