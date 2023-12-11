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
 * 分销员钱包表(DistributionWallet)DTO
 *
 * @author legendshop
 * @since 2022-02-23 16:19:08
 */
@Data
@Schema(description = "分销员钱包表DTO")
public class DistributionWalletDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 193567603930624134L;

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

	/**
	 * 分销员id
	 */
	@Schema(description = "分销员id")
	private Long distributionId;


	/**
	 * 已结算金额
	 */
	@Schema(description = "已结算金额")
	private BigDecimal settledCommission;

	/**
	 * 冻结已结算金额
	 */
	@Schema(description = "冻结已结算金额")
	private BigDecimal frozenSettledCommission;

	/**
	 * 累计支出已结算金额
	 */
	@Schema(description = "累计支出已结算金额")
	private BigDecimal totalReduceSettled;

	/**
	 * 累计收入已结算金额
	 */
	@Schema(description = "累计收入已结算金额")
	private BigDecimal totalAddSettled;

	/**
	 * 状态
	 */
	@Schema(description = "状态")
	private Integer status;

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

	@Schema(description = "操作金额")
	private BigDecimal commission;

	@Schema(description = "提现类型 (WALLET: 钱包， ALI:支付宝， WEIXIN:微信)")
	private String type;

	@Schema(description = "提现账号")
	private String account;

	@Schema(description = "真实姓名")
	private String realName;

	@Schema(description = "提现账号ID")
	private Long accountId;
}
