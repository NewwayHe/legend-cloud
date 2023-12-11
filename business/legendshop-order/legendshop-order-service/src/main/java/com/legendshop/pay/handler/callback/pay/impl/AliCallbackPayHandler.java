/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.callback.pay.impl;

import cn.hutool.core.util.StrUtil;
import com.alipay.easysdk.factory.Factory;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.util.HttpUtil;
import com.legendshop.pay.dto.PayNotifyDTO;
import com.legendshop.pay.handler.callback.pay.AbstractCallbackPayHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * @author legendshop
 */
@Slf4j
@Component(value = "ALI_PAY")
public class AliCallbackPayHandler extends AbstractCallbackPayHandler {

//	final Map<String, CallbackRefundHandler> refundHandler;

	@Override
	public Map<String, String> getNotifyParamMap(HttpServletRequest request, HttpServletResponse response) {
		log.info(" [ CallbackPayHandler ] [ Ali ] --------------- verifyNotifyParamMap ()---------------->");
		Map<String, String> map = HttpUtil.toMap(request);

		if (map.containsKey("refund_fee") && StrUtil.isNotEmpty(map.get("refund_fee"))) {
			return null;
		}
		return HttpUtil.toMap(request);
	}

	@Override
	public boolean verifyNotifyParamMap(Map<String, String> notifyParamMap) {
		log.info(" [ CallbackPayHandler ] [ Ali ] --------------- notifyParamMap ()---------------->");
		try {
			return Factory.Payment.Common().verifyNotify(notifyParamMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("支付宝回调参数验签失败！");
		}
	}

	@Override
	public PayNotifyDTO notifyParamsToBean(Map<String, String> notifyMap) {
		PayNotifyDTO payNotify = new PayNotifyDTO();
		String feeStr = notifyMap.get("total_amount");
		BigDecimal totalAmount = new BigDecimal(feeStr).setScale(2, RoundingMode.HALF_UP);
		payNotify.setCashAmount(totalAmount);
		payNotify.setSettlementSn(notifyMap.get("out_trade_no"));
		payNotify.setTransactionSn(notifyMap.get("trade_no"));
		String tradeStatus = notifyMap.get("trade_status");
		payNotify.setPaySuccess("TRADE_SUCCESS".equals(tradeStatus));
		payNotify.setPayType(SysParamNameEnum.ALI_PAY.name());
		return payNotify;
	}

	@Override
	public String success() {
		return "success";
	}

	@Override
	public String fail() {
		return "fail";
	}
}
