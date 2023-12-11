/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品上架方式 -1：不上架，0：预约上架，1：审核通过后马上上架
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ProductOnSaleWayEnum {

	/**
	 * 下架，审核通过后不上架、放仓库中
	 */
	OFFSALE(-1),

	/**
	 * 预约上架，审核通过后，到达预约时间后上架
	 */
	PRESALE(0),

	/**
	 * 上架，审核通过后，直接上架
	 */
	ONSALE(1);

	private Integer value;
}
