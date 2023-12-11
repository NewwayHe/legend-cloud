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
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户优惠券DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "用户优惠券DTO")
public class CouponUserDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -9018139485434498268L;

	/**
	 * id
	 */
	@Schema(description = "用户优惠券id")
	private Long id;


	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;


	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private Long couponId;


	/**
	 * 优惠券标题
	 */
	@Schema(description = "优惠券标题")
	private String couponTitle;


	/**
	 * 优惠券码
	 */
	@Schema(description = "优惠券码")
	private String couponCode;


	/**
	 * 领取时间
	 */
	@Schema(description = "领取时间")
	private Date getTime;

	/**
	 * 订单编号
	 */
	@Schema(description = "订单编号")
	private String orderNumber;


	/**
	 * 获取类型：{@link com.legendshop.activity.enums.CouponReceiveTypeEnum}
	 */
	@Schema(description = "领取来源 0->免费获取；1->卡密获取,2->积分兑换,3->店铺对指定用户发放'")
	private Integer getType;


	/**
	 * 优惠券使用状态
	 * {@link CouponUserStatusEnum}
	 */
	@Schema(description = "用户优惠券状态 -1:已失效；0为未开始；1:未使用 2：已使用")
	private Integer status;


	/**
	 * 使用时间
	 */
	@Schema(description = "使用时间")
	private Date useTime;

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
	 * 卡密
	 */
	@Schema(description = "卡密")
	private String password;

	/**
	 * 提交订单未付款可退还
	 */
	@Schema(description = "提交订单未付款可退还")
	private Boolean nonPaymentRefundableFlag;

	/**
	 * 生成订单申请退款可退还
	 */
	@Schema(description = "生成订单申请退款可退还")
	private Boolean paymentRefundableFlag;

	/**
	 * 生成订单全部商品申请售后可退还
	 */
	@Schema(description = "生成订单全部商品申请售后可退还")
	private Boolean paymentAllAfterSalesRefundableFlag;

}
