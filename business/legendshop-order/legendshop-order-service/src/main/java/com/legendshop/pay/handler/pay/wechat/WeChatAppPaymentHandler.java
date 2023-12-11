/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.pay.wechat;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.pay.expetion.NotWeChatAuthorizeBusinessException;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author legendshop
 */
@Component("APP_WX_PAY")
public class WeChatAppPaymentHandler extends AbstractWeChatPaymentHandler {

	@Override
	public String getAppId() {
		WxConfig config = super.wxApi.getAppConfig().getData();
		return config.getAppId();
	}

	@Override
	public String getOpenId(Long userId) {
		R<String> openIdR = this.passportApi.getOpenIdByUserId(userId, VisitSourceEnum.APP.name());
		String openId = openIdR.getData();
		if (StrUtil.isBlank(openId)) {
			throw new NotWeChatAuthorizeBusinessException("需要微信授权，才能进行支付！");
		}
		return openId;
	}

	@Override
	protected String getTradeType() {
		return TradeType.APP.getTradeType();
	}

	@Override
	protected String doBeforeResponse(Map<String, String> resultMap) {
		String appId = getAppId();
		String mchId = config.getMchId();
		String prepayId = resultMap.get("prepay_id");
		String partnerKey = config.getPartnerKey();
		// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
		Map<String, String> packageParams = WxPayKit.appPrepayIdCreateSign(appId, mchId, prepayId, partnerKey, SignType.MD5);
		return JSONUtil.toJsonStr(packageParams);
	}
}
