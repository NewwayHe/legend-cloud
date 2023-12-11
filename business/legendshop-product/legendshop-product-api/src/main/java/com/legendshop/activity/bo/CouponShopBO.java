/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.bo;

import com.legendshop.product.bo.SkuBO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 优惠券店铺
 *
 * @author legendshop
 * @create: 2020-12-21 15:55
 */
@Data
public class CouponShopBO implements Serializable {

	private static final long serialVersionUID = 7721785360268813853L;

	/**
	 * 店铺ID
	 */
	private Long shopId;

	/**
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 店铺头像
	 */
	private String shopAvatar;

	/**
	 * 商品列表
	 */
	private List<SkuBO> skuList;
}
