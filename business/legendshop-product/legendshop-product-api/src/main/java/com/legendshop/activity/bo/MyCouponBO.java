/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "我的优惠券出参")
public class MyCouponBO implements Serializable {

	private static final long serialVersionUID = 5983918230209065309L;
	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private String id;

	/**
	 * 优惠券名称
	 */
	@Schema(description = "优惠券名称")
	private String shopId;

	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private String couponId;

	/**
	 * 优惠券名称
	 */
	@Schema(description = "优惠券名称")
	private String title;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 面额
	 */
	@Schema(description = "面额")
	private String amount;

	/**
	 * 使用门槛，0表示无门槛
	 */
	@Schema(description = "使用门槛，0表示无门槛")
	private String minPoint;

	/**
	 * 使用开始时间
	 */
	@Schema(description = "使用开始时间")
	private String useStartTime;

	/**
	 * 使用结束时间
	 */
	@Schema(description = "使用结束时间")
	private String useEndTime;

	/**
	 * 优惠券使用类型
	 * {@link com.legendshop.activity.enums.CouponUseTypeEnum}
	 */
	@Schema(description = "优惠券使用类型：0全场通用，1指定商品使用，-1排除商品使用")
	private Integer useType;

	/**
	 * 优惠券码
	 */
	@Schema(description = "优惠券码")
	private String couponCode;

	/**
	 * 领取时间
	 */
	@Schema(description = "领取时间")
	private String getTime;

	/**
	 * 使用时间
	 */
	@Schema(description = "使用时间")
	private String useTime;

	/**
	 * 订单编号
	 */
	@Schema(description = "订单编号")
	private String orderNumber;

	/**
	 * 状态：{@link com.legendshop.activity.enums.CouponStatusEnum}
	 */
	@Schema(description = "状态:-1：已失效 0：未开始 1：进行中 2：已暂停 3：已结束")
	private Integer status;
	/**
	 * 优惠券图片
	 */
	@Schema(description = "优惠券图片")
	private String pic;

	/**
	 * 领取方式
	 * {@link com.legendshop.activity.enums.CouponReceiveTypeEnum}
	 */
	@Schema(description = "领取方式，0，免费领取，1，卡密领取，2，积分兑换")
	private Integer receiveType;


	/**
	 * 描述
	 */
	@Schema(description = "描述")
	private String description;
}
