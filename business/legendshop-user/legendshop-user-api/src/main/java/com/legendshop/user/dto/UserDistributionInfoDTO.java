/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户分销员信息
 *
 * @author legendshop
 */
@Data
public class UserDistributionInfoDTO implements Serializable {

	private static final long serialVersionUID = -2766180887973277656L;

	@Schema(description = "分销是否开启")
	private Boolean enabled;

	@Schema(description = "是否参与分销")
	private Boolean participateDistribution;

	@Schema(description = "用户是否为分销员")
	private Boolean userDistribution;

	@Schema(description = "分销员邀请码")
	private String invitationCode;

	/**
	 * 结算方式 0 ： 收货后  1：售后期结束
	 * DistributionSettlementType
	 */
	@Schema(description = "结算方式 0 ： 收货后 1：售后期结束")
	private String settlementType;

	/**
	 * 分销模式：一级分销佣金比例
	 */
	@Schema(description = "分销模式：一级分销佣金比例")
	private BigDecimal firstCommissionRate;

	/**
	 * 分销模式：一级分销佣金比例
	 */
	@Schema(description = "分销模式：二级分销佣金比例")
	private BigDecimal secondCommissionRate;

	/**
	 * 分销模式：一级分销佣金比例
	 */
	@Schema(description = "分销模式：三级分销佣金比例")
	private BigDecimal thirdCommissionRate;

	@Schema(description = "自购开关")
	private String selfPurchasedSwitch;

	public UserDistributionInfoDTO() {
		this.enabled = false;
		this.participateDistribution = false;
		this.userDistribution = false;
		this.invitationCode = null;
		this.firstCommissionRate = BigDecimal.ZERO;
		this.secondCommissionRate = BigDecimal.ZERO;
		this.thirdCommissionRate = BigDecimal.ZERO;
	}

	public UserDistributionInfoDTO(Boolean enabled, Boolean participateDistribution, Boolean userDistribution, String invitationCode) {
		this.enabled = enabled;
		this.participateDistribution = participateDistribution;
		this.userDistribution = userDistribution;
		this.invitationCode = invitationCode;
	}
}
