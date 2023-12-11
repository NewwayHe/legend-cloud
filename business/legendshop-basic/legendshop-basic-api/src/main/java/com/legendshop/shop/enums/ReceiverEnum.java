/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接收人类型
 * 1：普通用户 2：商家 3：平台
 *
 * @author legendshop
 * @create: 2021/6/16 12:01
 */
@Getter
@AllArgsConstructor
public enum ReceiverEnum {

	/**
	 *
	 */
	USER(1),


	SHOP(2),


	PLATFORM(3),

	;

	private final Integer value;

}
