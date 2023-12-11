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
 * 订单支付枚举
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum OrderPayEnum {


	UN_PAY(0, "未支付"),

	PAYED(1, "已支付");


	private final Integer value;
	private final String des;

	/**
	 * 获取des
	 *
	 * @param value
	 * @return
	 */
	public static String getDes(Integer value) {
		for (OrderPayEnum payEnum : OrderPayEnum.values()) {
			if (payEnum.getValue().equals(value)) {
				return payEnum.getDes();
			}
		}
		return null;
	}
}
