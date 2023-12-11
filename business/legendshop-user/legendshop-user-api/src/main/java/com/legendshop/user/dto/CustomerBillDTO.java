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
import java.util.Date;
import java.util.List;

/**
 * 客户账单DTO
 *
 * @author legendshop
 */
@Schema(description = "客户账单DTO")
@Data
public class CustomerBillDTO implements Serializable {


	@Schema(description = "主键ID")
	private Long id;


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

	/**
	 * 交易状态
	 */
	@Schema(description = "交易状态")
	private Integer status;

	/**
	 * 账单创建时间
	 */
	@Schema(description = "账单创建时间")
	private Date createTime;

	/**
	 * 用户删除状态
	 */
	@Schema(description = "用户删除状态")
	private Boolean delFlag = false;

	/**
	 * 批量删除使用 传递ID集合
	 */
	@Schema(description = "批量删除使用 传递ID集合")
	private List<Long> ids;

	/**
	 * 备注 订单支付 定金支付 尾款支付 给前端做拼接展示
	 */
	@Schema(description = "备注 订单支付 定金支付 尾款支付 给前端做拼接展示")
	private String remark;

}
