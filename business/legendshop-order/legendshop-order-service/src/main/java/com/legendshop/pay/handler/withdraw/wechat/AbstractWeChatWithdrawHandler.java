/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.withdraw.wechat;

import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.TransferModel;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.WxPayConfigDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.oss.http.OssService;
import com.legendshop.common.oss.properties.OssProperties;
import com.legendshop.pay.dto.UserWithdrawDTO;
import com.legendshop.pay.handler.withdraw.AbstractWithdrawHandler;
import com.legendshop.pay.service.UserWalletDetailsService;
import com.legendshop.pay.service.UserWalletService;
import com.legendshop.user.api.PassportApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * 微信提现抽象
 *
 * @author legendshop
 */
@Slf4j
public abstract class AbstractWeChatWithdrawHandler extends AbstractWithdrawHandler {

	@Autowired
	protected PassportApi passportApi;

	@Autowired
	protected SysParamsApi sysParamsApi;

	@Autowired
	protected UserWalletService userWalletService;

	@Autowired
	protected UserWalletDetailsService walletDetailsService;

	@Autowired
	private OssService ossService;

	@Autowired
	private OssProperties ossProperties;

	@Override
	protected R<Void> userWithdrawHandler(UserWithdrawDTO dto) {
		log.info(" [ UserWithdrawHandler WeChat ]进入微信企业支付 params: {}", JSONUtil.toJsonStr(dto));
		WxPayConfigDTO config = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.WX_PAY.name(), WxPayConfigDTO.class).getData(), WxPayConfigDTO.class);
		if (null == config) {
			log.error("支付参数错误，Type：{}", SysParamNameEnum.WX_PAY.name());
			return R.fail("支付参数错误！");
		}
		//密钥
		String partnerKey = config.getPartnerKey();

		// 用户提现来源
		VisitSourceEnum sourceEnum = VisitSourceEnum.getByName(dto.getSource());

		// 1.拼凑企业支付需要的参数
		// 微信公众号的appId
		String appId = getAppId(sourceEnum);
		// 商户号
		String mchId = config.getMchId();
		// 生成随机数
		String nonceStr = RandomStringUtils.randomNumeric(8);
		// 获取商户订单号
		String partnerTradeNo = dto.getWithdrawSerialNo().toString();
		// 支付给用户openId
		String openId = dto.getAccount();
		// 是否验证真实姓名呢
		String checkName = "NO_CHECK";

		// 企业付款金额，最少为100，单位为分
		String amount = String.valueOf(dto.getAmount().setScale(2, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100)).intValue());
		// 企业付款操作说明信息。必填。
		String desc = "用户提现到微信零钱";


		if (StringUtils.isBlank(mchId) || StringUtils.isBlank(appId) || StringUtils.isBlank(partnerKey)) {
			log.error("支付的密钥信息缺失：mchId：{}，appId：{}，partnerKey：{}", mchId, appId, partnerKey);
			return R.fail("平台提现功能尚未完成，请联系平台客服!");
		}

		if (StringUtils.isBlank(openId)) {
			log.error("获取用户微信openId失败");
			return R.fail("用户尚未进行微信授权，无法进行微信提现！");
		}

		// 2.生成map集合
		Map<String, String> params = TransferModel.builder()
				.mch_appid(appId)
				.mchid(mchId)
				.nonce_str(nonceStr)
				.partner_trade_no(partnerTradeNo)
				.openid(openId)
				.check_name(checkName)
				.amount(amount)
				.desc(desc)
				.build().toMap();

		// 3.利用上面的参数，构建签名并存入map
		params = WxPayKit.buildSign(params, partnerKey, SignType.MD5, false);

		InputStream inputStream = ossService.getObject(ossProperties.getPrivateBucketName(), config.getRefundFile());

		// 4.发送请求
		String returnXml = WxPayApi.transfers(params, inputStream, mchId);
		System.out.println("返回的returnXml为:" + returnXml);
		// 将请求返回的 xml 数据转为 Map，方便后面逻辑获取数据
		Map<String, String> resultMap = WxPayKit.xmlToMap(returnXml);

		// 5.判断返回的结果
		String returnCode = resultMap.get("return_code"),
				returnMsg = resultMap.get("err_code_des"),
				resultCode = resultMap.get("result_code");
		if (!(WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode))) {
			log.info(" [ CorporatePayment WeChat ] 进入微信企业支付 returnCode：{}, returnMsg: {}, resultCode: {}", returnCode, returnMsg, resultCode);
			return R.fail(returnMsg);
		}
		return R.ok();
	}

	private String getAppId(VisitSourceEnum sourceEnum) {
		String name = null;
		switch (sourceEnum) {
			case APP:
				name = SysParamNameEnum.WX_APP.name();
				break;
			case MINI:
				name = SysParamNameEnum.WX_MINI_PRO.name();
				break;
			case PC:
				name = SysParamNameEnum.WX_WEB.name();
				break;
			default:
				name = SysParamNameEnum.WX_MP.name();
		}
		WxConfig config = mapper.convertValue(sysParamsApi.getNotCacheConfigByName(name, WxConfig.class).getData(), WxConfig.class);
		return config.getAppId();
	}

	private String getOpenId(Long userId, VisitSourceEnum sourceEnum) {
		R<String> openIdR = passportApi.getOpenIdByUserId(userId, sourceEnum.name());
		return openIdR.getData();
	}

}
