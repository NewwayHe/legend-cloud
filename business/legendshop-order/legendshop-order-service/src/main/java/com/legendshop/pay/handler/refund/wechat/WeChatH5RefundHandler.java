/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.refund.wechat;

import com.ijpay.core.enums.SignType;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.config.sys.params.WxMpConfig;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.order.enums.PayTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Component("H5_WX_PAY_REFUND")
public class WeChatH5RefundHandler extends AbstractWeChatRefundHandler {

	@Override
	public String getAppId() {
		WxConfig config = mapper.convertValue(super.sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.WX_MP.name(), WxConfig.class).getData(), WxMpConfig.class);
		return config.getAppId();
	}

	@Override
	protected SignType getSignType() {
		return SignType.MD5;
	}

	@Override
	public boolean isSupport(String payType, String paySource) {
		return PayTypeEnum.WX_PAY.name().equals(payType) && VisitSourceEnum.H5.name().equals(paySource);
	}
}

