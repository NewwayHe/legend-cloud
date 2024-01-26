/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.utils;

import cn.hutool.core.util.RandomUtil;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SmsTemplateTypeEnum;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.properties.EnvironmentProperties;
import com.legendshop.common.core.util.SpringContextHolder;
import com.legendshop.user.dto.VerifyCodeDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 验证码校验工具
 *
 * @author legendshop
 */
public class VerifyCodeUtil {

	private static final StringRedisTemplate redisTemplate;
	private static EnvironmentProperties environmentProperties;

	static {
		redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		environmentProperties = SpringContextHolder.getBean(EnvironmentProperties.class);
	}


	/**
	 * 临时凭证缓存前缀
	 */
	public final static String TEMP_CERTIFICATE_KEY = UserTypeEnum.USER + StringConstant.COLON + MsgSendTypeEnum.VAL + StringConstant.COLON + SmsTemplateTypeEnum.TEMP_CERTIFICATE + StringConstant.COLON;

	/**
	 * 手机验证码校验（二次随机数校验也可以用当前方法）
	 * 验证用户输入的 随机验证码  是否正确，验证后清除
	 **/
	public static R<Void> validateCode(VerifyCodeDTO verifyCodeDTO, Boolean delete) {
		String key = verifyCodeDTO.getUserType() + StringConstant.COLON + MsgSendTypeEnum.VAL + verifyCodeDTO.getSmsTemplateType() + StringConstant.COLON + verifyCodeDTO.getMobile();
		String code = redisTemplate.opsForValue().get(key);
		if (StringUtils.isBlank(code)) {
			return R.fail("验证码已过期，请重新获取！");
		}

		// 非开发/演示环境
		if (!environmentProperties.isDebug()) {
			if (!code.equals(verifyCodeDTO.getCode())) {
				return R.fail("验证码不正确！");
			}
		} else {
			if (!code.equals(CommonConstants.DEFAULT_VERIFICATION_CODE)) {
				return R.fail("验证码不正确！");
			}
		}

		if (delete == null || delete) {
			redisTemplate.delete(key);
		}
		return R.ok();
	}

	/**
	 * 主动清除key
	 *
	 * @param verifyCodeDTO
	 */
	public static void deleteKey(VerifyCodeDTO verifyCodeDTO) {
		String key = verifyCodeDTO.getUserType() + StringConstant.COLON + MsgSendTypeEnum.VAL + verifyCodeDTO.getSmsTemplateType() + StringConstant.COLON + verifyCodeDTO.getMobile();
		redisTemplate.delete(key);
	}

	/**
	 * 手机验证码校验（二次随机数校验也可以用当前方法）
	 * 验证用户输入的 随机验证码  是否正确，验证后清除
	 **/
	public static boolean validateCode(VerifyCodeDTO verifyCodeDTO) {
		String key = verifyCodeDTO.getUserType() + StringConstant.COLON + MsgSendTypeEnum.VAL + verifyCodeDTO.getSmsTemplateType() + StringConstant.COLON + verifyCodeDTO.getMobile();
		String code = redisTemplate.opsForValue().get(key);

		// 非开发/演示环境
		if (!environmentProperties.isDebug()) {
			if (StringUtils.isBlank(code) || !code.equals(verifyCodeDTO.getCode())) {
				return false;
			}
		}else {
			if (StringUtils.isBlank(code) || !verifyCodeDTO.getCode().equals(CommonConstants.DEFAULT_VERIFICATION_CODE)) {
				return false;
			}
		}
		redisTemplate.delete(key);
		return true;
	}

	/**
	 * 1、验证用户输入的 随机验证码  是否正确，验证后清除；
	 * 2、并返回一个可以用于二次验证的缓存code；
	 * 3、code为随机数，value为校验的手机号
	 *
	 * @return the 返回R对象相应是否成功，成功后携带一个code用于二次校验
	 **/
	public static R<String> verifyCodeReturnRandomCode(VerifyCodeDTO verifyCodeDTO) {
		if (!validateCode(verifyCodeDTO)) {
			return R.fail("验证码错误！");
		}
		return R.ok(createValidateCode(verifyCodeDTO));
	}

	/**
	 * 创建二次验证随机数
	 **/
	public static String createValidateCode(VerifyCodeDTO verifyCodeDTO) {
		String key = verifyCodeDTO.getUserType() + StringConstant.COLON + MsgSendTypeEnum.VAL + verifyCodeDTO.getSmsTemplateType() + StringConstant.COLON + verifyCodeDTO.getMobile();
		redisTemplate.delete(key);
		String certificate = RandomUtil.randomString(32);
		redisTemplate.opsForValue().set(key, certificate, 30, TimeUnit.MINUTES);
		return certificate;
	}


}
