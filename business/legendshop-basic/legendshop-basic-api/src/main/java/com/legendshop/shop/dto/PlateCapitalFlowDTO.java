/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.shop.enums.DealTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台资金流水(PlateCapitalFlow)DTO
 *
 * @author legendshop
 * @since 2020-09-18 17:26:12
 */
@Data
@Schema(description = "平台资金流水DTO")
public class PlateCapitalFlowDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 599767514344136200L;

	/**
	 * 支付流水号
	 */
	@Schema(description = "支付流水号")
	@NotBlank(message = "支付流水号不能为空")
	private String paySettlementSn;

	/**
	 * 收支类型 {@link com.legendshop.shop.enums.FlowTypeEnum}
	 */
	@Schema(description = "收支类型")
	@NotBlank(message = "收支类型不能为空")
	private String flowType;

	/**
	 * 交易类型 {@link com.legendshop.shop.enums.DealTypeEnum}
	 */
	@Schema(description = "交易类型")
	@NotNull(message = "交易类型不能为空")
	private DealTypeEnum dealTypeEnum;


	/**
	 * 交易类型，用于出参
	 */
	@Schema(description = "交易类型")
	private String dealType;

	/**
	 * 支付方式 PayTypeEnum
	 */
	@Schema(description = "支付方式")
	private String payType;

	/**
	 * 支付名称
	 */
	@Schema(description = "支付名称")
	private String payTypeName;

	/**
	 * 金额
	 */
	@Schema(description = "金额")
	@NotNull(message = "金额不能为空")
	private BigDecimal amount;

	/**
	 * 记录时间
	 */
	@Schema(description = "记录时间")
	private Date recDate;

	/**
	 * 支付时间
	 */
	@Schema(description = "支付时间")
	private Date payTime;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;


	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	@NotNull(message = "用户id不能为空")
	private Long userId;


	/**
	 * 订单编号
	 */
	@Schema(description = "订单编号")
	@NotBlank(message = "订单编号不能为空")
	private String orderNumber;


	/**
	 * 档期号，用于商家结算
	 */
	@Schema(description = "档期号")
	private String flag;


	/**
	 * 详情id
	 */
	@Schema(description = "详情id")
	@NotNull(message = "详情id不能为空")
	private Long detailId;

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;
}
