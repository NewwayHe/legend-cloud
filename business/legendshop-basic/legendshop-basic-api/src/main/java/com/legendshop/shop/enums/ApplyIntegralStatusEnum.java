/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.Getter;

/**
 * 积分计划申请状态
 *
 * @author legendshop
 */
@Getter
public enum ApplyIntegralStatusEnum {

	/**
	 * 从未申请过
	 */
	NOT_APPLY(0),

	/**
	 * 申请加入
	 */
	APPLY_JOIN(1),

	/**
	 * 申请退出
	 */
	APPLY_EXIT(2),
	;

	private Integer status;

	ApplyIntegralStatusEnum(Integer status) {
		this.status = status;
	}
}
