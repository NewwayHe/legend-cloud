/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.business.order.impl;

import com.legendshop.order.dto.OrderDTO;
import com.legendshop.pay.handler.business.order.AbstractOrderCallbackBusinessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
@Component("ORDINARY_ORDER_CALLBACK")
public class OrdinaryOrderCallbackBusinessHandler extends AbstractOrderCallbackBusinessHandler {

	@Override
	public List<OrderDTO> orderHandler(List<OrderDTO> orderList) {
		return orderList;
	}

	@Override
	public void specialBusiness(List<OrderDTO> orderList) {
		super.commissionHandler(orderList);
		log.info(" [ Ordinary Order Callback ] SUCCESS 普通订单支付处理完成！");
	}
}
