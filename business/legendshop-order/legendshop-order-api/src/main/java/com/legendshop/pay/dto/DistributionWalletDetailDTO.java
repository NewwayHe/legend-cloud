/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import com.legendshop.common.core.annotation.DataSensitive;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.legendshop.common.core.annotation.DataSensitive.SensitiveTypeEnum.MOBILE_PHONE;

/**
 * (DistributionWalletDetail)DTO
 *
 * @author legendshop
 * @since 2022-02-23 16:46:36
 */
@Data
@Schema(description = "分销提现详情DTO")
public class DistributionWalletDetailDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 503535669240731373L;

	/**
	 * 主键id
	 */
	@Schema(description = "主键id")
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	@DataSensitive(type = MOBILE_PHONE)
	@Schema(description = "手机号")
	private String mobile;

	/**
	 * 业务id(与各个业务操作的id相对应)
	 */
	@Schema(description = "业务id(提现:提现明细id, 佣金:佣金明细id )")
	private Long businessId;

	/**
	 * * 业务类型（佣金结算，用户发起提现，提现审核通过， 提现审核拒绝， 提现成功， 提现失败）
	 */
	@Schema(description = "业务类型")
	private String businessType;

	/**
	 * * 金额类型(冻结金额，已结算金额)
	 * <p>
	 * WalletCommissionTypeEnum
	 */
	@Schema(description = "佣金类型 FROZEN_COMMISSION: 冻结金额、SETTLED_COMMISSION: 已结算金额")
	private String commissionType;

	/**
	 * DistributionWithdrawOperationTypeEnum
	 */
	@Schema(description = "操作类型（收入 ADDITION，支出 DEDUCTION）")
	private String operationType;

	/**
	 * 交易类型
	 * WalletTransactionTypeEnum
	 */
	@Schema(description = "交易类型  COMMISSION_SETTLEMENT: 佣金结算、REWARD_SETTLEMENT: 奖励结算、COMMISSION_WITHDRAWAL: 佣金提现")
	private String transactionType;

	/**
	 * 操作金额
	 */
	@Schema(description = "操作金额")
	private BigDecimal commission;

	/**
	 * 操作前已结算金额
	 */
	@Schema(description = "操作前已结算金额")
	private BigDecimal beforeCommission;

	/**
	 * 操作前待结算金额
	 */
	@Schema(description = "操作前待结算金额")
	private BigDecimal afterCommission;

	@Schema(description = "备注")
	private String remarks;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;

}
