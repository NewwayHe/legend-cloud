/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.ExceptionLogLevelEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.PaySettlementDTO;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.dto.PaySettlementSuccessDTO;
import com.legendshop.pay.expetion.PayBusinessException;
import com.legendshop.pay.query.PaySettlementQuery;
import com.legendshop.pay.service.PaySettlementItemService;
import com.legendshop.pay.service.PaySettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@Tag(name = "支付单管理")
@RequiredArgsConstructor
@RequestMapping(value = "/settlement")
public class PaySettlementController {

	final PaySettlementService paySettlementService;
	final PaySettlementItemService paySettlementItemService;

	@GetMapping(value = "/success/{settlementSn}")
	@Operation(summary = "【用户】查看支付状态")
	@Parameter(name = "settlementSn", description = "支付单号", required = true)
	public R<PaySettlementSuccessDTO> success(@PathVariable String settlementSn) {
		return this.paySettlementService.success(settlementSn);
	}

	@PostMapping(value = "/order/success")
	@Operation(summary = "【用户】查看支付状态根据订单号")
	@Parameter(name = "orderNumberList", description = "支付单号", required = true)
	public R<PaySettlementSuccessDTO> orderSuccess(@RequestBody List<String> businessOrderNumberList) {
		return this.paySettlementService.orderSuccess(businessOrderNumberList);
	}


	@GetMapping(value = "/payTest/{content}")
	public R<Void> test(@PathVariable String content) {
		if (StringUtils.isNotBlank(content) && "throw".equals(content)) {
			throw new PayBusinessException("支付异常测试！", ExceptionLogLevelEnum.EMERGENCY.level(), "SN123456789", "微信支付 OPEN 错误");
		}
		return R.ok();
	}

	@PostMapping(value = "/queryPaidBySnList")
	public R<List<PaySettlementDTO>> queryPaidBySnList(@RequestBody List<String> snList) {
		return R.ok(paySettlementService.queryPaidBySnList(snList));
	}

	@GetMapping("/getPaidByOrderNumber")
	public R<PaySettlementDTO> getPaidByOrderNumber(@RequestParam("orderNumber") String orderNumber) {
		return R.ok(paySettlementService.getPaidByOrderNumber(orderNumber));
	}


	@Operation(summary = "【平台】支付单异常列表")
	@GetMapping("/querySettlementExceptionList")
	public R<PageSupport<PaySettlementItemDTO>> querySettlementExceptionList(PaySettlementQuery query) {
		return R.ok(this.paySettlementItemService.querySettlementExceptionList(query));
	}

}
