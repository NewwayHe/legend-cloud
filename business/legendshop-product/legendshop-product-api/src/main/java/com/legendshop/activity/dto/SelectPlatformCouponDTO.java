/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 选择平台优惠券
 *
 * @author legendshop
 * @create: 2021-01-15 11:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectPlatformCouponDTO implements Serializable {

	private static final long serialVersionUID = -4069661237077439524L;

	/**
	 * 商家信息
	 */
	private List<ShopCouponDTO> shopCouponList;

	/**
	 * 平台优惠券
	 */
	private List<CouponItemDTO> platformCoupons;

	/**
	 * 用户选择的优惠券ID
	 */
	private List<Long> couponIds;
}
