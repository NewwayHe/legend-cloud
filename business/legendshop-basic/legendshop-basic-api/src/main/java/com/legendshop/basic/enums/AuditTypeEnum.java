/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum AuditTypeEnum {

	/**
	 * 店铺详情审核
	 */
	SHOP_DETAIL(1, "店铺详情审核"),

	/**
	 * 商品审核
	 */
	PRODUCT(2, "商品审核"),

	/**
	 * 品牌审核
	 */
	BRAND(3, "品牌审核"),

	/**
	 * 文章审核
	 */
	ARTICLE(9, "文章审核"),


	/**
	 * 提现审核
	 */
	WITHDRAWAL(12, "提现审核");
	/**
	 * 类型
	 */
	private final Integer value;

	/**
	 * 描述
	 */
	private final String description;


}
