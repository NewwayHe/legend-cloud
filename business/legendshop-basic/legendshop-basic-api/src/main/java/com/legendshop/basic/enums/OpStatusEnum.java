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
public enum OpStatusEnum {


	/**
	 * 待审核
	 */
	WAIT(0),

	/**
	 * 通过
	 */
	PASS(1),

	/**
	 * 拒绝
	 */
	DENY(-1),

	/**
	 * 全部商品
	 */
	PROD_ALL(5),

	/**
	 * 商品违规下线
	 */
	PROD_ILLEGAL_OFF(2),

	/**
	 * 商品违规锁定
	 */
	PROD_ILLEGAL_LOCK(3),

	/**
	 * 商品违规全部
	 */
	PROD_ILLEGAL_ALL(4),
	;


	private final Integer value;

}
