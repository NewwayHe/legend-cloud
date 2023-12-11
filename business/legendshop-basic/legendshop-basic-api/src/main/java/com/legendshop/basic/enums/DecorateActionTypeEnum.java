/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * 装修点击动作类型
 *
 * @author legendshop
 */
public enum DecorateActionTypeEnum implements StringEnum {

	/**
	 * 商品详情
	 */
	PROD_DETAIL("prodDetail"),

	/**
	 * 商品列表
	 */
	PROD_LIST("prodList"),

	/**
	 * 优惠券或红包
	 */
	COUPON("coupon"),

	/**
	 * 常用功能
	 */
	METHOD("method"),

	/**
	 * 营销活动
	 */
	MARKETING("marketing"),

	/**
	 * 自定义页面
	 */
	PAGE("page"),

	/**
	 * 自定义url
	 */
	URL("url");

	private final String value;

	DecorateActionTypeEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return value;
	}
}
