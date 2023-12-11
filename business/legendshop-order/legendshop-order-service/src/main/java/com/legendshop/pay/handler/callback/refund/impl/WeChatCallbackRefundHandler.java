/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.callback.refund.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ijpay.core.kit.WxPayKit;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.util.HttpUtil;
import com.legendshop.pay.dto.PayConfigDTO;
import com.legendshop.pay.dto.RefundNotifyDTO;
import com.legendshop.pay.dto.WxPayConfigDTO;
import com.legendshop.pay.handler.callback.refund.AbstractCallbackRefundHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
@Component(value = "WX_REFUND")
public class WeChatCallbackRefundHandler extends AbstractCallbackRefundHandler {

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
		log.info(" [ CallbackRefundHandler ] [ WeChat ] --------------- getNotifyParamMap ()---------------->");
		// 读取request参数（XML）
		String resultXml = HttpUtil.readData(request);

		log.info(" [ CallbackRefundHandler ] [ WeChat ] --------------- getNotify result ---------------> ,{}", resultXml);
		Map<String, String> notifyParamMap = WxPayKit.xmlToMap(resultXml);
		String reqInfo = notifyParamMap.get("req_info");
		String infoXml = WxPayKit.decryptData(reqInfo, wxPayConfig.getPartnerKey());
		Map<String, String> infoMap = WxPayKit.xmlToMap(infoXml);
		log.info(" [ CallbackRefundHandler ] [ WeChat ] --------------- getNotify info ---------------> ,{}", infoXml);
		return infoMap;
	}

	@Override
	public boolean verifyNotifyParamMap(Map<String, String> notifyParamMap) {
		// 验签
		log.info(" [ CallbackRefundHandler ] [ WeChat ] --------------- verifyNotifyParamMap ()---------------->");
		return Boolean.TRUE;
	}

	@Override
	public RefundNotifyDTO notifyParamsToBean(Map<String, String> notifyMap) {
		RefundNotifyDTO notify = new RefundNotifyDTO();
		notify.setRefundType(SysParamNameEnum.WX_PAY.name());
		notify.setRefundSn(notifyMap.get("out_trade_no"));
		notify.setExternalRefundSn(notifyMap.get("refund_id"));
		notify.setRefundAmount(new BigDecimal(notifyMap.get("settlement_refund_fee")));
		notify.setRefundStatus("SUCCESS".equals(notifyMap.get("refund_status")));
		return notify;
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

