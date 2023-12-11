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
 * 是否承担运费
 *
 * @author legendshop
 */
public enum SupportTransportFreeEnum implements IntegerEnum {
	/**
	 * 商家承担运费
	 **/
	SELLER_SUPPORT(1),

	/**
	 * 买家承担运费
	 **/
	BUYERS_SUPPORT(0);


	/**
	 * The num.
	 */
	private Integer num;

	@Override
	public Integer value() {
		return num;
	}

	SupportTransportFreeEnum(Integer num) {
		this.num = num;
	}

}
