/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 店铺搜索排序枚举  默认：distance  评分：credit  销量：buys
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum SearchShopSortByEnum {

	/**
	 * 距离
	 */
	DISTANCE("distance"),

	/**
	 * 评分
	 */
	CREDIT("credit"),

	/**
	 * 销量
	 */
	BUYS("buys"),


	;

	private String value;
}
