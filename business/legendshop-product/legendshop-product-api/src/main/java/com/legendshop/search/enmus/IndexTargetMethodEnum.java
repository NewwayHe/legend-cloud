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
public enum IndexTargetMethodEnum {


	/**
	 * 创建索引
	 */

	CREATE(10),

	/**
	 * 删除索引
	 */
	DELETE(20),

	/**
	 * 更新索引
	 */
	UPDATE(30),
	;

	private final Integer value;

	public static IndexTargetMethodEnum fromCode(Integer value) {
		if (null == value) {
			return null;
		}

		for (IndexTargetMethodEnum indexTargetMethodEnum : IndexTargetMethodEnum.values()) {
			if (value.equals(indexTargetMethodEnum.getValue())) {
				return indexTargetMethodEnum;
			}
		}

		return null;
	}
}
