/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 站内信接收者类型
 *
 * @author legendshop
 */
public enum MsgReceiverTypeEnum implements IntegerEnum {

	/**
	 * 普通用户
	 */
	ORDINARY_USER(1),

	/**
	 * 商家用户
	 */
	SHOP_USER(2),

	/**
	 * 平台用户
	 */
	ADMIN_USER(3);

	private Integer value;

	MsgReceiverTypeEnum(Integer value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}
}
