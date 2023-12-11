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
 * 平台资金流水收支类型
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum FlowTypeEnum {

	/**
	 * 收入
	 */
	INCOME("1", "收入"),

	/**
	 * 支出
	 */
	SPEND("2", "支出");

	private String value;
	private String des;

	/**
	 * 获取des
	 *
	 * @param value
	 * @return
	 */
	public static String getDes(String value) {
		for (FlowTypeEnum flowTypeEnum : FlowTypeEnum.values()) {
			if (flowTypeEnum.getValue().equalsIgnoreCase(value)) {
				return flowTypeEnum.getDes();
			}
		}
		return null;
	}
}
