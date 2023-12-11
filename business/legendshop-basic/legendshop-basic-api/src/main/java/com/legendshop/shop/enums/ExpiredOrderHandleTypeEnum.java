/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 自提点 过期订单处理类型
 *
 * @author legendshop
 */
@Getter
public enum ExpiredOrderHandleTypeEnum {
	/**
	 * 过期后订单自动完成， 不退款
	 */
	AFTEREXPIRATION_ORDERFULFILLMENT_NOREFUND(0),

	/**
	 * 过期后，订单自动售后， 自动项买家退款
	 */
	AFTEREXPIRATION_AUTOMATICAFTERSALES_REFUND(10);
	private Integer code;


	ExpiredOrderHandleTypeEnum(Integer code) {
		this.code = code;
	}

	public static ExpiredOrderHandleTypeEnum findByCode(Integer code) {
		return Arrays.stream(ExpiredOrderHandleTypeEnum.values()).filter((item) -> item.getCode().equals(code)).findFirst().orElse(null);
	}

}
