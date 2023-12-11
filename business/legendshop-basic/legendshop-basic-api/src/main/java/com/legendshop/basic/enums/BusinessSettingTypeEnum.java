/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum BusinessSettingTypeEnum {

	/**
	 * 分类展示设置
	 */
	CATEGORY_DISPLAY_SETTING("CATEGORY_DISPLAY_SETTING", "分类展示设置");


	/**
	 * 类型
	 */
	private final String value;

	/**
	 * 描述
	 */
	private final String des;
}
