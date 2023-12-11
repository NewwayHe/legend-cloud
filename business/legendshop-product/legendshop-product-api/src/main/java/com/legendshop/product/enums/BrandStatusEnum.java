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
 * 品牌状态.
 *
 * @author legendshop
 */
public enum BrandStatusEnum implements IntegerEnum {
	// 1: 上线， 0： 下线
	/**
	 * 上线 审核成功， 成功后可以上线，下线
	 */
	BRAND_ONLINE(1),
	/**
	 * 下线,平台自己操作
	 */
	BRAND_OFFLINE(0);


	/**
	 * The num.
	 */
	private Integer num;

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.IntegerEnum#value()
	 */
	@Override
	public Integer value() {
		return num;
	}

	/**
	 * Instantiates a new product status enum.
	 *
	 * @param num the num
	 */
	BrandStatusEnum(Integer num) {
		this.num = num;
	}

}
