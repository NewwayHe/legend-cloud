/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.activity.enums.CouponUserStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * 优惠券领取表
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_coupon_user")
public class CouponUser implements GenericEntity<Long> {

	private static final long serialVersionUID = -48798235624115502L;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "COUPON_USER_SEQ")
	private Long id;


	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 优惠券id
	 */
	@Column(name = "coupon_id")
	private Long couponId;


	/**
	 * 优惠券标题
	 */
	@Column(name = "coupon_title")
	private String couponTitle;


	/**
	 * 优惠券码
	 */
	@Column(name = "coupon_code")
	private String couponCode;


	/**
	 * 领取时间
	 */
	@Column(name = "get_time")
	private Date getTime;

	/**
	 * 订单编号
	 */
	@Column(name = "order_number")
	private String orderNumber;


	/**
	 * 获取类型：{@link com.legendshop.activity.enums.CouponReceiveTypeEnum}
	 */
	@Column(name = "get_type")
	private Integer getType;


	/**
	 * 优惠券使用状态
	 * {@link CouponUserStatusEnum}
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 使用时间
	 */
	@Column(name = "use_time")
	private Date useTime;

	/**
	 * 使用开始时间
	 */
	@Column(name = "use_start_time")
	private Date useStartTime;

	/**
	 * 使用结束时间
	 */
	@Column(name = "use_end_time")
	private Date useEndTime;

	/**
	 * 卡密
	 */
	@Column(name = "password")
	private String password;

	/**
	 * 提交订单未付款可退还
	 */
	@Column(name = "non_payment_refundable_flag")
	private Boolean nonPaymentRefundableFlag;

	/**
	 * 生成订单申请退款可退还
	 */
	@Column(name = "payment_refundable_flag")
	private Boolean paymentRefundableFlag;

	/**
	 * 生成订单全部商品申请售后可退还
	 */
	@Column(name = "payment_all_after_sales_refundable_flag")
	private Boolean paymentAllAfterSalesRefundableFlag;

	/**
	 * 来源渠道, PC: pc端, H5: h5端, APP: APP端
	 */
	@Column(name = "source")
	private String source;
}
