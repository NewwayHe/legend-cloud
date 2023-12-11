/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import com.legendshop.activity.enums.CouponUserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 退款订单DTO
 *
 * @author legendshop
 */
@Data
public class OrderRefundCouponDTO implements Serializable {

	private String orderNumber;

	private Long couponId;

	private Long couponUserId;

	private String couponOrderNumber;

	/**
	 * 优惠券使用状态
	 * {@link CouponUserStatusEnum}
	 */
	@Schema(description = "用户优惠券状态 -1:已失效；0为未开始；1:未使用 2：已使用")
	private Integer status;
}
