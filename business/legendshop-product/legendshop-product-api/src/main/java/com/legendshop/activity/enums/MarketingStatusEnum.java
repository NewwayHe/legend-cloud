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
 * 营销类型枚举
 *
 * @author legendshop
 */
public enum MarketingStatusEnum implements IntegerEnum {

	/**
	 * 未发布
	 */
	OFFLINE(-1),

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

	/**
	 * 已失效
	 */
	INVALID(4),

	/**
	 * 已删除
	 */
	DELETE(-2),


	;

	final Integer state;

	MarketingStatusEnum(Integer state) {
		this.state = state;
	}

	@Override
	public Integer value() {
		return state;
	}

	public static MarketingStatusEnum fromCode(Integer code) {
		for (MarketingStatusEnum statusEnum : MarketingStatusEnum.values()) {
			if (statusEnum.value().equals(code)) {
				return statusEnum;
			}
		}
		return null;
	}
}
