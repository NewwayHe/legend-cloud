/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;

import com.legendshop.common.core.enums.IntegerEnum;

/**
 * @author legendshop
 */
public enum AdvertiseStatusEnum implements IntegerEnum {
	/**
	 * 未开始
	 */
	WAIT(0),

	/**
	 * 进行中
	 */
	START(1),

	/**
	 * 已暂停
	 */
	SUSPEND(2),

	/**
	 * 已结束
	 */
	END(3),
	;


	final Integer state;

	AdvertiseStatusEnum(Integer state) {
		this.state = state;
	}

	@Override
	public Integer value() {
		return state;
	}

	public static AdvertiseStatusEnum fromCode(Integer code) {
		for (AdvertiseStatusEnum statusEnum : AdvertiseStatusEnum.values()) {
			if (statusEnum.value().equals(code)) {
				return statusEnum;
			}
		}
		return null;
	}
}
