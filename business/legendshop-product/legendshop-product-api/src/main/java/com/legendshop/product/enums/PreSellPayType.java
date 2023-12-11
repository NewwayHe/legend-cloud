/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

/**
 * @author legendshop
 */
public enum PreSellPayType {

	/**
	 * 全款支付
	 */
	FULL_AMOUNT(0),

	/**
	 * 定金尾款支付
	 */
	DEPOSIT(1),

	;
	private Integer code;

	PreSellPayType(Integer code) {
		this.code = code;
	}

	public Integer value() {
		return this.code;
	}
}
