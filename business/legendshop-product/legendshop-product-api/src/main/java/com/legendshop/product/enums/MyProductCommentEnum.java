/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * @author legendshop
 */
public enum MyProductCommentEnum implements StringEnum {

	/**
	 * 全部
	 */
	ALL("0"),

	/**
	 * 待评论
	 */
	WAIT_COMMENT("1"),

	/**
	 * 待追评和追评
	 */
	ALL_COMMENT("2"),

	/**
	 * 已追评
	 */
	ADD_COMMENTED("3"),
	;

	/**
	 * The num.
	 */
	private String num;

	MyProductCommentEnum(String num) {
		this.num = num;
	}

	@Override
	public String value() {
		return num;
	}
}
