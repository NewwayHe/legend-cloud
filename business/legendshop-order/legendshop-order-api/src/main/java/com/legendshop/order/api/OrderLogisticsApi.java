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
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.order.dto.OrderLogisticsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author legendshop
 */
@FeignClient(contextId = "orderLogisticsApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface OrderLogisticsApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;


	@GetMapping(PREFIX + "/logistics/getByOrderId")
	R<OrderLogisticsDTO> getByOrderId(@RequestParam("orderId") Long orderId);
}
