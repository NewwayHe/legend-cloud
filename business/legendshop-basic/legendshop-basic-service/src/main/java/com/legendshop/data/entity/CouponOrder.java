/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * (CouponOrder)实体类
 *
 * @author legendshop
 * @since 2022-03-25 10:45:34
 */
@Data
@Entity
@Table(name = "ls_coupon_order")
public class CouponOrder extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 618304506039813820L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "couponOrder_SEQ")
	private Long id;

	/**
	 * 优惠券id
	 */
	@Column(name = "coupon_id")
	private Integer couponId;

	/**
	 * 用户优惠券id
	 */
	@Column(name = "user_coupon_id")
	private Integer userCouponId;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Integer userId;

	/**
	 * 订单id
	 */
	@Column(name = "order_id")
	private Long orderId;

	/**
	 * 订单状态，10、已使用，20、使用后被退
	 */
	@Column(name = "status")
	private Integer status;


	@Column(name = "create_time")
	private Date createTime;


	@Column(name = "update_time")
	private Date updateTime;


	@Column(name = "coupon_item_aount")
	private BigDecimal couponItemAount;

	@Column(name = "order_item_id")
	private Long orderItemId;

	/**
	 * 来源
	 */
	@Column(name = "source")
	private String source;
}
