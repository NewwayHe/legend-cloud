/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.common.core.config.sys.params.AliPayConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.handler.pay.PaymentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Component
@RequiredArgsConstructor
public class AliPaySDKFactory {

	private Boolean bool = false;

	final SysParamsApi sysParamsApi;

	final CommonProperties commonProperties;

	public com.alipay.easysdk.payment.app.Client paymentHandlerApp() throws Exception {
		init();
		return Factory.Payment.App();
	}

	public com.alipay.easysdk.payment.wap.Client paymentHandlerWap() throws Exception {
		init();
		return Factory.Payment.Wap();
	}

	public com.alipay.easysdk.payment.common.Client paymentHandlerCommon() throws Exception {
		init();
		return Factory.Payment.Common();
	}

	public com.alipay.easysdk.payment.page.Client paymentHandlerPage() throws Exception {
		init();
		return Factory.Payment.Page();
	}


	public void init() {
		if (!bool) {
			Factory.setOptions(getOptions());
		}
		this.bool = true;
	}

	private Config getOptions() {
		R<AliPayConfig> payConfigR = this.sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ALI_PAY.name(), AliPayConfig.class);
		ObjectMapper mapper = new ObjectMapper();
		AliPayConfig payConfig = mapper.convertValue(payConfigR.getData(), AliPayConfig.class);

		Config config = new Config();
		config.protocol = "https";
		config.gatewayHost = "openapi.alipay.com";
		config.signType = "RSA2";
		config.appId = payConfig.getAppId();
		// 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
		config.merchantPrivateKey = payConfig.getRsaPrivate();
		//注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
		config.alipayPublicKey = payConfig.getRsaPublish();
		//可设置异步通知接收服务地址（可选）
		config.notifyUrl = commonProperties.getServiceDomainName() + PaymentHandler.notifyUrl + SysParamNameEnum.ALI_PAY.name();
		//可设置AES密钥，调用AES加解密相关接口时需要（可选）
		config.encryptKey = payConfig.getKey();
		return config;
	}

}
