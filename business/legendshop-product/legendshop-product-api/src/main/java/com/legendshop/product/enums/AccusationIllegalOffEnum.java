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
 * 商品举报处理
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum AccusationIllegalOffEnum {

	/**
	 * 不处理
	 */
	NONE(0),

	/**
	 * 违规下架
	 */
	ILLEGAL(1),

	/**
	 * 锁定
	 */
	LOCK(2);

	private final Integer value;

}
