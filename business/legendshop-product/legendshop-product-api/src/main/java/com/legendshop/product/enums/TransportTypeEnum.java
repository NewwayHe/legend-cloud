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
 * 是否固定运费还是运费模板类型
 *
 * @author legendshop
 */
public enum TransportTypeEnum implements IntegerEnum {

	/**
	 * 运费模版
	 **/
	TRANSPORT_TEMPLE(0),

	/**
	 * 固定运费
	 **/
	FIXED_TEMPLE(1);

	private Integer num;

	@Override
	public Integer value() {
		return num;
	}

	TransportTypeEnum(Integer num) {
		this.num = num;
	}

}
