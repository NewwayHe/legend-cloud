/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.common.core.config.sys.params.AliPayConfig;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author legendshop
 */
@Component
@RequiredArgsConstructor
public class AliPayCommonSDKFactory {


	final SysParamsApi sysParamsApi;

	final CommonProperties commonProperties;

	private AlipayClient alipayClient;

	private CertAlipayRequest certAlipayRequest;

	/**
	 * 退款
	 *
	 * @return
	 */
	public AlipayTradeRefundResponse refund(String outTradeNo, String refundAmount, String outRequestNo) throws AlipayApiException {
		initAlipayClient();
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

		Map<String, String> map = new HashMap<>(16);
		map.put("out_trade_no", outTradeNo);
		map.put("refund_amount", refundAmount);
		if (StrUtil.isNotEmpty(outRequestNo)) {
			map.put("out_request_no", outRequestNo);
		}

		request.setBizContent(JSONUtil.toJsonStr(map));

		return this.alipayClient.execute(request);
	}


	public void initAlipayClient() {
		if (ObjectUtil.isNotNull(alipayClient)) {
			return;
		}
		if (ObjectUtil.isNull(certAlipayRequest)) {
			certAlipayRequest = getClientParams();
		}

		// 1. 创建AlipayClient实例
		this.alipayClient = new DefaultAlipayClient(certAlipayRequest.getServerUrl(), certAlipayRequest.getAppId(), certAlipayRequest.getPrivateKey(), certAlipayRequest.getFormat(), certAlipayRequest.getCharset(), certAlipayRequest.getAlipayPublicCertContent(), certAlipayRequest.getSignType());

	}


	private CertAlipayRequest getClientParams() {
		R<AliPayConfig> payConfigR = this.sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ALI_PAY.name(), AliPayConfig.class);
		ObjectMapper mapper = new ObjectMapper();
		AliPayConfig payConfig = mapper.convertValue(payConfigR.getData(), AliPayConfig.class);


		CertAlipayRequest certParams = new CertAlipayRequest();
		certParams.setServerUrl("https://openapi.alipay.com/gateway.do");
		//请更换为您的AppId
		certParams.setAppId(payConfig.getAppId());
		//请更换为您的PKCS8格式的应用私钥
		certParams.setPrivateKey(payConfig.getRsaPrivate());
		//请更换为您使用的字符集编码，推荐采用utf-8
		certParams.setCharset("utf-8");
		certParams.setFormat("json");
		certParams.setSignType("RSA2");
		//请更换您的支付宝公钥证书文件路径
		certParams.setAlipayPublicCertContent(payConfig.getRsaPublish());
		return certParams;
	}
}
