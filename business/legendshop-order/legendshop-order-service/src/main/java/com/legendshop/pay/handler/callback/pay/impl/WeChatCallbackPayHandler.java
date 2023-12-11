/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.callback.pay.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.legendshop.basic.dto.WxPayConfigDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.util.HttpUtil;
import com.legendshop.pay.dto.PayConfigDTO;
import com.legendshop.pay.dto.PayNotifyDTO;
import com.legendshop.pay.handler.callback.pay.AbstractCallbackPayHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
@Component(value = "WX_PAY")
public class WeChatCallbackPayHandler extends AbstractCallbackPayHandler {

	private WxPayConfigDTO wxPayConfig;

	public WxPayConfigDTO getWxPayConfig() {
		if (null != wxPayConfig) {
			return wxPayConfig;
		}
		PayConfigDTO config = config(SysParamNameEnum.WX_PAY.name());
		this.wxPayConfig = BeanUtil.mapToBean(config.getMap(), WxPayConfigDTO.class, true);
		return wxPayConfig;
	}

	@Override
	public Map<String, String> getNotifyParamMap(HttpServletRequest request, HttpServletResponse response) {
		log.info(" [ CallbackPayHandler ] [ WeChat ] --------------- getNotifyParamMap ()---------------->");
		// 读取request参数（XML）
		String resultXml = HttpUtil.readData(request);
		log.info(" [ CallbackPayHandler ] [ WeChat ] --------------- getNotify---------------> " + resultXml + "  end ");
		return WxPayKit.xmlToMap(resultXml);
	}

	@Override
	public boolean verifyNotifyParamMap(Map<String, String> notifyParamMap) {
		// 验签
		log.info(" [ CallbackPayHandler ] [ WeChat ] --------------- verifyNotifyParamMap ()---------------->");
		return WxPayKit.verifyNotify(notifyParamMap, getWxPayConfig().getPartnerKey(), SignType.MD5);
	}

	@Override
	public PayNotifyDTO notifyParamsToBean(Map<String, String> notifyMap) {
		PayNotifyDTO payNotify = new PayNotifyDTO();
		payNotify.setSettlementSn(notifyMap.get("out_trade_no"));
		payNotify.setTransactionSn(notifyMap.get("transaction_id"));

		// 微信单位为分
		String feeStr = notifyMap.get("total_fee");
		BigDecimal totalAmount = new BigDecimal(feeStr).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);
		payNotify.setCashAmount(totalAmount);
		payNotify.setPayType(SysParamNameEnum.WX_PAY.name());
		String returnCode = notifyMap.get("return_code");
		if (WxPayKit.codeIsOk(returnCode)) {
			payNotify.setPaySuccess(true);
		} else {
			payNotify.setPaySuccess(false);
			log.error("微信支付错误消息： {}", notifyMap.get("err_code_des"));
		}
		return payNotify;
	}


	@Override
	public String success() {
		return "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>";
	}

	@Override
	public String fail() {
		return "<xml><return_code>FAIL</return_code><return_msg>支付回调失败</return_msg></xml>";
	}
}

