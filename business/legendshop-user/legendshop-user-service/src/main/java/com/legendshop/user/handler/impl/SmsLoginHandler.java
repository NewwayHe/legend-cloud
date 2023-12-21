/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.handler.impl;

import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SmsTemplateTypeEnum;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.dto.ShopUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.handler.AbstractSocialLoginHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 短信登录处理类
 *
 * @author legendshop
 */
@Slf4j
@Component("SMS")
public class SmsLoginHandler extends AbstractSocialLoginHandler {

	@Override
	protected R<Void> verify(BasisLoginParamsDTO loginParams) {
		String key = loginParams.getUserType() + StringConstant.COLON + MsgSendTypeEnum.VAL + SmsTemplateTypeEnum.LOGIN + StringConstant.COLON + loginParams.getPrincipal();
		String cacheCode = redisTemplate.opsForValue().get(key);

		// 非开发/演示环境
		if (!environmentProperties.isDebug()){
			if (null == cacheCode || !cacheCode.equals(loginParams.getCredentials())) {
				return R.fail(LegendshopSecurityErrorEnum.VERIFY_CODE_ERROR.name());
			}
		}else {
			if (null == cacheCode || !loginParams.getCredentials().equals(CommonConstants.DEFAULT_VERIFICATION_CODE)) {
				return R.fail(LegendshopSecurityErrorEnum.VERIFY_CODE_ERROR.name());
			}
		}
		redisTemplate.delete(key);
		return R.ok();
	}

	/**
	 * 短信验证码登录，确认身份为手机号不做任何处理
	 *
	 * @param loginParams the 认证信息
	 * @return mobile
	 */
	@Override
	public R<UserInfo> identify(BasisLoginParamsDTO loginParams) {
		return info(loginParams);
	}

	/**
	 * 通过mobile 获取用户信息
	 *
	 * @return UserInfo
	 */
	public R<UserInfo> info(BasisLoginParamsDTO loginParams) throws UsernameNotFoundException {
		String mobile = loginParams.getPrincipal();
		UserTypeEnum userType = loginParams.getUserType();
		String ip = loginParams.getIp();
		VisitSourceEnum source = StringUtils.isNotBlank(loginParams.getSource()) ? VisitSourceEnum.valueOf(loginParams.getSource()) : VisitSourceEnum.UNKNOWN;
		switch (userType) {
			case USER:
				OrdinaryUserDTO ordinaryUser = this.ordinaryUserService.getByMobile(mobile);
				// 不存在创建用户
				if (ordinaryUser == null) {
					ordinaryUser = new OrdinaryUserDTO(mobile);
					ordinaryUser.setRegIp(ip);
					ordinaryUser.setSource(source);
					ordinaryUser.setVisitorId(loginParams.getVisitorId());
					this.ordinaryUserService.save(ordinaryUser);
				}
				return this.ordinaryUserService.getUserInfo(ordinaryUser.getUsername());
			case SHOP:
				ShopUserDTO shopUser = this.shopUserService.getByMobile(mobile);
				// 不存在创建用户
				if (shopUser == null) {
					shopUser = new ShopUserDTO(mobile);
					this.shopUserService.save(shopUser);
				}
				return this.shopUserService.getUserInfo(shopUser.getUsername());
			default:
				return R.fail(LegendshopSecurityErrorEnum.ERROR_AUTH_TYPE.name());
		}
	}
}
