/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class SearchPreSellProductDTO {


	/**
	 * 预售id
	 */
	private Long id;

	/**
	 * 商品id
	 */
	private Long productId;

	/**
	 * 支付方式,0:全额,1:定金
	 */
	@Schema(description = "支付方式,0:全额,1:定金尾款")
	private Integer payPctType;


	/**
	 * 预售支付百分比
	 */
	@Schema(description = "预售支付百分比")
	private BigDecimal payPct;

	/**
	 * 预售开始时间
	 */
	@Schema(description = "预售开始时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date preSaleStartTime;

	/**
	 * 预售结束时间
	 */
	@Schema(description = "预售结束时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date preSaleEndTime;


	/**
	 * 预售发货时间
	 */
	@Schema(description = "预售发货时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date preDeliveryTime;

	/**
	 * 预售发货截至时间
	 */
	@Schema(description = "预售发货截至时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date preDeliveryEndTime;


	/**
	 * 定金支付开始时间
	 */
	@Schema(description = "定金支付开始时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date depositStartTime;

	/**
	 * 定金支付结束时间
	 */
	@Schema(description = "定金支付结束时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date depositEndTime;


	/**
	 * 尾款支付开始时间
	 */
	@Schema(description = "尾款支付开始时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date finalMStartTime;

	/**
	 * 尾款支付结束时间
	 */
	@Schema(description = "尾款支付开始时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date finalMEndTime;
}
