/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.enums.WalletOperationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 * @version 1.0.0
 * @title DistributionWalletOperationDTO
 * @date 2022/3/30 13:53
 * @description： 钱包操作对象
 */
@Data
public class UserWalletOperationDTO implements Serializable {

	private static final long serialVersionUID = 4651170223781890690L;


	/**
	 * 用户id
	 */
	@NotNull(message = "用户ID不能为空")
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 业务id(提现:提现明细id, 佣金:佣金明细id )
	 */
	@NotNull(message = "业务ID不能为空")
	@Schema(description = "业务id(提现:提现明细id, 佣金:佣金明细id )")
	private Long businessId;

	/**
	 * 业务类型（佣金结算，提现）
	 */
	@NotNull(message = "业务类型不能为空")
	@Schema(description = "业务类型（佣金结算，提现）")
	private WalletBusinessTypeEnum businessType;

	/**
	 * 操作类型（收入，支出）
	 */
	@Schema(description = "操作类型（收入，支出）")
	private WalletOperationTypeEnum operationType;

	/**
	 * 操作金额
	 */
	@NotNull(message = "操作金额不能为空")
	@DecimalMin(value = "0", message = "操作金额不能小于0", inclusive = false)
	@Schema(description = "操作金额")
	private BigDecimal amount;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remarks;
}
