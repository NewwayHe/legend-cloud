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
import com.legendshop.pay.dto.PaySettlementDTO;
import com.legendshop.pay.service.PaySettlementBusinessService;
import com.legendshop.pay.service.PaySettlementItemService;
import com.legendshop.pay.service.PaySettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class PaySettlementApiImpl implements PaySettlementApi {

	final PaySettlementService paySettlementService;
	final PaySettlementItemService paySettlementItemService;
	final PaySettlementBusinessService paySettlementBusinessService;

	@Override
	public R<List<PaySettlementDTO>> queryPaidBySnList(@RequestBody List<String> snList) {
		return R.ok(paySettlementService.queryPaidBySnList(snList));
	}

	@Override
	public R<PaySettlementDTO> getPaidByOrderNumber(@RequestParam("orderNumber") String orderNumber) {
		return R.ok(paySettlementService.getPaidByOrderNumber(orderNumber));
	}

	@Override
	public R<Void> paySuccessCompensateJobHandle() {
		return paySettlementBusinessService.paySuccessCompensateJobHandle();
	}
}
