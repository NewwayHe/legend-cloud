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
 * 订单退货退款类型
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum OrderRefundTypeEnum {

	/**
	 * 撤销
	 */
	REVOKE(-1, "撤销"),

	/**
	 * 退款
	 */
	REFUND(1, "退款"),

	/**
	 * 商家申请取消订单
	 */
	CANCELORDER(3, "商家申请取消订单"),

	/**
	 * 退货
	 */
	RETURN(2, "退款退货");


	private final Integer value;
	private final String desc;

	public static OrderRefundTypeEnum fromCode(Integer value) {
		if (null != value) {
			for (OrderRefundTypeEnum orderRefundTypeEnum : OrderRefundTypeEnum.values()) {
				if (orderRefundTypeEnum.getValue().equals(value)) {
					return orderRefundTypeEnum;
				}
			}
		}
		return null;
	}

	/**
	 * 获取des
	 *
	 * @param value
	 * @return
	 */
	public static String getDes(Integer value) {
		for (OrderRefundTypeEnum orderStatus : OrderRefundTypeEnum.values()) {
			if (orderStatus.getValue().equals(value)) {
				return orderStatus.getDesc();
			}
		}
		return null;
	}
}
