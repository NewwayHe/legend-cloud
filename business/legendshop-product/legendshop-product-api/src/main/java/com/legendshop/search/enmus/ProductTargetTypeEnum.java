/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.enmus;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ProductTargetTypeEnum
 * @date 2022/2/28 17:39
 * @description： 商品
 */
@Getter
@AllArgsConstructor
public enum ProductTargetTypeEnum implements IndexTargetTypeEnum {

	/**
	 * 统计，包括访问数，购买数，评论数（可自行扩展）
	 */
	STATISTICS(31),

	;

	private final Integer value;

	public static ProductTargetTypeEnum fromCode(Integer value) {
		if (ObjectUtil.isEmpty(value)) {
			return null;
		}

		for (ProductTargetTypeEnum productTargetTypeEnum : ProductTargetTypeEnum.values()) {
			if (productTargetTypeEnum.getValue().equals(value)) {
				return productTargetTypeEnum;
			}
		}
		return null;
	}
}
