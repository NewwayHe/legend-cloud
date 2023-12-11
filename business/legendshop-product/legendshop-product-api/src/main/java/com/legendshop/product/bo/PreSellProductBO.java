/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预售商品表(PreSellProduct)BO
 *
 * @author legendshop
 * @since 2020-08-18 10:14:33
 */
@Data
public class PreSellProductBO implements Serializable {

	private static final long serialVersionUID = 797181957021774453L;

	/**
	 * 主键
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
	 * 预售开始时间
	 */
	@Schema(description = "预售开始时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date preSaleStart;

	/**
	 * 预售开始时间戳
	 */
	@Schema(description = "预售开始时间戳")
	private Long saleStart;

	/**
	 * 预售截至时间戳
	 */
	@Schema(description = "预售截至时间戳")
	private Long saleEnd;

	/**
	 * 预售结束时间
	 */
	@Schema(description = "预售结束时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date preSaleEnd;


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
	 * 预售发货开始时间戳
	 */
	@Schema(description = "预售发货开始时间戳")
	private Long deliveryTime;

	/**
	 * 预售发货截至时间戳
	 */
	@Schema(description = "预售发货截至时间戳")
	private Long deliveryEndTime;


	/**
	 * 预售支付百分比
	 */
	@Schema(description = "预售支付百分比")
	private BigDecimal payPct;


	/**
	 * 定金支付开始时间
	 */
	@Schema(description = "定金支付开始时间")
	private Long depositStarts;

	/**
	 * 定金支付结束时间
	 */
	@Schema(description = "定金支付截至时间")
	private Long depositEnds;


	/**
	 * 尾款支付开始时间
	 */
	@Schema(description = "尾款支付开始时间")
	private Long finalMStarts;

	/**
	 * 尾款支付结束时间
	 */
	@Schema(description = "尾款支付开始时间")
	private Long finalMEnds;

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
	@Schema(description = "尾款支付开始时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date finalMEnd;


	@Schema(description = "商品预售状态：1：在定金支付时间段内 0：不在定金支付时间段内")
	private Integer status;


	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

}
