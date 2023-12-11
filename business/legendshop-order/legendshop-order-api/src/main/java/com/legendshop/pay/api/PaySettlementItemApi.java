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
import com.legendshop.pay.dto.PaySettlementItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "paySettlementItemApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface PaySettlementItemApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	@GetMapping(value = PREFIX + "/settlement/item/queryBySn")
	R<List<PaySettlementItemDTO>> queryBySn(@RequestParam(value = "sn") String sn);

	@PostMapping(value = PREFIX + "/settlement/item/queryBySnList")
	R<List<PaySettlementItemDTO>> queryBySnList(@RequestBody List<String> sn);

	/**
	 * 根据订单号获取已支付的支付项
	 *
	 * @param orderNumber
	 * @return
	 */
	@GetMapping(value = PREFIX + "/settlement/item/queryPaidByOrderNumber")
	R<List<PaySettlementItemDTO>> queryPaidByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber);
}
