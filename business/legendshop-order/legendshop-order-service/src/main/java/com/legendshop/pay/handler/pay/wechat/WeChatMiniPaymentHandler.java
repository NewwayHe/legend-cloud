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
@Component("MINI_WX_PAY")
public class WeChatMiniPaymentHandler extends AbstractWeChatPaymentHandler {

	@Override
	public String getAppId() {
		WxConfig config = super.wxApi.getMiniConfig().getData();
		return config.getAppId();
	}

	@Override
	public String getOpenId(Long userId) {
		R<String> openIdR = this.passportApi.getOpenIdByUserId(userId, VisitSourceEnum.MINI.name());
		String openId = openIdR.getData();
		if (StrUtil.isBlank(openId)) {
			throw new NotWeChatAuthorizeBusinessException("需要微信授权，才能进行支付！");
		}
		return openId;
	}

	@Override
	protected String getTradeType() {
		return TradeType.JSAPI.getTradeType();
	}

	@Override
	protected String doBeforeResponse(Map<String, String> resultMap) {
		String appId = getAppId();
		String prepayId = resultMap.get("prepay_id");
		String partnerKey = config.getPartnerKey();
		// 二次签名，构建公众号唤起支付的参数,这里的签名方式要与上面统一下单请求签名方式保持一致
		Map<String, String> packageParams = WxPayKit.miniAppPrepayIdCreateSign(appId, prepayId, partnerKey, SignType.MD5);
		return JSONUtil.toJsonStr(packageParams);
	}
}
