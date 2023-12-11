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
 * (DistributionWithdrawDetail)DTO
 *
 * @author legendshop
 * @since 2022-03-10 09:54:04
 */
@Data
@Schema(description = "佣金提现DTO")
public class DistributionWithdrawDetailDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -63513438583204279L;

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
	 * 提现金额
	 */
	@Schema(description = "提现金额")
	private BigDecimal amount;

	/**
	 * 净提现金额
	 */
	@Schema(description = "净提现金额（除去手续费和个税）")
	private BigDecimal netAmount;

	/**
	 * 提现类型(WALLET: 钱包， ALI:支付宝， WEIXIN:微信)
	 */
	@Schema(description = "WITHDRAW_DISTRIBUTION_WALLET: 钱包， ALI_DISTRIBUTION_WALLET:支付宝， WECHAT_DISTRIBUTION_WALLET:微信")
	private String withdrawType;

	/**
	 * 手续费
	 */
	@Schema(description = "手续费")
	private BigDecimal withdrawalCharge;

	/**
	 * 个税金额
	 */
	@Schema(description = "个税金额")
	private BigDecimal myselfScaleAmount;

	/**
	 * 审核状态(-1: 审核拒绝, 0:待审核, 1:审核通过)
	 */
	@Schema(description = "审核状态(-1: 审核拒绝, 0:待审核, 1:审核通过)")
	private Integer opStatus;

	/**
	 * 状态(-1提现失败, 0: 处理中, 1: 提现成功)
	 */
	@Schema(description = "状态(-1提现失败, 0: 处理中, 1: 提现成功)")
	private Integer status;

	/**
	 * 累计提现金额(包括本次提现)
	 */
	@Schema(description = "累计提现金额(包括本次提现)")
	private BigDecimal totalAmount;

	/**
	 * 第三方调用参数
	 */
	@Schema(description = "\tPC(\")," +
			"\tH5(\"H5\")," +
			"\tMINI(\"小程序\")," +
			"\tMP(\"公众号\")," +
			"\tAPP(\"App\")," +
			"\tUNKNOWN(\"未知\");")
	private String source;

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

	@Schema(description = "审核时间")
	private Date auditTime;

	@Schema(description = "支付宝提现账号")
	private String account;

	@Schema(description = "真实姓名")
	private String realName;

	@Schema(description = "提现账户id")
	private Long accountId;

	@Schema(description = "提现流水号")
	private Long serialNo;

	@Schema(description = "管理员审核状态")
	private Integer adminOpStatus;

	@Schema(description = "用户昵称")
	private String nickName;

	@Schema(description = "手机号")
	private String mobile;

	@Schema(description = "审核意见")
	private String opRemark;
}
