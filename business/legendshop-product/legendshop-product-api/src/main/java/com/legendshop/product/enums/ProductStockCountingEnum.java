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
 * @author legendshop
 */
public enum ProductStockCountingEnum implements IntegerEnum {
	/*
	 * 库存计数方式，0：拍下减库存，1：付款减库存
	 *
	 */
	//0: 拍下减库存
	STOCK_BY_ORDER(0),
	//1：付款减库存
	STOCK_BY_PAY(1);

	/**
	 * The num.
	 */
	private Integer num;

	@Override
	public Integer value() {
		return num;
	}

	ProductStockCountingEnum(Integer num) {
		this.num = num;
	}

}
