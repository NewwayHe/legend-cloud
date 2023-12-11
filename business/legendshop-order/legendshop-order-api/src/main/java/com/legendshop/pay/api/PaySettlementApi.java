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
import com.legendshop.pay.dto.PaySettlementDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "paySettlementApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface PaySettlementApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	@PostMapping(value = PREFIX + "/settlement/queryPaidBySnList")
	R<List<PaySettlementDTO>> queryPaidBySnList(@RequestBody List<String> snList);

	/**
	 * 根据订单号查询支付成功的订单
	 *
	 * @param orderNumber
	 * @return
	 */
	@GetMapping(PREFIX + "/settlement/getPaidByOrderNumber")
	R<PaySettlementDTO> getPaidByOrderNumber(@RequestParam("orderNumber") String orderNumber);

	/**
	 * 处理支付成功但处于待支付的订单
	 *
	 * @return
	 */
	@GetMapping(PREFIX + "/settlement/paySuccessCompensateJobHandle")
	R<Void> paySuccessCompensateJobHandle();
}
