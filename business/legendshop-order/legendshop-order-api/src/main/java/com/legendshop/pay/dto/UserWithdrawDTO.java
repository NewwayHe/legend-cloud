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

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
public class UserWithdrawDTO implements Serializable {

	private static final long serialVersionUID = 3510450381985950L;

	@Schema(description = "单据流水号")
	private Long withdrawSerialNo;

	@Schema(description = "用户ID")
	private Long userId;

	@Schema(description = "提现金额")
	private BigDecimal amount;

	/**
	 * 渠道来源（目前用于提现）
	 * VisitSourceEnum
	 */
	@Schema(description = "渠道来源（目前用于提现）")
	private String source;

	@Schema(description = "提现账号")
	private String account;

	@Schema(description = "真实姓名")
	private String realName;

	public UserWithdrawDTO(DistributionWithdrawDetailDTO walletDetailsDTO) {
		this.withdrawSerialNo = walletDetailsDTO.getSerialNo();
		this.userId = walletDetailsDTO.getUserId();
		// 只获取净提现金额
		this.amount = walletDetailsDTO.getNetAmount();
		this.source = walletDetailsDTO.getSource();
		this.account = walletDetailsDTO.getAccount();
		this.realName = walletDetailsDTO.getRealName();
	}
}
