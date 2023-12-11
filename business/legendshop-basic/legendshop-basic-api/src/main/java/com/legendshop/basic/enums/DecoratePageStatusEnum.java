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
 * 装修页面状态
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum DecoratePageStatusEnum {

	/**
	 * 未发布
	 */
	DRAFT(-1),

	/**
	 * 已修改未发布
	 */
	MODIFIED(0),

	/**
	 * 已发布
	 */
	RELEASED(1),

	;

	private final Integer num;

}
