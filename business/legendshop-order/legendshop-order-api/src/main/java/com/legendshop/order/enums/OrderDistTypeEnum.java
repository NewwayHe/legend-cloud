/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单分佣类型
 * 分销类型：1：推广分佣  2：链路分佣
 *
 * @author legendshop
 * @create: 2021/5/13 14:42
 */
@Getter
@AllArgsConstructor
public enum OrderDistTypeEnum {

	/**
	 * 推广分佣
	 */
	PROMOTIONAL_COMMISSION(1),

	/**
	 * 链路分佣
	 */
	LINK_COMMISSION(2),

	;

	private final Integer value;
}
