/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 * @version 1.0.0
 * @title DistributionLevelInfoPropEnum
 * @date 2022/3/18 15:39
 * @description：
 */
@Getter
@AllArgsConstructor
public enum DistributionLevelInfoPropEnum {

	/**
	 * 等级内用户人数
	 */
	USER_TOTAL_COUNT("userTotalCount"),

	/**
	 * 占比
	 */
	PROPORTION("proportion"),

	/**
	 * 增长数
	 */
	GROW_COUNT("growCount"),

	/**
	 * 净增长数
	 */
	NET_GROWTH_COUNT("netGrowthCount"),
	;

	private final String value;

	public static DistributionLevelInfoPropEnum fromCode(String value) {
		if (StrUtil.isBlank(value)) {
			return null;
		}

		for (DistributionLevelInfoPropEnum distributionLevelInfoPropEnum : DistributionLevelInfoPropEnum.values()) {
			if (distributionLevelInfoPropEnum.getValue().equals(value)) {
				return distributionLevelInfoPropEnum;
			}
		}
		return null;
	}

}
