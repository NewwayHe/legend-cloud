/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 优惠券BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "优惠券出参")
public class CouponVO implements Serializable {

	private static final long serialVersionUID = -6700134389968194474L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 礼券提供方是否为店铺
	 */
	@Schema(description = "礼券提供方是否为店铺")
	private Boolean shopProviderFlag;

	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 店铺id
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 优惠券标题
	 */
	@Schema(description = "优惠券标题")
	private String title;

	/**
	 * 领取时间
	 */
	@Schema(description = "领取时间")
	private String receiveTime;

	/**
	 * 使用时间
	 */
	@Schema(description = "使用时间")
	private String useTime;

	/**
	 * 使用规则
	 */
	@Schema(description = "使用规则")
	private String rule;

	/**
	 * 限领
	 */
	@Schema(description = "限领")
	private String receiveLimit;

	/**
	 * 领取方式
	 * {@link com.legendshop.activity.enums.CouponReceiveTypeEnum}
	 */
	@Schema(description = "领取方式，0，免费领取，1，卡密领取，2，积分兑换")
	private Integer receiveType;

	/**
	 * 优惠券使用类型
	 * {@link com.legendshop.activity.enums.CouponUseTypeEnum}
	 */
	@Schema(description = "优惠券使用类型：0全场通用，1指定商品使用，-1排除商品使用")
	private Integer useType;

	/**
	 * 已用 | 已领 | 发放数量
	 */
	@Schema(description = "已用 | 已领 | 发放数量")
	private String countCn;

	/**
	 * 优惠券图片
	 */
	@Schema(description = "优惠券图片")
	private String pic;

	/**
	 * 状态：{@link com.legendshop.activity.enums.CouponStatusEnum}
	 */
	@Schema(description = "状态:-1：已失效 0：未开始 1：进行中 2：已暂停 3：已结束")
	private Integer status;

	/**
	 * 面额
	 */
	@Schema(description = "优惠券面额")
	@DecimalMin(value = "0.00", message = "优惠券面额不能小于0")
	@Digits(integer = 6, fraction = 2, message = "优惠券面额保留2位小数且不能大于6位数")
	private BigDecimal amount;


	/**
	 * 使用门槛，0.00为无门槛
	 */
	@DecimalMin(value = "0.00", message = "优惠券门槛不能小于0")
	@Digits(integer = 6, fraction = 2, message = "优惠券使用门槛保留2位小数且不能大于6位数")
	@Schema(description = "使用门槛，0.00为无门槛")
	private BigDecimal minPoint;
}
