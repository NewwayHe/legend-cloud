/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;


import com.legendshop.common.core.enums.StringEnum;

/**
 * 处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败
 *
 * @author legendshop
 */
public enum OrderRefundHandleStatusEnum implements StringEnum {

	/* 退款中 */
	PROCESSING("0"),

	/* 退款成功 */
	SUCCESS("1"),

	/* 退款失败 */
	FAILED("-1");

	private String status;

	OrderRefundHandleStatusEnum(String status) {
		this.status = status;
	}

	@Override
	public String value() {
		return status;
	}

}
