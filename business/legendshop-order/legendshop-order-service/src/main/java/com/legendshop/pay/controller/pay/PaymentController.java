/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller.pay;


import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.pay.bo.CreatePayBO;
import com.legendshop.pay.dto.CreatePayDTO;
import com.legendshop.pay.dto.PayParamsDTO;
import com.legendshop.pay.dto.PaymentSuccessDTO;
import com.legendshop.pay.service.PaySettlementService;
import com.legendshop.pay.strategy.PayBusinessStrategyContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付相关接口
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "支付相关")
public class PaymentController {

	private final HttpServletRequest request;

	private final PaySettlementService paySettlementService;

	private final PayBusinessStrategyContext payBusinessStrategyContext;

	/**
	 * 发起预支付（构建收银台）
	 */
	@Operation(summary = "发起支付", description = "构建收银台")
	@PostMapping(value = "/p/payment/toPay")
	public R<CreatePayBO> toPay(@Valid @RequestBody CreatePayDTO createPayDTO) {
		log.info("发起支付{}", createPayDTO);
		Long userId = SecurityUtils.getUserId();
		createPayDTO.setUserId(userId);
		R<CreatePayBO> result = payBusinessStrategyContext.executeCreatePrepay(createPayDTO);
		if (!result.success()) {
			log.info("发起预支付反馈: {}", result.getMsg());
			return R.fail(result.getMsg());
		}
		log.info("##### 收银台信息: {} ###", JSONUtil.toJsonStr(result.getData()));
		return R.ok(result.getData());
	}


	/**
	 * 确认支付（选择支付方式，创建支付单据，调用第三方发起真实支付）
	 */
	@Operation(summary = "确认支付", description = "选择支付方式，调用第三方发起真实支付")
	@PostMapping(value = "/p/payment/pay")
	public R<PaymentSuccessDTO> pay(@Valid @RequestBody PayParamsDTO payParamsDTO) {

		PayParamsDTO payParam = createPayParam(payParamsDTO);
		R<PaymentSuccessDTO> result = payBusinessStrategyContext.executePay(payParam);
		if (!result.success()) {
			log.info("支付反馈: {}", result.getMsg());
			return R.fail(result.getMsg());
		}
		log.info("##### 支付成功返回信息: {} ###", JSONUtil.toJsonStr(result.getData()));

		return R.ok(result.getData());
	}


	/**
	 * 创建支付参数
	 */
	private PayParamsDTO createPayParam(PayParamsDTO payParamsDTO) {
		Long userId = SecurityUtils.getUserId();
		String clientIp = JakartaServletUtil.getClientIP(request);
		payParamsDTO.setIp(clientIp);
		payParamsDTO.setUserId(userId);
		return payParamsDTO;
	}
}
