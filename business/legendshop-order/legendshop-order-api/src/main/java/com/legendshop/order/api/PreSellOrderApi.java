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
import com.legendshop.order.dto.PreSellOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "preSellOrderApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface PreSellOrderApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;


	@PostMapping(PREFIX + "/pre-sell/order/queryByOrderIds")
	R<List<PreSellOrderDTO>> queryByOrderIds(@RequestBody List<Long> orderIds);

	@PutMapping(PREFIX + "/pre-sell/order/update")
	R<Integer> update(@RequestBody List<PreSellOrderDTO> preSellOrderList);

	@PutMapping(PREFIX + "/pre-sell/order/updateOne")
	R<Integer> update(@RequestBody PreSellOrderDTO preSellOrderDTO);

	@GetMapping(PREFIX + "/pre-sell/order/getByOrderId")
	R<PreSellOrderDTO> getByOrderId(@RequestParam(value = "orderId") Long orderId);

}
