/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.handler.impl;

import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.SecurityConstants;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.user.dto.*;
import com.legendshop.user.handler.AbstractSocialLoginHandler;
import com.legendshop.user.utils.WxEncryptUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 微信小程序登录
 *
 * @author legendshop
 */
@Slf4j
@Component("WECHAT_MINI")
public class WechatMiniLoginHandler extends AbstractSocialLoginHandler {

	@Getter
	@Setter
	private String source = VisitSourceEnum.MINI.name();

	@Override
	public String authUrl() {
		return super.authUrl();
	}

	@Override
	protected R<Void> verify(BasisLoginParamsDTO loginParams) {
		String credentials = loginParams.getCredentials();
		return R.process(StringUtils.isNotBlank(credentials), LegendshopSecurityErrorEnum.VERIFY_CODE_ERROR.name());
	}

	/**
	 * 小程序调用参数说明
	 * 验证类型                                    'auth_type':'WECHAT_MINI'
	 * 登录用户名（通过OPENID通录时统一固定编码CODE）	principal: CODE / 手机号， ,iv
	 * 登录密码，令牌	                            credentials:self.weixinCode,
	 * 扩展令牌（用户信息）,带上加密方式          	extended: 用户信息（watermark）,iv
	 * 第三方ID(没上用):	                        thirdPartyIdentifier：
	 *
	 * @param loginParams
	 * @return
	 */
	@Override
	public R<UserInfo> identify(BasisLoginParamsDTO loginParams) {
		String appId = this.getConfig().getAppId();
		String appSecret = this.getConfig().getSecret();
		String ip = loginParams.getIp();

		// 获取微信openId授权
		String url = String.format(SecurityConstants.WX_MINI_AUTHORIZATION_CODE_URL, appId, appSecret, loginParams.getCredentials());
		WxUserInfo wxUserInfo = weChatInfo(url);
		String openId = wxUserInfo.getOpenid();
		String sessionKey = wxUserInfo.getSession_key();

		//extended: 用户信息（watermark）,iv
		String[] extendeds = loginParams.getExtended().split(",");
		if (extendeds.length < 2) {
			log.error("扩展令牌信息不完整，{}", loginParams.getExtended());
			throw new BusinessException("登录失败，请重新授权登录！");
		}

		WxMiniUserInfo wxMiniUserInfo = WxEncryptUtil.decodeEncryptedData(extendeds[0], extendeds[1], sessionKey, WxMiniUserInfo.class);
		wxUserInfo.setSource(VisitSourceEnum.valueOf(source));
		wxUserInfo.setUnionid(wxMiniUserInfo.getUnionid());
		wxUserInfo.setNickname(wxMiniUserInfo.getNickname());
		wxUserInfo.setHeadimgurl(wxMiniUserInfo.getAvatarUrl());
		wxUserInfo.setSex(wxMiniUserInfo.getGender());
		wxUserInfo.setCountry(wxMiniUserInfo.getCountry());
		wxUserInfo.setProvince(wxMiniUserInfo.getProvince());
		wxUserInfo.setCity(wxMiniUserInfo.getCity());

		// principal 当用户是第一次绑定时，需要手机号作为绑定唯一标识， 格式 （手机号 ,iv）
		// 如果是已经绑定，则principal 为空，此时通过code 换openid判断用户是否已绑定，直接登录，如果没有对应的code，则返回未绑定异常。
		// 解密小程序手机号
		String phoneNumber = "";
		if (StringUtils.isNotBlank(loginParams.getPrincipal()) && !"CODE".equals(loginParams.getPrincipal())) {
			String[] principalValues = loginParams.getPrincipal().split(",");
			if (principalValues.length < 2) {
				log.error("扩展令牌信息不完整，{}", loginParams.getPrincipal());
				throw new BusinessException("登录失败，请重新授权登录！");
			}

			WxMiniUserPhoneDTO wxMiniUserPhone = WxEncryptUtil.decodeEncryptedData(principalValues[0], principalValues[1], sessionKey, WxMiniUserPhoneDTO.class);
			if (null == wxMiniUserPhone) {
				log.error("获取微信小程序手机号失败！");
				return R.fail(LegendshopSecurityErrorEnum.THIRD_PARTY_AUTH_ERROR.name());
			}
			phoneNumber = wxMiniUserPhone.getPurePhoneNumber();
		}

		WxAuthorizationUserDTO authorizationUserDTO = new WxAuthorizationUserDTO(phoneNumber, openId, ip, VisitSourceEnum.MINI);
		authorizationUserDTO.setVisitorId(loginParams.getVisitorId());
		return super.wxUserBind(authorizationUserDTO, wxUserInfo);
	}

	@Override
	public R<Boolean> updateUserOpenId(BasisLoginParamsDTO loginParams) {
		if (StrUtil.isBlank(loginParams.getThirdPartyIdentifier())) {
			return R.ok(false);
		}

		String appId = this.getConfig().getAppId();
		String appSecret = this.getConfig().getSecret();

		// 获取微信openId授权
		String url = String.format(SecurityConstants.WX_MINI_AUTHORIZATION_CODE_URL, appId, appSecret, loginParams.getThirdPartyIdentifier());
		WxUserInfo wxUserInfo = weChatInfo(url);
		String openId = wxUserInfo.getOpenid();

		R<UserInfo> userInfo = ordinaryUserService.getUserInfo(loginParams.getPrincipal());
		WxAuthorizationUserDTO authorizationUserDTO = new WxAuthorizationUserDTO(userInfo.getData().getSysUser().getMobile(), openId, loginParams.getIp(), VisitSourceEnum.APP);
		authorizationUserDTO.setVisitorId(loginParams.getVisitorId());
		super.wxUserBind(authorizationUserDTO, wxUserInfo);
		return R.ok(true);
	}


	private WxConfig getConfig() {
		return wxApi.getMiniConfig().getData();
	}
}
