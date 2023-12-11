/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 客户账单详情BO
 *
 * @author legendshop
 */
@Schema(description = "客户账单详情BO")
@Data
public class CustomerBillBO implements Serializable {


	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 账单性质 收入支出 参考ModeTypeEnum
	 */
	@Schema(description = "账单性质 收入：INCOME  支出： EXPENDITURE")
	private String mode;

	/**
	 * 账单类型 参考CustomerBillTypeEnum
	 */
	@Schema(description = "账单类型 商品交易：PAYMENT_GOODS 退款：REFUND")
	private String type;


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
	 * 商户单号
	 */
	@Schema(description = "商户单号")
	private String innerPaymentNo;

	/**
	 * 交易状态
	 */
	@Schema(description = "交易状态 1：成功 0：失败")
	private Integer status;

	/**
	 * 交易时间
	 */
	@Schema(description = "交易时间")
	private Date createTime;

	/**
	 * 当前记录（用于关联记录表示）
	 */
	@Schema(description = "当前记录（用于关联记录表示）")
	private Boolean currentFlag;

	/**
	 * 关联记录列表
	 */
	@Schema(description = "关联记录")
	private List<CustomerBillBO> relatedBizOrderList;

}
