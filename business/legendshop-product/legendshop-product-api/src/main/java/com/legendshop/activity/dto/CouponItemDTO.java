/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import com.legendshop.activity.enums.CouponSelectStatusEnum;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.product.dto.ProductItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 优惠券项DTO
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponItemDTO implements Serializable {
	private static final long serialVersionUID = 3708524537924008389L;

	@Schema(description = "用户优惠券ID")
	private Long userCouponId;

	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private Long couponId;

	/**
	 * 优惠券名称
	 */
	@Schema(description = "优惠券名称")
	private String couponName;

	/**
	 * 优惠券备注
	 */
	@Schema(description = "优惠券备注")
	private String remark;

	/**
	 * 优惠券提供方是否为店铺
	 */
	@Schema(description = "优惠券提供方是否为店铺")
	private Boolean shopProviderFlag;

	/**
	 * 选着状态
	 */
	@Schema(description = "优惠券选中状态，1，已选中，0，未选中，-1，不可选")
	private Integer selectStatus = CouponSelectStatusEnum.UN_OPTIONAL.getStatus();

	/**
	 * 不可用原因
	 */
	@Schema(description = "不可用原因")
	private String unAvailableReason;


	/**
	 * 使用类型
	 * {@link com.legendshop.activity.enums.CouponUseTypeEnum}
	 */
	@Schema(description = "使用类型，-1、排除指定商品，0、全场通用，1、包含指定商品")
	private Integer useType;

	/**
	 * 优惠券面额
	 */
	@Schema(description = "优惠券面额")
	private BigDecimal amount;

	/**
	 * 使用门槛；0表示无门槛
	 */
	@Schema(description = "优惠券门槛，0.00代表无门槛")
	private BigDecimal minPoint;

	/**
	 * 使用开始时间
	 */
	@Schema(description = "使用开始时间")
	private Date useStartTime;

	/**
	 * 使用结束时间
	 */
	@Schema(description = "使用结束时间")
	private Date useEndTime;


	/**
	 * 优惠券使用的商品
	 * 1张优惠券对应一个或多个商品
	 */
	@Schema(description = "优惠券对应的商品列表")
	private List<ProductItemDTO> productItems;

	@Schema(description = "优惠券对应的店铺列表")
	private List<Long> shopItems;

	@Schema(description = "请求来源")
	private VisitSourceEnum sourceEnum;

}
