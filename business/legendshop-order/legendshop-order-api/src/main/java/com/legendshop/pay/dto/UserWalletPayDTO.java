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
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
public class UserWalletPayDTO implements Serializable {

	private static final long serialVersionUID = 4133135898386273063L;

	@Schema(description = "钱包是否可用")
	private Boolean available;

	@Schema(description = "可用金额")
	private BigDecimal amount;

	@Schema(description = "支付密码")
	private String payPassword;

	public UserWalletPayDTO(Boolean available, BigDecimal amount, String payPassword) {
		this.amount = Optional.ofNullable(amount).orElse(BigDecimal.ZERO);
		this.available = available;
		this.payPassword = payPassword;
	}
}
