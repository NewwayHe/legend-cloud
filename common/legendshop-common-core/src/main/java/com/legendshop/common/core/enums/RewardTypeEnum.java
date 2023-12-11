/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum RewardTypeEnum {
	UPGRADE(1, "升级奖励"),
	PULL_NEW(0, "拉新奖励"),
	;

	private final Integer value;

	private final String desc;

}
