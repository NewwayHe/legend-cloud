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
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.service.PaySettlementItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PaySettlementItemApiImpl implements PaySettlementItemApi {

	final PaySettlementItemService paySettlementItemService;

	@Override
	public R<List<PaySettlementItemDTO>> queryBySn(@RequestParam(value = "sn") String sn) {
		return R.ok(this.paySettlementItemService.queryBySn(sn));
	}

	@Override
	public R<List<PaySettlementItemDTO>> queryBySnList(@RequestBody List<String> sn) {
		return R.ok(paySettlementItemService.queryBySnList(sn));
	}

	@Override
	public R<List<PaySettlementItemDTO>> queryPaidByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber) {
		return paySettlementItemService.queryPaidByOrderNumber(orderNumber);
	}
}
