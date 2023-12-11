/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.enums.StringEnum;
import com.legendshop.common.core.expetion.BusinessException;

/**
 * sku活动类型，用户标记sku参加什么营销活动
 *
 * @author legendshop
 */
public enum SkuActiveTypeEnum implements StringEnum {

	/**
	 * 默认类型，没有参加营销活动
	 */
	PRODUCT(null),

	/**
	 * 预售活动
	 */
	PRESELL("PRESELL"),

	/**
	 * 积分抵扣
	 */
	INTEGRAL_DEDUCTION("INTEGRAL_DEDUCTION"),

	/**
	 * 积分商品
	 */
	INTEGRAL("INTEGRAL");

	private String value;

	@Override
	public String value() {
		return value;
	}

	/**
	 * Instantiates a new order status enum.
	 *
	 * @param value the value
	 */
	SkuActiveTypeEnum(String value) {
		this.value = value;
	}

	public static SkuActiveTypeEnum fromCode(String value) {
		if (null != value) {
			for (SkuActiveTypeEnum typeEnum : SkuActiveTypeEnum.values()) {
				if (typeEnum.value.equals(value)) {
					return typeEnum;
				}
			}
		}
		throw new BusinessException("sku活动类型不匹配");
	}

	public static SkuActiveTypeEnum getDes(String value) {
		if (ObjectUtil.isNotEmpty(value)) {
			for (SkuActiveTypeEnum skuActiveTypeEnum : SkuActiveTypeEnum.values()) {
				if (value.equals(skuActiveTypeEnum.value())) {
					return skuActiveTypeEnum;
				}
			}
		}
		return null;
	}
}
