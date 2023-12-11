/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.bo;

import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.enums.CouponUserStatusEnum;
import com.legendshop.common.core.annotation.DataSensitive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import static com.legendshop.common.core.annotation.DataSensitive.SensitiveTypeEnum.MOBILE_PHONE;

/**
 * 用户优惠券BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "用户优惠券出参")
public class CouponUserBO implements Serializable {

	private static final long serialVersionUID = 5324902196827904291L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;


	/**
	 * 优惠券id
	 */
	private Long couponId;


	/**
	 * 优惠券标题
	 */
	private String couponTitle;


	/**
	 * 获取类型：{@link com.legendshop.activity.enums.CouponReceiveTypeEnum}
	 */
	private Integer getType;


	/**
	 * 使用开始时间
	 */
	private Date useStartTime;

	/**
	 * 使用结束时间
	 */
	private Date useEndTime;

	/**
	 * 优惠券码
	 */
	@Schema(description = "优惠券码")
	private String couponCode;

	/**
	 * 手机
	 */
	@DataSensitive(type = MOBILE_PHONE)
	@Schema(description = "手机")
	private String mobile;

	/**
	 * 领取时间
	 */
	@Schema(description = "领取时间")
	private Date getTime;

	/**
	 * 使用时间
	 */
	@Schema(description = "使用时间")
	private Date useTime;

	/**
	 * 订单编号
	 */
	@Schema(description = "订单编号")
	private String orderNumber;

	/**
	 * 优惠券使用状态{@link CouponUserStatusEnum}
	 */
	@Schema(description = "优惠券使用状态")
	private Integer status;

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

	/**
	 * 优惠券详情
	 */
	@Schema(description = "优惠券详情")
	private CouponDTO couponDTO;

}
