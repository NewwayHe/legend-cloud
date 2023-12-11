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
 * 增值税普通发票类型
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum UserInvoiceTitleTypeEnum {

	/**
	 * 个人
	 */
	PERSONAL("PERSONAL", "个人"),

	/**
	 * 企业
	 */
	COMPANY("COMPANY", "企业");


	private final String value;
	private final String des;

	/**
	 * 获取描述
	 *
	 * @param value
	 * @return
	 */
	public static String getDes(String value) {
		for (UserInvoiceTitleTypeEnum titleTypeEnum : values()) {
			if (titleTypeEnum.getValue().equals(value)) {
				return titleTypeEnum.getDes();
			}
		}
		return null;
	}

}
