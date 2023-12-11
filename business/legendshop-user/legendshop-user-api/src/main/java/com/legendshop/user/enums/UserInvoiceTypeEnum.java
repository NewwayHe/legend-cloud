/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发票类型
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum UserInvoiceTypeEnum {

	/**
	 * 增值税普票
	 */
	NORMAL("NORMAL", "普通发票"),

	/**
	 * 增值税专票
	 */
	DEDICATED("DEDICATED", "增值税发票");


	private final String value;
	private final String des;

	/**
	 * 获取描述
	 *
	 * @param value
	 * @return
	 */
	public static String getDes(String value) {
		for (UserInvoiceTypeEnum userInvoiceTypeEnum : values()) {
			if (userInvoiceTypeEnum.getValue().equals(value)) {
				return userInvoiceTypeEnum.getDes();
			}
		}
		return null;
	}
}
