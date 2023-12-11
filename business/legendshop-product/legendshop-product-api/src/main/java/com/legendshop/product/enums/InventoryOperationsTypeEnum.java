/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import lombok.Getter;

/**
 * 库存操作类型
 *
 * @author legendshop
 */
@Getter
public enum InventoryOperationsTypeEnum {

	/**
	 * 全部操作
	 */
	ALL(0, "全部操作"),

	/**
	 * 销售库存
	 */
	SALES(1, "操作销售库存"),

	/**
	 * 实际库存
	 */
	ACTUAL(2, "操作实际库存"),
	;

	final Integer code;

	final String desc;

	InventoryOperationsTypeEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
