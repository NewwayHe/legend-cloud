/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预售订单DTO
 *
 * @author legendshop
 * @since 2020-09-09 11:36:38
 */
@Data
public class PreSellOrderDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 775322048097357756L;


	private Long id;

	/**
	 * 订购ID
	 */
	@Schema(description = "订购ID")
	private Long orderId;


	/**
	 * 支付方式,0:全额,1:定金
	 */
	@Schema(description = "支付方式,0:全额,1:定金")
	private Integer payPctType;


	/**
	 * 定金金额
	 */
	@Schema(description = "定金金额")
	private BigDecimal preDepositPrice;


	/**
	 * 尾款金额
	 */
	@Schema(description = "尾款金额")
	private BigDecimal finalPrice;

	/**
	 * 实付金额
	 */
	@Schema(description = "实付金额")
	private BigDecimal actualAmount;


	/**
	 * 定金支付开始时间
	 */
	@Schema(description = "定金支付开始时间")
	private Date preSaleStart;


	/**
	 * 定金支付结束时间
	 */
	@Schema(description = "定金支付结束时间")
	private Date preSaleEnd;


	/**
	 * 尾款支付开始时间
	 */
	@Schema(description = "尾款支付开始时间")
	private Date finalMStart;


	/**
	 * 尾款支付结束时间
	 */
	@Schema(description = "尾款支付结束时间")
	private Date finalMEnd;

	/**
	 * 预售发货时间
	 */
	@Schema(description = "预售发货开始时间")
	private Date preDeliveryTime;

	/**
	 * 预售发货时间
	 */
	@Schema(description = "预售发货结束时间")
	private Date preDeliveryEndTime;


	/**
	 * 定金支付时间
	 */
	@Schema(description = "定金支付时间")
	private Date depositPayTime;


	/**
	 * 是否支付定金
	 */
	@Schema(description = "是否支付定金")
	private Boolean payDepositFlag;


	/**
	 * 尾款支付时间
	 */
	@Schema(description = "尾款支付时间")
	private Date payFinalTime;


	/**
	 * 是否支付尾款
	 */
	@Schema(description = "是否支付尾款")
	private Boolean payFinalFlag;

	/**
	 * 支付方式 PayTypeEnum
	 */
	@Schema(description = "定金支付方式", example = "1:支付宝 2:微信 3:模拟支付 4:线下打款")
	private String depositPayType;

	@Schema(description = "尾款支付方式", example = "1:支付宝 2:微信 3:模拟支付 4:线下打款")
	private String finalPayType;
}
