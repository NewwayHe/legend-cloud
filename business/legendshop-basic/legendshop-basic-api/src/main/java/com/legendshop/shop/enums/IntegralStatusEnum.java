/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.Getter;

/**
 * 积分计划状态
 *
 * @author legendshop
 */
@Getter
public enum IntegralStatusEnum {
	/**
	 * 未参与
	 */
	NOT_JOIN(0),

	/**
	 * 已参与
	 */
	HAS_JOINED(1);

	private Integer value;

	IntegralStatusEnum(Integer value) {
		this.value = value;
	}
}
