/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * 礼券
 * 领取来源
 *
 * @author legendshop
 */
public enum CouponSourceEnum implements StringEnum {

	PC("PC"),

	APP("APP"),

	WEIXIN("WEIXIN"),

	DRAW("DRAW");

	/**
	 * The num.
	 */
	private String type;

	CouponSourceEnum(String type) {
		this.type = type;
	}

	@Override
	public String value() {
		return type;
	}

}
