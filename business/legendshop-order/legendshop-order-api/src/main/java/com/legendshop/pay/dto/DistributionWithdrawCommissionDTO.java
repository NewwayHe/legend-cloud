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

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@Schema(description = "已提现佣金，可提现佣金DTO")
public class DistributionWithdrawCommissionDTO {

	@Schema(description = "可提现金额")
	private BigDecimal remainingWithdrawCommission;

	@Schema(description = "已提现金额")
	private BigDecimal withdrawnCommission;
}
