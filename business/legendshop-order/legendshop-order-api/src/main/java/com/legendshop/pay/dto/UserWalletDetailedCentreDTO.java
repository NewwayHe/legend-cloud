/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户钱包详情中间表(UserWalletDetailedCentre)DTO
 *
 * @author legendshop
 * @since 2022-04-27 14:06:35
 */
@Data
@Schema(description = "用户钱包详情中间表DTO")
public class UserWalletDetailedCentreDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -95800490955964825L;

	/**
	 * 主键id
	 */
	@Schema(description = "主键id")
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Integer userId;

	/**
	 * 业务id(提现:提现明细id, 佣金:佣金明细id )
	 */
	@Schema(description = "业务id(提现:提现明细id, 佣金:佣金明细id )")
	private Integer businessId;

	/**
	 * 业务类型（佣金结算，提现）
	 */
	@Schema(description = "业务类型（佣金结算，提现）")
	private String businessType;

	/**
	 * 操作类型（收入，支出）
	 */
	@Schema(description = "操作类型（收入，支出）")
	private String operationType;

	/**
	 * 金额类型(冻结金额，已结算金额)
	 */
	@Schema(description = "金额类型(冻结金额，已结算金额)")
	private String amountType;

	/**
	 * 操作金额
	 */
	@Schema(description = "操作金额")
	private BigDecimal amount;

	/**
	 * 状态（0: 不处理，10: 未处理（未通知），20: 未处理（已通知），30: 已处理）
	 * WalletCentreStatusEnum
	 */
	@Schema(description = "状态（0: 未处理，1: 已处理）")
	private Integer status;

	/**
	 * 操作备注
	 */
	@Schema(description = "操作备注")
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
