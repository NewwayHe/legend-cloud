/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.pay.dto.PaySettlementOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "paySettlementOrderApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface PaySettlementOrderApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	/**
	 * 根据订单号查询对应支付单编号
	 *
	 * @param orderNumber
	 * @return
	 */
	@GetMapping(value = PREFIX + "/settlement/order/queryByOrderNumber")
	R<List<PaySettlementOrderDTO>> querySnByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber);

}
