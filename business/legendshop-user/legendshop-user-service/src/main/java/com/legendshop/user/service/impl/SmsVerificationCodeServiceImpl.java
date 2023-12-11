/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import com.legendshop.basic.api.SmsSendApi;
import com.legendshop.basic.dto.SmsSendParamDTO;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SmsTemplateTypeEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.BaseUserQuery;
import com.legendshop.user.dto.SmsVerificationCodeDTO;
import com.legendshop.user.service.BaseUserService;
import com.legendshop.user.service.OrdinaryUserService;
import com.legendshop.user.service.SmsVerificationCodeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author legendshop
 */
@Slf4j
@Service
@AllArgsConstructor
public class SmsVerificationCodeServiceImpl implements SmsVerificationCodeService {

	final SmsSendApi smsSendApi;

	final BaseUserService baseUserService;

	final StringRedisTemplate redisTemplate;

	final OrdinaryUserService ordinaryUserService;

	@Override
	public R<String> sendSmsCode(SmsVerificationCodeDTO smsVerificationCode) {
		// 校验发送参数
		R<String> verifyResult = smsSendingVerify(smsVerificationCode);
		// 获取校验结果
		if (!verifyResult.getSuccess()) {
			return R.fail(verifyResult.getMsg());
		}
		// 发送手机号
		String mobile = verifyResult.getData();
		// 组装发送参数
		SmsSendParamDTO sendParamDTO = new SmsSendParamDTO();
		sendParamDTO.setPhoneNumbers(Collections.singletonList(mobile));
		sendParamDTO.setTypeEnum(MsgSendTypeEnum.VAL);
		sendParamDTO.setSmsTemplateTypeEnum(smsVerificationCode.getCodeType());
		sendParamDTO.setUserType(smsVerificationCode.getUserType());
		sendParamDTO.setTemplateParam(null);
		sendParamDTO.setIp(smsVerificationCode.getIp());
		return this.smsSendApi.sendSms(sendParamDTO);
	}

	private R<String> smsSendingVerify(SmsVerificationCodeDTO smsVerificationCode) {
		// 初始化手机号和用户类型
		String mobile = smsVerificationCode.getMobile();
		String userType = smsVerificationCode.getUserType();

		if (StringUtils.isBlank(mobile)) {
			return R.fail("手机号不能为空");
		}

		if (mobile.length() != 11) {
			return R.fail("请输入正确的手机号");
		}

		boolean exist = this.baseUserService.isMobileExist(new BaseUserQuery(mobile, userType));
		if (SmsTemplateTypeEnum.REGISTER.equals(smsVerificationCode.getCodeType())
				|| SmsTemplateTypeEnum.CONFIRM_MOBILE_BIND.equals(smsVerificationCode.getCodeType())) {
			// 注册、绑定新手机
			if (exist) {
				return R.fail("手机号已被占用！");
			}
		} else if (!SmsTemplateTypeEnum.LOGIN.equals(smsVerificationCode.getCodeType())) {
			// 忘记密码、修改支付密码、换绑手机号，旧号码验证
			if (!exist) {
				log.info("手机号未注册:{}", mobile);
				return R.fail("用户不存在！");
			}
		}
		return R.ok(mobile);
	}

}
