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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "短信验证码发送")
public class SmsVerificationCodeDTO {

	@Schema(description = "发送手机号 ： 不需要登录请求/userSend/smsVerifyCode；普通用户登录请求/p/userSend/smsVerifyCode；商家用户登录/s/userSend/smsVerifyCode；")
	private String mobile;

	@NotNull(message = "短信发送类型不能为空")
	@Schema(description = "发送验证码类型：注册：REGISTER、登录：LOGIN、身份认证：USER_AUTH、忘记密码：FORGET_PASSWORD、修改登录密码：MODIFY_LOGIN_PASSWORD、修改支付密码：MODIFY_PAY_PASSWORD、换绑手机，旧号码：MODIFY_BINDING_MOBILE、换绑手机，新号码：CONFIRM_MOBILE_BIND")
	private SmsTemplateTypeEnum codeType;

	@Schema(description = "用户类型")
	private String userType;

	@Schema(description = "请求ip地址")
	private String ip;


}
