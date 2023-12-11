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
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 帮助栏目-显示页面枚举
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum DisplayPageEnum implements IntegerEnum {

	//全不显示
	ALL_NOT(0),

	//用户端
	USER(1),

	//商家端
	SHOP(2),

	//全部显示
	ALL_DISPLAY(3);

	private Integer value;

	@Override
	public Integer value() {
		return value;
	}
}
