/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.pay.free;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.common.core.config.sys.params.FreePayConfigDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.util.SignerHelper;
import com.legendshop.pay.dto.FreePayNotifyDTO;
import com.legendshop.pay.dto.PaymentDTO;
import com.legendshop.pay.handler.pay.PaymentHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
@Component("_FREE_PAY")
public class FreePaymentHandler implements PaymentHandler {

	final CommonProperties commonProperties;

	final SysParamsApi sysParamsApi;

	@Override
	public R<String> payment(PaymentDTO payment) {
		ObjectMapper mapper = new ObjectMapper();
		FreePayConfigDTO freePayConfigDTO = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.FREE_PAY.name(), FreePayConfigDTO.class).getData(), FreePayConfigDTO.class);
		if (null == freePayConfigDTO.getEnabled() || !freePayConfigDTO.getEnabled()) {
			log.error(" [ Payment Free ] 非法调用！");
			return R.fail("非法调用！");
		}

		if (BigDecimal.ZERO.compareTo(payment.getAmount()) != 0) {
			log.error(" [ Payment Free ] 支付金额大于零！");
			return R.fail("支付金额大于零！");
		}

		// 组装回调参数
		FreePayNotifyDTO notifyDTO = new FreePayNotifyDTO();
		notifyDTO.setSettlementSn(payment.getNumber());
		notifyDTO.setPaySuccess(Boolean.TRUE);
		notifyDTO.setCashAmount(payment.getAmount());
		notifyDTO.setTransactionSn(RandomStringUtils.randomNumeric(8));
		notifyDTO.setPayType(SysParamNameEnum.FREE_PAY.name());
		notifyDTO.setValidTime(System.currentTimeMillis());
		notifyDTO.setSign(SignerHelper.getSign(BeanUtil.beanToMap(notifyDTO), freePayConfigDTO.getKey()));

		log.info(" [ Payment Free ]  平台免付  发起支付成功的回调 url: {}, 参数:{}  ", getNotifyUrl(), JSONUtil.toJsonStr(notifyDTO));
		String res = HttpUtil.post(getNotifyUrl(), BeanUtil.beanToMap(notifyDTO));
		log.info(" [ Payment Free ]  平台免付 异步回调响应 {} ", res);
		return R.ok(res);
	}

	@Override
	public String getNotifyUrl() {
		return commonProperties.getServiceDomainName() + PaymentHandler.notifyUrl + SysParamNameEnum.FREE_PAY.name();
	}
}
