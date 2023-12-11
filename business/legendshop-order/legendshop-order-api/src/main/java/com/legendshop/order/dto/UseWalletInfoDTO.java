/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用于表示订单钱包信息状态
 *
 * @author legendshop
 */
@Data
public class UseWalletInfoDTO implements Serializable {

	@Schema(description = "钱包是否可用")
	private Boolean allowed = false;

	@Schema(description = "是否开启标识", required = true)
	@NotNull(message = "是否开启标识不能为空")
	private Boolean useWallet = false;

	@Schema(description = "钱包抵扣金额")
	private BigDecimal amount;
}
