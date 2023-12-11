/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.pay.simulate;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.common.core.config.sys.params.SimulatePayConfigDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.util.SignerHelper;
import com.legendshop.pay.dto.PaymentDTO;
import com.legendshop.pay.dto.SimulatePayNotifyDTO;
import com.legendshop.pay.handler.pay.PaymentHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
@Component("_SIMULATE_PAY")
public class SimulatePaymentHandler implements PaymentHandler {

	final CommonProperties commonProperties;

	final SysParamsApi sysParamsApi;

	@Override
	public R<String> payment(PaymentDTO payment) {

		// 处理拆分为微服务后，强转失败问题
		ObjectMapper mapper = new ObjectMapper();
		SimulatePayConfigDTO simulatePayConfig = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.SIMULATE_PAY.name(), SimulatePayConfigDTO.class).getData(), SimulatePayConfigDTO.class);
		if (null == simulatePayConfig.getEnabled() || !simulatePayConfig.getEnabled()) {
			log.error(" [ Payment Simulate ] 非法调用模拟支付 ");
			return R.fail("模拟支付失败");
		}

		// 组装回调参数
		SimulatePayNotifyDTO notifyDTO = new SimulatePayNotifyDTO();
		notifyDTO.setSettlementSn(payment.getNumber());
		notifyDTO.setPaySuccess(Boolean.TRUE);
		notifyDTO.setCashAmount(payment.getAmount());
		notifyDTO.setTransactionSn(RandomStringUtils.randomNumeric(8));
		notifyDTO.setPayType(SysParamNameEnum.SIMULATE_PAY.name());
		notifyDTO.setValidTime(System.currentTimeMillis());
		notifyDTO.setSign(SignerHelper.getSign(BeanUtil.beanToMap(notifyDTO), simulatePayConfig.getKey()));

		log.info(" [ Payment Simulate ]  平台余额支付模式  发起支付成功的回调 url: {}, 参数:{}  ", getNotifyUrl(), JSONUtil.toJsonStr(notifyDTO));
		//http://127.0.0.1:9999/pay/payNotify/notify/SIMULATE_PAY
		String res = HttpUtil.post(getNotifyUrl(), BeanUtil.beanToMap(notifyDTO));
		log.info(" [ Payment Simulate ]  平台余额支付模式 异步回调响应 {} ", res);
		return R.ok(res);
	}

	@Override
	public String getNotifyUrl() {
		return commonProperties.getServiceDomainName() + notifyUrl + SysParamNameEnum.SIMULATE_PAY.name();
	}

}
