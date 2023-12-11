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
 * 商品咨询类型 The Enum CpnsTypeEnum.
 *
 * @author legendshop
 */
public enum SendCouponTypeEnum implements IntegerEnum {
	/**
	 * 线上用户
	 */
	ONLINE(0),
	/**
	 * 线下发送
	 */
	OFFLINE(1);


	/**
	 * The num.
	 */
	private Integer type;

	/**
	 * Instantiates a new consult type enum.
	 *
	 * @param num the num
	 */
	SendCouponTypeEnum(Integer type) {
		this.type = type;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.IntegerEnum#value()
	 */
	@Override
	public Integer value() {
		return type;
	}

}
