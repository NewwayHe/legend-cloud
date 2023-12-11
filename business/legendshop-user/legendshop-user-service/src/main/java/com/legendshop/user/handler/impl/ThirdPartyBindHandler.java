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
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.handler.AbstractSocialLoginHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.legendshop.user.utils.VerifyCodeUtil.TEMP_CERTIFICATE_KEY;

/**
 * 第三方通行证绑定
 *
 * @author legendshop
 */
@Slf4j
@Component("THIRD_PARTY_BIND")
public class ThirdPartyBindHandler extends AbstractSocialLoginHandler {

	@Override
	protected R<Void> verify(BasisLoginParamsDTO loginParams) {
		String principal = loginParams.getPrincipal();
		String credentials = loginParams.getCredentials();
		// 验证码是否正确
		// 临时凭证是否存在
		String codeKey = loginParams.getUserType() + StringConstant.COLON + MsgSendTypeEnum.VAL + SmsTemplateTypeEnum.BIND_MOBILE_PHONE + StringConstant.COLON + principal;
		String temCredentialsKey = TEMP_CERTIFICATE_KEY + loginParams.getThirdPartyIdentifier();
		String cacheCode = redisTemplate.opsForValue().get(codeKey);
		String thirdPartyIdentifier = redisTemplate.opsForValue().get(temCredentialsKey);
		if (null == cacheCode || !cacheCode.equals(credentials)) {
			return R.fail(LegendshopSecurityErrorEnum.VERIFY_CODE_ERROR.name());
		}
		if (null == thirdPartyIdentifier) {
			return R.fail(LegendshopSecurityErrorEnum.THIRD_PARTY_BIND_ERROR.name());
		}
		loginParams.setThirdPartyIdentifier(thirdPartyIdentifier);
		redisTemplate.delete(codeKey);
		redisTemplate.delete(temCredentialsKey);
		return R.ok();
	}

	@Override
	protected R<UserInfo> identify(BasisLoginParamsDTO loginParams) {
		String principal = loginParams.getPrincipal();
		R<OrdinaryUserDTO> ordinaryUserDTOResult = this.ordinaryUserService.bindThirdParty(principal, loginParams.getThirdPartyIdentifier());
		if (null == ordinaryUserDTOResult.getSuccess() || !ordinaryUserDTOResult.getSuccess()) {
			return R.fail(LegendshopSecurityErrorEnum.BAD_CREDENTIALS.name());
		}
		OrdinaryUserDTO ordinaryUserDTO = ordinaryUserDTOResult.getData();
		return this.ordinaryUserService.getUserInfo(ordinaryUserDTO.getUsername());
	}
}
