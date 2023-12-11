/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 公告类型
 *
 * @author legendshop
 */
public enum PubTypeEnum implements IntegerEnum {

	/**
	 * 买家公告
	 */
	PUB_BUYERS(0),

	/**
	 * 卖家公告
	 */
	PUB_SELLER(1);

	/**
	 *
	 */
	private Integer num;

	@Override
	public Integer value() {
		return num;
	}

	PubTypeEnum(Integer num) {
		this.num = num;
	}

}
