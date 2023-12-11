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
 * 预售商品状态
 *
 * @author legendshop
 */
public enum PreSellProductStatusEnum implements IntegerEnum {

	/**
	 * 未提审 状态:-2
	 */
	NOT_PUT_AUDIT(-2),

	/**
	 * 待审核 状态:-1
	 */
	WAIT_AUDIT(-1),

	/**
	 * 上线（审核通过） 状态:0
	 */
	ONLINE(0),

	/**
	 * 拒绝 状态:1
	 */
	DENY(1),

	/**
	 * 完成 状态:2
	 */
	FINISH(2),

	/**
	 * 已终止 状态:3
	 */
	STOP(3),

	/**
	 * 已过期标记
	 */
	EXPIRED(-3),
	;

	/**
	 * The num.
	 */
	private int num;

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.LongEnum#value()
	 */
	@Override
	public Integer value() {
		return num;
	}

	/**
	 * Instantiates a new shop status enum.
	 *
	 * @param num the num
	 */
	PreSellProductStatusEnum(int num) {
		this.num = num;
	}
}
