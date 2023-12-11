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
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ProductGroupSortEnum implements StringEnum {

	/**
	 * 销量
	 */
	BUYS("buys"),

	/**
	 * 发布时间
	 */
	CREATE_TIME("createTime"),
	;

	/**
	 * The num.
	 */
	private String value;

	@Override
	public String value() {
		return value;
	}
}
