/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (CouponOrder)BO
 *
 * @author legendshop
 * @since 2022-03-25 10:45:36
 */
@Data
public class CouponOrderBO implements Serializable {

	private static final long serialVersionUID = 276440903195408118L;

	private Long id;

	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private Integer couponId;

	/**
	 * 用户优惠券id
	 */
	@Schema(description = "用户优惠券id")
	private Integer couponUserId;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Integer userId;

	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String orderNumber;

	/**
	 * 订单项号码
	 */
	@Schema(description = "订单项号码")
	private String orderItemNumber;

	/**
	 * 订单状态，1、已使用，10、使用后被退
	 */
	@Schema(description = "订单状态，1、已使用，10、使用后被退")
	private Integer status;

	private Date createTime;

	private Date updateTime;

}
