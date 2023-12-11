/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.pay.ali;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.common.core.config.sys.params.AliPayConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.config.AliPaySDKFactory;
import com.legendshop.pay.dto.AliPaymentDTO;
import com.legendshop.pay.dto.PaymentDTO;
import com.legendshop.pay.handler.pay.PaymentHandler;
import com.legendshop.pay.service.PaySettlementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author legendshop
 */
@Slf4j
public abstract class AbstractAliPaymentHandler implements PaymentHandler {

	protected AliPayConfig config;

	protected ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	protected SysParamsApi sysParamsApi;

	@Autowired
	protected CommonProperties commonProperties;

	@Autowired
	protected PaySettlementService paySettlementService;

	@Autowired
	protected AliPaySDKFactory aliPaySDKFactory;

	@Override
	public R<String> payment(PaymentDTO payment) {
		log.info(" [ Payment Ali ] 进入支付宝支付，payment: {}", JSONUtil.toJsonStr(payment));
		R<AliPayConfig> aliPayConfigR = this.sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ALI_PAY.name(), AliPayConfig.class);
		this.config = this.objectMapper.convertValue(aliPayConfigR.getData(), AliPayConfig.class);
		if (null == config) {
			log.error("支付参数错误，Type：{}", SysParamNameEnum.ALI_PAY.name());
			return R.fail("支付参数错误！");
		}
		AliPaymentDTO aliPayment = new AliPaymentDTO();
		BeanUtils.copyProperties(payment, aliPayment);
		aliPayment.setQuitUrl(commonProperties.getServiceDomainName() + PaymentHandler.paymentEnd);
		aliPayment.setReturnUrl(commonProperties.getServiceDomainName() + PaymentHandler.synchronizeUrl);

		try {
			return R.ok(postRequest(aliPayment));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(" [ Payment Ali ] 请求支付宝失败: 参数={}", JSONUtil.toJsonStr(aliPayment));
		}
		return R.fail("请求支付宝下单失败");
	}

	@Override
	public String getNotifyUrl() {
		return commonProperties.getServiceDomainName() + PaymentHandler.notifyUrl + SysParamNameEnum.ALI_PAY.name();
	}

	/**
	 * 发送请求
	 *
	 * @Param payment
	 */
	public abstract String postRequest(AliPaymentDTO payment) throws Exception;

}
