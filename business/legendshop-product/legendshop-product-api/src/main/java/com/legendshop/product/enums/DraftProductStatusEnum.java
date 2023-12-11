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
 * @author legendshop
 * @version 1.0.0
 * @title DraftProductStatusEnum
 * @date 2022/5/10 17:01
 * @description：-10: 商品未发布、-1: 拒绝、0: 待审核、1: 通过
 */
@Getter
@AllArgsConstructor
public enum DraftProductStatusEnum {

	/**
	 * 商品未发布
	 */
	UNPUBLISHED(-10, "未发布"),

	/**
	 * 拒绝
	 */
	DENY(-1, "拒绝"),

	/**
	 * 待审核
	 */
	WAIT(0, "待审核"),

	/**
	 * 通过
	 */
	PASS(1, "通过"),

	;

	private final Integer value;
	private final String desc;

	public static String fromDesc(Integer value) {
		if (value != null) {
			for (DraftProductStatusEnum draftProductStatusEnum : DraftProductStatusEnum.values()) {
				if (draftProductStatusEnum.getValue().equals(value)) {
					return draftProductStatusEnum.getDesc();
				}
			}
		}
		return "-";
	}
}
