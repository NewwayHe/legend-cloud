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
import com.legendshop.common.core.enums.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class VerifyCodeDTO implements Serializable {

	@Schema(description = "需要校验的验证码")
	private String code;

	@Schema(description = "需要校验的手机号")
	private String mobile;

	@Schema(description = "验证码校验的用户类型")
	private UserTypeEnum userType;

	@Schema(description = "验证码类型")
	private SmsTemplateTypeEnum smsTemplateType;

	public VerifyCodeDTO() {
	}


	public VerifyCodeDTO(String code, String mobile, UserTypeEnum userType, SmsTemplateTypeEnum smsTemplateType) {
		this.code = code;
		this.mobile = mobile;
		this.userType = userType;
		this.smsTemplateType = smsTemplateType;
	}
}
