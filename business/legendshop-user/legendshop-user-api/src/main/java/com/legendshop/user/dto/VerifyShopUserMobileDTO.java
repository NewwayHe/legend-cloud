/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import com.legendshop.basic.enums.SmsTemplateTypeEnum;
import com.legendshop.common.core.annotation.MobileValid;
import com.legendshop.common.core.enums.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
public class VerifyShopUserMobileDTO {

	private static final long serialVersionUID = 4564996990240187448L;

	/**
	 * 商家id
	 */
	@Schema(description = "商家id")
	private Long id;

	/**
	 * 手机号码
	 */
	@Schema(description = "手机号码")
	@MobileValid
	private String mobile;

	@Schema(description = "验证码校验的用户类型")
	private UserTypeEnum userType;

	/**
	 * 验证码
	 */
	@NotNull(message = "验证码不能为空！")
	@Schema(description = "授权码：验证码、临时凭据")
	private String code;

	/**
	 * 验证码
	 */
	@NotNull(message = "请先完成旧手机验证~")
	@Schema(description = "旧手机授权码：验证码、临时凭据")
	private String oldCode;

	@Schema(description = "发送验证码类型：注册：REGISTER、登录：LOGIN、忘记密码：FORGET_PASSWORD、修改登录密码：MODIFY_LOGIN_PASSWORD、修改支付密码：MODIFY_PAY_PASSWORD、换绑手机，旧号码：MODIFY_BINDING_MOBILE、换绑手机，新号码：CONFIRM_MOBILE_BIND")
	private SmsTemplateTypeEnum codeType;

}
