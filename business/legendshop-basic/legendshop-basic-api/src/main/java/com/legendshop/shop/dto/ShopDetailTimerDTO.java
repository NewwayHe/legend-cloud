/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 定时器用的商家实体类.
 *
 * @author legendshop
 */
@Data
public class ShopDetailTimerDTO implements Serializable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 9001963640831542538L;

	/**
	 * The shop id.
	 */
	private Long shopId;

	/**
	 * 商城名字.
	 */
	private String siteName;

	/**
	 * 商城独立分销比例
	 */
	private Double commissionRate;
}
