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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券(Coupon)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_coupon")
public class Coupon extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -855052150846556515L;
	/**
	 * id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "COUPON_SEQ")
	private Long id;

	/**
	 * 礼券提供方是否为店铺
	 */
	@Column(name = "shop_provider_flag")
	private Boolean shopProviderFlag;

	/**
	 * 店铺id
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 优惠券标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 优惠券备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 发放数量
	 */
	@Column(name = "count")
	private Integer count;

	/**
	 * 已使用数量
	 */
	@Column(name = "use_count")
	private Integer useCount;

	/**
	 * 已领取数量
	 */
	@Column(name = "receive_count")
	private Integer receiveCount;

	/**
	 * 状态：{@link com.legendshop.activity.enums.CouponStatusEnum}
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 平台删除状态
	 */
	@Column(name = "platform_delete_status")
	private Boolean platformDeleteStatus;

	/**
	 * 面额
	 */
	@Column(name = "amount")
	private BigDecimal amount;

	/**
	 * 使用门槛，0为无门槛
	 */
	@Column(name = "min_point")
	private BigDecimal minPoint;

	/**
	 * 领取开始时间
	 */
	@Column(name = "receive_start_time")
	private Date receiveStartTime;

	/**
	 * 领取结束时间
	 */
	@Column(name = "receive_end_time")
	private Date receiveEndTime;

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
	 * 多少天后可以使用
	 */
	@Column(name = "use_day_later")
	private Integer useDayLater;

	/**
	 * 几天之内可用
	 */
	@Column(name = "within_day")
	private Integer withinDay;

	/**
	 * 优惠券使用类型
	 * {@link com.legendshop.activity.enums.CouponUseTypeEnum}
	 */
	@Column(name = "use_type")
	private Integer useType;

	/**
	 * 领取方式
	 * {@link com.legendshop.activity.enums.CouponReceiveTypeEnum}
	 */
	@Column(name = "receive_type")
	private Integer receiveType;

	/**
	 * 每人每天限领张数
	 */
	@Column(name = "per_day_limit")
	private Integer perDayLimit;

	/**
	 * 每人总限领张数
	 */
	@Column(name = "per_total_limit")
	private Integer perTotalLimit;

	/**
	 * 积分兑换数
	 */
	@Column(name = "integral")
	private Integer integral;

	/**
	 * 描述
	 */
	@Column(name = "description")
	private String description;

	/**
	 * 优惠券图片
	 */
	@Column(name = "pic")
	private String pic;


	/**
	 * 指定用户{@link com.legendshop.activity.enums.CouponDesignateEnum}
	 */
	@Column(name = "designated_user")
	private Integer designatedUser;

	/**
	 * 指定用户手机号
	 */
	@Column(name = "designated_user_mobile")
	private String designatedUserMobile;

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
}
