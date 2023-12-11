/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.OrderLogisticsDTO;
import com.legendshop.order.service.OrderLogisticsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
public class OrderLogisticsApiImpl implements OrderLogisticsApi {

	private final OrderLogisticsService orderLogisticsService;

	@Override
	public R<OrderLogisticsDTO> getByOrderId(@RequestParam("orderId") Long orderId) {
		return R.ok(orderLogisticsService.getByOrderId(orderId));
	}
}
