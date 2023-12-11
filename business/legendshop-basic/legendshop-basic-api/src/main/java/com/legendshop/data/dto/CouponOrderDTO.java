/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * (CouponOrder)DTO
 * 订单优惠券使用明细
 *
 * @author legendshop
 * @since 2022-03-25 10:45:35
 */
@Data
@Schema(description = "订单优惠券使用明细")
public class CouponOrderDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -46760160446463632L;

	private Long id;

	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private Long couponId;

	/**
	 * 用户优惠券id
	 */
	@Schema(description = "用户优惠券id")
	private Long userCouponId;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 订单id
	 */
	@Schema(description = "订单id")
	private Long orderId;

	@Schema(description = "订单号")
	private String orderNumber;

	/**
	 * 订单项id
	 */
	@Schema(description = "订单项id")
	private Long orderItemId;

	/**
	 * 优惠金额
	 */
	@Schema(description = "优惠金额")
	private BigDecimal couponItemAmount;

	/**
	 * 订单状态，1、已使用，10、使用后被退
	 */
	@Schema(description = "订单状态，1、已使用，10、使用后被退")
	private Integer status;

	@Schema(description = "是否店铺优惠券")
	private Boolean shopProviderFlag;

	/**
	 * 来源
	 */
	@Schema(description = "来源")
	private String source;
}
