/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.enums;


import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 预支付的类型
 * 余额支付类型(1:预付款  2:金币)
 *
 * @author legendshop
 */
public enum PrePayTypeEnum implements IntegerEnum {

	/**
	 * 预存款
	 **/
	PREDEPOSIT(1),

	;

	/**
	 * The value.
	 */
	private final Integer value;

	/**
	 * Instantiates a new role enum.
	 *
	 * @param value the value
	 */
	private PrePayTypeEnum(Integer value) {
		this.value = value;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.StringEnum#value()
	 */
	@Override
	public Integer value() {
		return this.value;
	}


}
