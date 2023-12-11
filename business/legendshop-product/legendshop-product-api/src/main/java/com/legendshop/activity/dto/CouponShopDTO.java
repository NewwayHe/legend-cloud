/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 优惠券门店对应表(CouponShop)实体类
 *
 * @author legendshop
 */
@Data
public class CouponShopDTO implements Serializable {


	private static final long serialVersionUID = 8344845576107268650L;
	/**
	 * id
	 */
	private Long id;


	/**
	 * 优惠券id
	 */
	private Long couponId;


	/**
	 * 店铺id
	 */
	private Long shopId;

}
