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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户账单创建入参DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "客户账单创建入参DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBillCreateDTO implements Serializable {


	private static final long serialVersionUID = 6000108028525350237L;
	/**
	 * 账单性质 收入支出 参考ModeTypeEnum
	 */
	@Schema(description = "账单性质 收入支出")
	private String mode;

	/**
	 * 账单类型 参考CustomerBillTypeEnum
	 */
	@Schema(description = "账单类型")
	private String type;

	/**
	 * 账单归属方类型 参考IdentityTypeEnum
	 */
	@Schema(description = "账单归属方类型")
	private String ownerType;

	/**
	 * 账单归属方用户ID
	 */
	@Schema(description = "账单归属方用户ID")
	private Long ownerId;

	/**
	 * 交易说明
	 */
	@Schema(description = "交易说明")
	private String tradeExplain;

	/**
	 * 交易金额
	 */
	@Schema(description = "交易金额")
	private BigDecimal amount;

	/**
	 * 支付方式ID
	 */
	@Schema(description = "支付方式ID")
	private String payTypeId;

	/**
	 * 支付方式名称
	 */
	@Schema(description = "支付方式名称")
	private String payTypeName;

	/**
	 * 业务单号
	 */
	@Schema(description = "业务单号")
	private String bizOrderNo;

	/**
	 * 内部支付流号
	 */
	@Schema(description = "内部支付流号")
	private String innerPaymentNo;


	/**
	 * 关联业务单号
	 */
	@Schema(description = "关联业务单号")
	private String relatedBizOrderNo;


	@Schema(description = "创建时间")
	private Date createTime;


	/**
	 * 用户删除状态
	 */
	@Schema(description = "用户删除状态")
	private Boolean delFlag;


	/**
	 * 交易状态
	 */
	@Schema(description = "交易状态")
	private Integer status;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;
}
