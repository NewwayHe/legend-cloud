/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 浏览历史访问的页面类型
 *
 * @author legendshop
 */
public enum VitLogPageEnum implements IntegerEnum {

	/**
	 * 商品页
	 */
	PRODUCT_PAGE(0),

	/**
	 * 店铺页
	 */
	SHOP_PAGE(1),

	;

	private Integer value;

	VitLogPageEnum(Integer value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}
}
