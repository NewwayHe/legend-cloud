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
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author legendshop
 */
@FeignClient(contextId = "shopOrderBillApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface ShopOrderBillApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	/**
	 * 账单结算定时任务处理
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@GetMapping(PREFIX + "/shop/order/bill/calculateBillJobHandle")
	R<Void> calculateBillJobHandle(@RequestParam(value = "startDate") Date startDate, @RequestParam(value = "endDate") Date endDate);


}
