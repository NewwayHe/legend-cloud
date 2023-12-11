/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
public class UserWithdrawApplyDTO implements Serializable {

	@Schema(description = "用户ID")
	private Long userId;

	@Schema(description = "用户真实姓名")
	private String realName;

	@Schema(description = "支付密码")
	private String payPassword;

	@Schema(description = "提现金额")
	private BigDecimal amount;

	/**
	 * 后续扩展：需要对应扩展提现记录表，记录卡号、开户行、姓名等
	 */
	@Schema(description = "提现渠道：当前默认微信提现")
	private String channel = "WECHAT";

	@Schema(description = "来源")
	private String source;
}
