/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单发票开具美剧
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum OrderInvoiceFlagEnum {


	UN_UED(0, "未开具"),

	UED(1, "已开具");


	private final Integer value;
	private final String des;

	/**
	 * 获取des
	 *
	 * @param value
	 * @return
	 */
	public static String getDes(Integer value) {
		for (OrderInvoiceFlagEnum flagEnum : OrderInvoiceFlagEnum.values()) {
			if (flagEnum.getValue().equals(value)) {
				return flagEnum.getDes();
			}
		}
		return null;
	}
}
