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
 * 被关注者用户类型
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum GraUserTypeEnum {

	/**
	 * 个人用户.
	 */
	PERSONAL(2),

	/**
	 * 商家用户.
	 */
	BUSINESS(3),
	;

	private Integer value;
}
