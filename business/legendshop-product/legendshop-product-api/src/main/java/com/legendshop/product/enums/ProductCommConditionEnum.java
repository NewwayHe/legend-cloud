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
 * 商品评论筛选条件枚举
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ProductCommConditionEnum {


	/**
	 * 全部评价类型。
	 */
	ALL("all", "全部"),

	/**
	 * 好评类型。
	 */
	GOOD("good", "好评"),

	/**
	 * 中评类型。
	 */
	MEDIUM("medium", "中评"),

	/**
	 * 差评类型。
	 */
	POOR("poor", "差评"),

	/**
	 * 带有照片的评价类型。
	 */
	PHOTO("photo", "有图"),

	/**
	 * 追加评价类型。
	 */
	APPEND("append", "追评");


	private final String value;
	private final String des;

}
