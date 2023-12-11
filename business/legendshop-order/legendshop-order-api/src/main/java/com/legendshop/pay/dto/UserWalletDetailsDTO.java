/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.pay.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 用户钱包收支记录详情(UserWalletDetails)DTO
 *
 * @author legendshop
 * @since 2021-03-13 14:44:01
 */
@Data
@Schema(description = "用户钱包收支记录详情DTO")
public class UserWalletDetailsDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 122227276972103566L;

	private Long id;

	/**
	 * 用户Id
	 */
	@Schema(description = "用户Id")
	private Long userId;

	/**
	 * 流水号
	 */
	@Schema(description = "流水号")
	private Long serialNo;

	/**
	 * 业务编码
	 */
	@Schema(description = "业务编码")
	private Long businessId;

	/**
	 * 业务类型
	 */
	@Schema(description = "业务类型")
	private WalletBusinessTypeEnum businessType;

	/**
	 * 金额类型(冻结金额，已结算金额)
	 * WalletAmountTypeEnum
	 */
	@Schema(description = "金额类型(冻结金额，已结算金额)")
	private UserWalletAmountTypeEnum amountType;

	/**
	 * 操作金额
	 */
	@Schema(description = "操作金额")
	private BigDecimal amount;

	/**
	 * 操作类型
	 */
	@Schema(description = "操作类型， DEDUCTION：扣款、ADDITION：添加")
	private WalletOperationTypeEnum operationType;

	/**
	 * WalletDetailsStateEnum
	 */
	@Deprecated
	@Schema(description = "记录状态（-1：失效） 1：完成、0：默认、999：异常")
	private Integer state;

	@Schema(description = "记录备注")
	private String remarks;

	/**
	 * 收支签名；MD5(时间+金额+用户ID+流水号)
	 */
	@Schema(description = "收支签名；MD5(时间+金额+用户ID+流水号)")
	private String signature;

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

	/**
	 * 渠道来源（目前用于提现）
	 * VisitSourceEnum
	 */
	@Deprecated
	@Schema(description = "渠道来源（目前用于提现）")
	private String source;

	// ================================================================

	@Schema(description = "业务类型名称，用于前端显示")
	private String businessTypeName;

	@Deprecated
	@Schema(description = "用户手机号")
	private String mobile;

	@Deprecated
	@Schema(description = "用户昵称")
	private String nickName;

	@Deprecated
	@Schema(description = "分销等级")
	private Integer grade;

	@Schema(description = "平台的操作状态 0待审核 1通过 2拒绝")
	private Integer opStatus;

	@Deprecated
	@Schema(description = "手续费,不是百分比")
	private BigDecimal withdrawalCharge;

	@Deprecated
	@Schema(description = "状态，用于前端展示， （-1：失效） 1：完成、0：默认、999：异常")
	private String stateName;

	@Deprecated
	@Schema(description = "可用金额(不包括本次操作)")
	private BigDecimal availableAdmount;


	public Integer getOpStatus() {
		if (ObjectUtil.isEmpty(opStatus)) {
			return WalletDetailsAuditEnum.WAIT.getCode();
		}
		return opStatus;
	}

	public String getStateName() {
		StringBuilder sb = new StringBuilder();
		if (WalletOperationTypeEnum.DEDUCTION.equals(operationType)) {
			sb.append("支出");

		} else {
			sb.append("收入");
		}

		if (null == state) {
			return "-";
		}

		switch (Objects.requireNonNull(WalletDetailsStateEnum.getInstance(state))) {
			case DEFAULT:
				return String.valueOf(sb.append("处理中"));
			case INVALID:
				return String.valueOf(sb.append("失败"));
			case FULFILL:
				return String.valueOf(sb.append("完成"));
			case ABNORMAL:
				return String.valueOf(sb.append("失败"));
			case PROCESSING:
				return String.valueOf(sb.append("处理中"));

			default:
				return "-";
		}

	}


	public String getBusinessTypeName() {
		return WalletBusinessTypeEnum.getValue(businessType.name());
	/*	switch (Objects.requireNonNull(WalletBusinessTypeEnum.getCode(businessType.getCode()))) {
			case DISTRIBUTION_REWARDS:
				return "分销奖励";
			case SELF_DISTRIBUTION_REWARDS:
				return "自购返佣";
			case PAYMENT_RECHARGE:
				return "付款充值";
			case PLATFORM_COMPENSATION:
				return "平台补偿";
			case ORDER_DEDUCTION:
				return "订单抵扣";
			case ORDER_OVERTIME_REFUND:
				return "抵扣退还";
			case PAYMENT_DEDUCTION:
				return "支付抵扣";
			case CASH_WITHDRAWAL:
				return "现金提现";
			case DISTRIBUTION_WITHDRAWAL:
				return "佣金提现";
			case PLATFORM_DEDUCTION:
				return "平台扣款";
			case REFUND_COMPENSATION:
				return "订单退款";
			case DISTRIBUTION_WITHDRAW:
				return "佣金提现";

			default:
				return "-";
		}*/
	}
}
