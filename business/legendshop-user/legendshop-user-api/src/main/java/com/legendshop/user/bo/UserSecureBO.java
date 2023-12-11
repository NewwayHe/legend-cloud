/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;

import com.legendshop.common.core.annotation.DataSensitive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import static com.legendshop.common.core.annotation.DataSensitive.SensitiveTypeEnum.MOBILE_PHONE;

/**
 * 用户账户安全信息
 *
 * @author legendshop
 */
@Data
public class UserSecureBO implements Serializable {

	/**
	 * userId
	 */
	@Schema(description = "userId")
	private Long userId;

	/**
	 * 登录密码
	 */
	@Schema(description = "登录密码")
	private String password;

	@Schema(description = "是否设置了登录密码")
	private boolean setPasswordFlag = false;

	/**
	 * 支付密码
	 */
	@Schema(description = "支付密码")
	private String payPassword;

	@Schema(description = "是否设置了支付密码")
	private boolean setPayPasswordFlag = false;

	/**
	 * 号码
	 */
	@Schema(description = "号码")
	@DataSensitive(type = MOBILE_PHONE)
	private String mobile;
}
