/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * 短信模板类型
 *
 * @author legendshop
 */
public enum SmsTemplateTypeEnum implements StringEnum {

	/**
	 * 身份认证
	 */
	USER_AUTH("user_auth"),

	/**
	 * 临时凭证
	 */
	TEMP_CERTIFICATE("temp_certificate"),

	/**
	 * 手机验证码登录
	 */
	LOGIN("login"),

	/**
	 * 手机验证码注册
	 */
	REGISTER("register"),

	/**
	 * 绑定手机
	 */
	BIND_MOBILE_PHONE("bind_mobile_phone"),

	/**
	 * 忘记密码
	 */
	FORGET_PASSWORD("forget_password"),

	/**
	 * 修改密码
	 */
	MODIFY_LOGIN_PASSWORD("modify_login_password"),


	/**
	 * 修改支付密码
	 */
	MODIFY_PAY_PASSWORD("modify_pay_password"),

	/**
	 * 修改绑定手机
	 */
	MODIFY_BINDING_MOBILE("modify_binding_mobile"),

	/**
	 * 绑定新手机
	 */
	CONFIRM_MOBILE_BIND("confirm_mobile_bind"),

	/**
	 * 商家入驻提醒管理员手机号审核
	 */
	SHOP_ENTER_NOTIFY("shop_enter_notify");


	/**
	 * The value.
	 */
	private final String value;

	/**
	 * Instantiates a new function enum.
	 *
	 * @param value the value
	 */
	SmsTemplateTypeEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return this.value;
	}

	/**
	 * Instance.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public static boolean instance(String name) {
		SmsTemplateTypeEnum[] licenseEnums = values();
		for (SmsTemplateTypeEnum licenseEnum : licenseEnums) {
			if (licenseEnum.value().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
