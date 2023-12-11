/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 营销活动公共skuDTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "营销活动公共skuDTO")
public class ActivitySkuDTO implements Serializable {

	private static final long serialVersionUID = 4148103898354900559L;

	@Schema(description = "skuId")
	private Long skuId;

	/**
	 * 活动价格
	 */
	@Schema(description = "活动价格")
	private BigDecimal price;

	/**
	 * 活动id
	 */
	@Schema(description = "活动id")
	private Long id;

	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	private Date startTime;

	@Schema(description = "活动开始时间的时间戳")
	private Long startTimeStamp;

	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间")
	private Date endTime;

	@Schema(description = "活动结束时间的时间戳")
	private Long endTimeStamp;

	/**
	 * 活动库存
	 */
	@Schema(description = "活动库存")
	private Integer stocks;

	/**
	 * 活动总库存
	 */
	@Schema(description = "活动总库存")
	private Integer activityStocks;


	/**
	 * 营销活动类型  {@link com.legendshop.product.enums.SkuActiveTypeEnum}
	 */
	@Schema(description = "sku营销活动类型NULL:没有")
	private String skuType;

	/**
	 * 活动状态
	 */
	@Schema(description = "状态[-1：预备状态 0:未开始 1：进行中 2：暂停 10：活动结束 15：失效]")
	private Integer status;

	/**
	 * 活动名称
	 */
	@Schema(description = "活动名称")
	private String name;

	/**
	 * 拼团人数
	 */
	@Schema(description = "拼团人数")
	private Integer peopleNumber;

	/**
	 * 是否限购
	 */
	@Schema(description = "是否限购")
	private Boolean limitFlag;

	/**
	 * {@link com.legendshop.activity.enums.OrderLimitTypeEnum}
	 */
	@Schema(description = "每单限购类型 0：不限购。 1：每人每单购买商品")
	private Integer orderLimitType;

	/**
	 * {@link com.legendshop.activity.enums.LimitTypeEnum}
	 */
	@Schema(description = "活动限购类型 0不限购。1：限制每天每人购买每个SKU。2：限制每天每人购买活动商品。3：限制活动期间每人购买每个SKU。4：限制活动期间每人购买活动商品")
	private Integer limitType;

	/**
	 * 每单限购数量
	 */
	@Schema(description = "每单限购数量")
	private Integer orderLimitNumber;

	/**
	 * 活动限购数量
	 */
	@Schema(description = "活动每人限购数量")
	private Integer activityLimitNumber;

	/**
	 * 是否开启团长免单
	 */
	@Schema(description = "是否开启团长免单")
	private Boolean freeFlag;

	/**
	 * 团长专属折扣
	 * 百分比
	 */
	@Schema(description = "团长专属折扣")
	private Double headerDiscount;

	/**
	 * 团购状态
	 */
	@Schema(description = "团购状态true:成功")
	private Boolean success;


	@Schema(description = "秒杀场次")
	private Integer number;

	@Schema(description = "积分抵扣比例")
	private BigDecimal integralProportion;

	@Schema(description = "抵扣比例")
	private BigDecimal proportion;

	@Schema(description = "抵扣价格")
	private BigDecimal proportionPrice;
}
