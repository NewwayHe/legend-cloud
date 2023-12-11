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
import com.legendshop.order.dto.PreSellOrderDTO;
import com.legendshop.order.service.PreSellOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
public class PreSellOrderApiImpl implements PreSellOrderApi {

	private final PreSellOrderService preSellOrderService;

	@Override
	public R<Integer> update(@RequestBody List<PreSellOrderDTO> preSellOrderList) {
		return R.ok(preSellOrderService.update(preSellOrderList));
	}

	@Override
	public R<Integer> update(@RequestBody PreSellOrderDTO preSellOrderDTO) {
		return R.ok(preSellOrderService.update(preSellOrderDTO));
	}


	@Override
	public R<PreSellOrderDTO> getByOrderId(@RequestParam("orderId") Long orderId) {
		return R.ok(preSellOrderService.getByOrderId(orderId));
	}

	@Override
	public R<List<PreSellOrderDTO>> queryByOrderIds(@RequestBody List<Long> orderIds) {
		return R.ok(preSellOrderService.queryByOrderIds(orderIds));
	}
}
