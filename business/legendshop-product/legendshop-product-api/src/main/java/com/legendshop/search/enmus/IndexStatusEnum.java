/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum IndexStatusEnum {


	/**
	 * 创建索引
	 */

	WAIT(10),

	/**
	 * 删除索引
	 */
	SUCCESS(20),
	;

	private final Integer value;

}
