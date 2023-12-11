/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.handler.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.basic.enums.FrontEndPageEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SmsTemplateTypeEnum;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.SecurityConstants;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.dto.WxAuthorizationUserDTO;
import com.legendshop.user.dto.WxUserInfo;
import com.legendshop.user.handler.AbstractSocialLoginHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import static com.legendshop.user.utils.VerifyCodeUtil.TEMP_CERTIFICATE_KEY;

/**
 * pc端微信登录处理类
 *
 * @author legendshop
 */
@Slf4j
@Component("WECHAT_PC")
public class WeChatPcLoginHandler extends AbstractSocialLoginHandler {


	final static String type = "WECHAT";

	@Getter
	@Setter
	private String source = VisitSourceEnum.PC.name();

	/**
	 * 认证缓存Key
	 */
	String keyPrefix = UserTypeEnum.USER + StringConstant.COLON + type + StringConstant.HIPHEN + source + StringConstant.COLON;

	@Override
	public String authUrl() {
		String appId = this.getConfig().getAppId();
		String userPcDomainName = commonProperties.getUserPcDomainName();

		String redirectUri = "/" + FrontEndPageEnum.PC_AUTH_CALLBACK.getUrl();
		try {
			redirectUri = URLEncoder.encode(redirectUri, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		redirectUri = userPcDomainName + redirectUri;
		String state = RandomUtil.randomString(32);
		String key = keyPrefix + state;
		redisTemplate.opsForValue().set(key, state, 60, TimeUnit.MINUTES);
		return String.format(SecurityConstants.WX_PC_AUTHORIZATION_CODE_URL, appId, redirectUri, state);
	}

	@Override
	protected R<Void> verify(BasisLoginParamsDTO loginParams) {

		// 带有平台自签临时凭证
		if (StringUtils.isNotBlank(loginParams.getThirdPartyIdentifier())) {
			// 短信验证码
			String key = loginParams.getUserType() + StringConstant.COLON + MsgSendTypeEnum.VAL + SmsTemplateTypeEnum.LOGIN + StringConstant.COLON + loginParams.getPrincipal();
			String cacheCode = this.redisTemplate.opsForValue().get(key);
			if (StringUtils.isBlank(cacheCode) || !cacheCode.equals(loginParams.getCredentials())) {
				return R.fail(LegendshopSecurityErrorEnum.VERIFY_CODE_ERROR.name());
			}
			this.redisTemplate.delete(key);
			// 第三方临时签证
			String tempCertificateAuth = this.redisTemplate.opsForValue().get(TEMP_CERTIFICATE_KEY + loginParams.getThirdPartyIdentifier());
			if (StringUtils.isBlank(tempCertificateAuth)) {
				return R.fail(LegendshopSecurityErrorEnum.THIRD_PARTY_BIND_ERROR.name());
			}
			loginParams.setThirdPartyIdentifier(tempCertificateAuth);
			return R.ok();
		} else {
			String state = this.redisTemplate.opsForValue().get(keyPrefix + loginParams.getExtended());
			if (StringUtils.isBlank(state)) {
				return R.fail(LegendshopSecurityErrorEnum.THIRD_PARTY_BIND_ERROR.name());
			}
		}
		// 带有微信CODE
		return R.process(StringUtils.isNotBlank(loginParams.getCredentials()), LegendshopSecurityErrorEnum.THIRD_PARTY_AUTH_ERROR.name());

	}

	/**
	 * 微信登录传入的code
	 *
	 * @param loginParams the 登录信息
	 * @return openId
	 */
	@Override
	public R<UserInfo> identify(BasisLoginParamsDTO loginParams) {
		String ip = loginParams.getIp();
		if (StringUtils.isNotBlank(loginParams.getThirdPartyIdentifier())) {
			log.info("已存在第三方用户标识, 用户第三方标识信息:{}", loginParams.getThirdPartyIdentifier());
			WxAuthorizationUserDTO authorizationUserDTO = new WxAuthorizationUserDTO(loginParams.getPrincipal(), loginParams.getThirdPartyIdentifier(), ip, VisitSourceEnum.PC);
			authorizationUserDTO.setVisitorId(loginParams.getVisitorId());
			return super.wxUserBind(authorizationUserDTO, null);
		}
		log.info("不存在第三方用户标识, 验证参数:{}", loginParams);
		loginParams.setPrincipal("");
		String appId = this.getConfig().getAppId();
		String secret = this.getConfig().getSecret();
		String url = String.format(SecurityConstants.WX_AUTHORIZATION_CODE_URL, appId, secret, loginParams.getCredentials());
		WxUserInfo wxUserInfo = weChatInfo(url);
		wxUserInfo = weChatInfoExtend(wxUserInfo);
		String openId = wxUserInfo.getOpenid();
		String phoneNumber = loginParams.getPrincipal();
		WxAuthorizationUserDTO authorizationUserDTO = new WxAuthorizationUserDTO(phoneNumber, openId, ip, VisitSourceEnum.PC);
		authorizationUserDTO.setVisitorId(loginParams.getVisitorId());
		return super.wxUserBind(authorizationUserDTO, wxUserInfo);
	}

	/**
	 * 更新获取微信openId授权
	 *
	 * @param loginParams
	 * @return
	 */
	@Override
	public R<Boolean> updateUserOpenId(BasisLoginParamsDTO loginParams) {
		if (StrUtil.isBlank(loginParams.getThirdPartyIdentifier())) {
			return R.ok(false);
		}
		String appId = this.getConfig().getAppId();
		String appSecret = this.getConfig().getSecret();

		// 获取微信openId授权
		String url = String.format(SecurityConstants.WX_AUTHORIZATION_CODE_URL, appId, appSecret, loginParams.getThirdPartyIdentifier());
		//微信绑定处理
		WxUserInfo wxUserInfo = weChatInfo(url);
		//获取openId
		String openId = wxUserInfo.getOpenid();

		//登录构建UserInfo
		R<UserInfo> userInfo = ordinaryUserService.getUserInfo(loginParams.getPrincipal());
		if (!userInfo.getSuccess()) {
			return R.fail(userInfo.getMsg());
		}
		WxAuthorizationUserDTO authorizationUserDTO = new WxAuthorizationUserDTO(userInfo.getData().getSysUser().getMobile(), openId, loginParams.getIp(), VisitSourceEnum.PC);
		authorizationUserDTO.setVisitorId(loginParams.getVisitorId());
		super.wxUserBind(authorizationUserDTO, wxUserInfo);
		return R.ok(true);
	}

	private WxConfig getConfig() {
		return wxApi.getWebConfig().getData();
	}

	@Override
	public String getSource() {
		return VisitSourceEnum.PC.name();
	}
}
