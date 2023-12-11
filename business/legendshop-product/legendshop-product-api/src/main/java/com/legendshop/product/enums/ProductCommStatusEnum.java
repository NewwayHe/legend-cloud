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

/**
 * 商品评论状态
 *
 * @author legendshop
 */
public enum ProductCommStatusEnum implements IntegerEnum {

	/**
	 * 待审核
	 */
	WAIT_AUDIT(0),

	/**
	 * 审核通过
	 */
	SUCCESS(1),

	/**
	 * 审核失败
	 */
	FAIL(-1),
	;

	/**
	 * The num.
	 */
	private Integer num;

	ProductCommStatusEnum(Integer num) {
		this.num = num;
	}

	@Override
	public Integer value() {
		return num;
	}

}
