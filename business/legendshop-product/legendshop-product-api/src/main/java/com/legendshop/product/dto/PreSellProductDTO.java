/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.legendshop.common.core.annotation.DateCompareValid;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预售商品表(PreSellProduct)DTO
 *
 * @author legendshop
 * @since 2020-08-18 10:14:33
 */
@Data
@DateCompareValid
@Schema(description = "预售商品DTO")
public class PreSellProductDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 750226858023612537L;

	/**
	 * 商品id
	 */
	private Long productId;

	/**
	 * 支付方式,0:全额,1:定金
	 */
	@Schema(description = "支付方式,0:全额,1:定金")
	private Integer payPctType;

	/**
	 * 预售开始时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "预售开始时间")
	private Date preSaleStart;

	/**
	 * 预售结束时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "预售结束时间")
	private Date preSaleEnd;

	/**
	 * 预售发货时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "预售发货开始时间")
	private Date preDeliveryTime;

	/**
	 * 预售发货时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "预售发货结束时间")
	private Date preDeliveryEndTime;

	/**
	 * 预售支付百分比
	 */
	@Schema(description = "预售支付百分比")
	@Min(value = 0, message = "预售支付百分比不能小于0")
	@Max(value = 100, message = "预售支付百分比不能大于100")
	private BigDecimal payPct;

	/**
	 * 定金支付开始时间
	 */
	@Schema(description = "定金支付开始时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date depositStart;

	/**
	 * 定金支付结束时间
	 */
	@Schema(description = "定金支付结束时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date depositEnd;

	/**
	 * 尾款支付开始时间
	 */
	@Schema(description = "尾款支付开始时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date finalMStart;

	/**
	 * 尾款支付结束时间
	 */
	@Schema(description = "尾款支付结束时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date finalMEnd;


	@Schema(description = "商品预售状态：1：在定金支付时间段内 0：不在定金支付时间段内")
	private Integer status;

}
