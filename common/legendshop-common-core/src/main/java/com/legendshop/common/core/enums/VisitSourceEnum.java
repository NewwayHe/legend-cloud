/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.enums;

import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
public enum VisitSourceEnum {
	PC("网页"),
	SHOP("商家"),
	PLATFORM("平台"),

	/**
	 * h5和公众号目前合并成h5
	 */
	H5("H5"),
	MP("H5"),
	MINI("小程序"),
	APP("App"),
	UNKNOWN("未知");

	final String desc;

	private VisitSourceEnum(String desc) {
		this.desc = desc;
	}

	public static String getDescByName(String name) {
		VisitSourceEnum[] licenseEnums = values();
		VisitSourceEnum[] var2 = licenseEnums;
		int var3 = licenseEnums.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			VisitSourceEnum licenseEnum = var2[var4];
			if (licenseEnum.name().equals(name)) {
				return licenseEnum.getDesc();
			}
		}

		return UNKNOWN.desc;
	}

	public static VisitSourceEnum getByName(String name) {
		VisitSourceEnum[] licenseEnums = values();
		VisitSourceEnum[] var2 = licenseEnums;
		int var3 = licenseEnums.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			VisitSourceEnum licenseEnum = var2[var4];
			if (licenseEnum.name().equals(name)) {
				return licenseEnum;
			}
		}

		return UNKNOWN;
	}

	public String getDesc() {
		return this.desc;
	}
}
