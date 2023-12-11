/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.pay.wechat;

import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.TradeType;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author legendshop
 */
@Component("H5_WX_PAY")
public class WeChatH5PaymentHandler extends AbstractWeChatPaymentHandler {

	@Override
	public String getAppId() {
		WxConfig config = super.wxApi.getMpConfig().getData();
		return config.getAppId();
	}

	@Override
	public String getOpenId(Long userId) {
		R<String> openIdR = this.passportApi.getOpenIdByUserId(userId, VisitSourceEnum.MP.name());
		return openIdR.getData();
	}

	@Override
	protected String getTradeType() {
		return TradeType.MWEB.getTradeType();
	}

	@Override
	protected String doBeforeResponse(Map<String, String> resultMap) {
		// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
		String mwebUrl = resultMap.get("mweb_url");
		Map<String, String> param = new HashMap<>(1);
		param.put("mwebUrl", mwebUrl);
		return JSONUtil.toJsonStr(param);
	}
}
