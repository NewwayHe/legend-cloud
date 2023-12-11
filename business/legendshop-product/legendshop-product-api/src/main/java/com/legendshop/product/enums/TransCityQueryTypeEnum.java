/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import com.legendshop.common.core.enums.IntegerEnum;
import lombok.Getter;

/**
 * 配送区域类型
 *
 * @author legendshop
 */
@Getter
public enum TransCityQueryTypeEnum implements IntegerEnum {

	/**
	 * 按重/体积/件计算运费
	 */
	FREIGHT_CAL(1),

	/**
	 * 固定运费
	 */
	CONSTANT_FREIGHT(2),

	/**
	 * 包邮
	 */
	AREA_LIMIT(3),

	/**
	 * 默认
	 */
	DEFAULT(999);

	private Integer value;

	TransCityQueryTypeEnum(Integer value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}

	public static TransCityQueryTypeEnum fromCode(Integer value) {
		if (null != value) {
			for (TransCityQueryTypeEnum typeEnum : TransCityQueryTypeEnum.values()) {
				if (typeEnum.value.equals(value)) {
					return typeEnum;
				}
			}
		}
		return DEFAULT;
	}
}
