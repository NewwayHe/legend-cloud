/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.legendshop.common.core.annotation.DataSensitive;
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
@HeadRowHeight(20)
@ContentRowHeight(20)
public class DistributionWalletDetailExcelDTO implements Serializable {

	private static final long serialVersionUID = 503535669240731373L;

	/**
	 * 用户id
	 */
	@ColumnWidth(20)
	@ExcelProperty("用户Id")
	@Schema(description = "用户id")
	private Long userId;

	@ColumnWidth(20)
	@ExcelProperty("手机号")
	@DataSensitive(type = MOBILE_PHONE)
	@Schema(description = "手机号")
	private String mobile;

	/**
	 * DistributionWithdrawOperationTypeEnum
	 */
	@ColumnWidth(20)
	@ExcelProperty("收支类型")
	@Schema(description = "操作类型（收入 ADDITION，支出 DEDUCTION）")
	private String operationType;

	/**
	 * 交易类型
	 * WalletTransactionTypeEnum
	 */
	@ColumnWidth(20)
	@ExcelProperty("交易类型")
	@Schema(description = "交易类型  COMMISSION_SETTLEMENT: 佣金结算、REWARD_SETTLEMENT: 奖励结算、COMMISSION_WITHDRAWAL: 佣金提现")
	private String transactionType;

	/**
	 * 操作金额
	 */
	@ColumnWidth(20)
	@ExcelProperty("金额")
	@Schema(description = "金额")
	private BigDecimal commission;

	/**
	 * 操作前已结算金额
	 */
	@ColumnWidth(20)
	@ExcelProperty("交易前可用分销佣金金额")
	@Schema(description = "交易前可用分销佣金金额")
	private BigDecimal beforeCommission;

	/**
	 * 操作前待结算金额
	 */
	@ColumnWidth(20)
	@ExcelProperty("交易后可用分销佣金金额")
	@Schema(description = "交易后可用分销佣金金额")
	private BigDecimal afterCommission;

	/**
	 * 创建时间
	 */
	@ColumnWidth(30)
	@ExcelProperty("记录时间")
	@Schema(description = "记录时间")
	private Date createTime;

	@ColumnWidth(200)
	@ExcelProperty("备注")
	@Schema(description = "备注")
	private String remarks;


}
