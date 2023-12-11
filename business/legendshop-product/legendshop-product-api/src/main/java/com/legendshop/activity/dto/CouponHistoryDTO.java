/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券历史信息
 *
 * @author legendshop
 * @create: 2021-05-07 13:56
 */
@Data
public class CouponHistoryDTO implements Serializable {

	private static final long serialVersionUID = 7646163273648145730L;

	/**
	 * 类型： 1、领取优惠券，2、使用优惠券
	 */
	@Schema(description = "类型： 1、领取优惠券，2、使用优惠券")
	private Integer type;

	/**
	 * 时间
	 */
	@Schema(description = "时间")
	private Date datetime;

	/**
	 * 金额
	 */
	@Schema(description = "金额")
	private BigDecimal amount;

	@Schema(description = "订单号")
	private Long orderId;
}
