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
 * 包邮活动关联商品表(ShippingProd)实体类
 *
 * @author legendshop
 */
@Data
public class ShippingProductDTO implements Serializable {


	private static final long serialVersionUID = -3799705501814647631L;
	/**
	 * 包邮活动关联商品表Id
	 */
	private Long id;


	/**
	 * 商家Id
	 */
	private Long shopId;


	/**
	 * 商品Id
	 */
	private Long productId;


	/**
	 * 包邮活动Id
	 */
	private Long shippingId;

}
