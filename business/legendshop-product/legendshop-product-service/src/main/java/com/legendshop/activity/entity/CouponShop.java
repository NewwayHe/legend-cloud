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
import lombok.Data;

/**
 * 优惠券商家对应表(CouponShop)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_coupon_shop")
public class CouponShop implements GenericEntity<Long> {

	private static final long serialVersionUID = -29663022250164973L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "COUPON_SHOP_SEQ")
	private Long id;


	/**
	 * 优惠券id
	 */
	@Column(name = "coupon_id")
	private Long couponId;


	/**
	 * 店铺id
	 */
	@Column(name = "shop_id")
	private Long shopId;

}
