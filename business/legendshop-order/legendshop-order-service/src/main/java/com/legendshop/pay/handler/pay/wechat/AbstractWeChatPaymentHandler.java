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
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.legendshop.basic.api.WxApi;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.common.core.config.sys.params.WxPayConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.pay.dto.PaySettlementDTO;
import com.legendshop.pay.dto.PaymentDTO;
import com.legendshop.pay.handler.pay.PaymentHandler;
import com.legendshop.pay.service.PaySettlementService;
import com.legendshop.user.api.PassportApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * @author legendshop
 */
@Slf4j
public abstract class AbstractWeChatPaymentHandler implements PaymentHandler {

	protected WxPayConfig config;

	@Autowired
	protected WxApi wxApi;

	@Autowired
	protected PassportApi passportApi;

	@Autowired
	protected CommonProperties commonProperties;

	@Autowired
	protected PaySettlementService paySettlementService;

	@Override
	public R<String> payment(PaymentDTO payment) {
		log.info(" [ Payment WeChat ]进入微信支付，payment: {}", JSONUtil.toJsonStr(payment));
		R<WxPayConfig> configR = wxApi.getPayConfig();
		config = configR.getData();

		if (null == config) {
			log.error("支付参数错误，Type：{}", SysParamNameEnum.WX_PAY.name());
			return R.fail("支付参数错误！");
		}

		if (null == config.getEnabled() || !config.getEnabled()) {
			log.error(" [ Refund WeChat ] 微信支付尚未启用！");
			return R.fail("微信支付尚未启用！");
		}

		log.info("微信支付参数，config：{}", JSONUtil.toJsonStr(config));

		// 获取支付单信息
		PaySettlementDTO settlement = this.paySettlementService.getBySn(payment.getNumber());
		// 获取用户信息
		Long userId = settlement.getUserId();
		String mchId = config.getMchId();
		String appId = getAppId();
		String openId = getOpenId(userId);
		String partnerKey = config.getPartnerKey();
		String currentNotifyUrl = getNotifyUrl();

		if (StringUtils.isBlank(mchId) || StringUtils.isBlank(appId) || StringUtils.isBlank(partnerKey)) {
			log.error("支付的密钥信息缺失：mchId：{}，appId：{}，partnerKey：{}", mchId, appId, partnerKey);
			return R.fail("请设置支付的密钥信息!");
		}

		// 设置微信支付标题，注意标题一定去空格
		String subject = payment.getPayDescription().replaceAll(" ", "").replaceAll("/", "");
		if (subject.length() > 20) {
			subject = subject.substring(0, 20);
			subject += "...";
		}

		// 金额
		String totalPrice = String.valueOf(payment.getAmount().setScale(2, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100)).intValue());

		// 随机数
		String nonceStr = RandomStringUtils.randomNumeric(8);

		String outTradeNo = payment.getNumber();
		// 交易类型
		String tradeType = getTradeType();

		String sceneInfo = null;
		if (VisitSourceEnum.H5.equals(payment.getVisitSource())) {
			sceneInfo = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"" + commonProperties.getServiceDomainName() + "\",\"wap_name\": \"订单支付\"}}";
		}
		// 获取沙箱apiKey
		if (null != config.getEnabledSandbox() && config.getEnabledSandbox()) {
			partnerKey = WxPayApi.getSignKey(mchId, partnerKey, SignType.MD5);
			Map<String, String> partnerKeyMap = WxPayKit.xmlToMap(partnerKey);
			partnerKey = partnerKeyMap.get("sandbox_signkey");
			totalPrice = "101";
		}


		Map<String, String> params = UnifiedOrderModel.builder()
				.appid(appId)
				.mch_id(mchId)
				.openid(openId)
				.nonce_str(nonceStr)
				.sign_type(SignType.MD5.getType())
				.body(subject)
				.out_trade_no(outTradeNo)
				.total_fee(totalPrice)
				.spbill_create_ip(payment.getIp())
				.notify_url(currentNotifyUrl)
				.trade_type(tradeType)
				.scene_info(sceneInfo)
				.build().toMap();
		// 签名
		params = WxPayKit.buildSign(params, partnerKey, SignType.MD5);

		log.info("微信支付参数，params：{}", JSONUtil.toJsonStr(params));

		// 发送请求
		String xmlResult = WxPayApi.pushOrder(config.getEnabledSandbox() != null && config.getEnabledSandbox(), params);

		// 将请求返回的 xml 数据转为 Map，方便后面逻辑获取数据
		Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);


		log.info("微信支付参数，resultMap：{}", JSONUtil.toJsonStr(resultMap));
		// 判断返回的结果
		String returnCode = resultMap.get("return_code"),
				returnMsg = resultMap.get("return_msg"),
				resultCode = resultMap.get("result_code");
		if (!WxPayKit.codeIsOk(returnCode) || !WxPayKit.codeIsOk(resultCode)) {
			log.error(" [ Payment WeChat ] 微信支付失败 returnCode：{}, returnMsg: {}, resultCode: {}", returnCode, returnMsg, resultCode);
			return R.fail("发起支付失败!");
		}

		return R.ok(doBeforeResponse(resultMap));
	}

	@Override
	public String getNotifyUrl() {
		return commonProperties.getServiceDomainName() + notifyUrl + SysParamNameEnum.WX_PAY.name();
	}

	/**
	 * AppId
	 */
	public abstract String getAppId();

	/**
	 * 获取用户在不同端的openId
	 */
	public abstract String getOpenId(Long userId);

	/**
	 * 交易类型
	 */
	protected abstract String getTradeType();

	/**
	 * 响应数据
	 */
	protected abstract String doBeforeResponse(Map<String, String> resultMap);

}
