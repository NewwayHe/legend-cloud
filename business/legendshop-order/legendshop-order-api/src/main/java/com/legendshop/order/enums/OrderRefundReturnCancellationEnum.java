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
 * 取消创建来源  1用户取消售后 2逾期取消
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum OrderRefundReturnCancellationEnum {
	/* 1用户取消售后 */
	USERCANEL(1, "用户取消售后"),

	/* 2逾期取消 */
	OVERDUECANEL(2, "逾期取消"),
	/* 待用户寄回 */
	USERSENDSBACK(3, "待用户寄回"),
	/* 系统自动处理 */
	SYSTEMAUTOMATICALLY(4, "系统自动处理"),
	/* 商家已确认收货 */
	SHOPCONFIRMRECEIPT(5, "商家已确认收货"),
	/* 商家弃货 */
	ABANDONEDGOODS(6, "商家弃货"),
	/* 用户发的物流单号有异常 */
	ORDERABNORMAL(7, "用户发的物流单号有异常");

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
