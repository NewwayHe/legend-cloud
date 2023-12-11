/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.handler.impl;

import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.enums.VisitSourceEnum;
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

/**
 * 微信登录处理类
 *
 * @author legendshop
 */
@Slf4j
@Component("WECHAT_APP")
public class WeChatAppLoginHandler extends AbstractSocialLoginHandler {


	final static String type = "WECHAT";

	@Getter
	@Setter
	private String source = VisitSourceEnum.APP.name();

	/**
	 * 认证缓存Key
	 */
	String keyPrefix = UserTypeEnum.USER + StringConstant.COLON + type + StringConstant.HIPHEN + source + StringConstant.COLON;

	@Override
	public String authUrl() {
		return null;
	}

	@Override
	protected R<Void> verify(BasisLoginParamsDTO loginParams) {
		return R.ok();
	}

	/**
	 * 微信登录传入的code
	 *
	 * @param loginParams the 登录信息
	 * @return openId
	 */
	/**
	 * 微信登录传入的code
	 *
	 * @param loginParams the 验证参数
	 * @return openId
	 */
	@Override
	public R<UserInfo> identify(BasisLoginParamsDTO loginParams) {
		String ip = loginParams.getIp();
		if (StringUtils.isNotBlank(loginParams.getThirdPartyIdentifier())) {
			WxAuthorizationUserDTO authorizationUserDTO = new WxAuthorizationUserDTO(loginParams.getPrincipal(), loginParams.getThirdPartyIdentifier(), ip, VisitSourceEnum.APP);
			authorizationUserDTO.setVisitorId(loginParams.getVisitorId());
			WxUserInfo wxUserInfo = new WxUserInfo();
			wxUserInfo.setOpenid(loginParams.getPrincipal());
			return super.wxUserBind(authorizationUserDTO, wxUserInfo);
		}
		WxUserInfo wxUserInfo = new WxUserInfo();
		wxUserInfo.setOpenid(loginParams.getPrincipal());
		wxUserInfo.setAccess_token(loginParams.getCredentials());
		wxUserInfo = weChatInfoExtend(wxUserInfo);
		String openId = wxUserInfo.getOpenid();
		String phoneNumber = "";
		WxAuthorizationUserDTO authorizationUserDTO = new WxAuthorizationUserDTO(phoneNumber, openId, ip, VisitSourceEnum.APP);
		authorizationUserDTO.setVisitorId(loginParams.getVisitorId());
		return super.wxUserBind(authorizationUserDTO, wxUserInfo);
	}

	@Override
	public R<Boolean> updateUserOpenId(BasisLoginParamsDTO loginParams) {
		R<UserInfo> userInfo = ordinaryUserService.getUserInfo(loginParams.getPrincipal());
		if (!userInfo.getSuccess()) {
			return R.fail(userInfo.getMsg());
		}

		WxAuthorizationUserDTO authorizationUserDTO = new WxAuthorizationUserDTO(userInfo.getData().getSysUser().getMobile(), loginParams.getThirdPartyIdentifier(), loginParams.getIp(), VisitSourceEnum.APP);
		authorizationUserDTO.setVisitorId(loginParams.getVisitorId());

		WxUserInfo wxUserInfo = new WxUserInfo();
		wxUserInfo.setOpenid(loginParams.getThirdPartyIdentifier());
		super.wxUserBind(authorizationUserDTO, wxUserInfo);
		return R.ok(true);
	}

	private WxConfig getConfig() {
		return wxApi.getWebConfig().getData();
	}
}
