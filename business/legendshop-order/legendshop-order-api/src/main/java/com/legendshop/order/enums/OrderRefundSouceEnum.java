/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;


import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 退款及退货的状态
 *
 * @author legendshop
 */
public enum OrderRefundSouceEnum implements IntegerEnum {

	/**
	 * 用户
	 */
	USER(0),

	/**
	 * 商家
	 */
	SHOP(1),

	/**
	 * 平台
	 */
	PLATFORM(2),

	;

	private Integer status;

	OrderRefundSouceEnum(Integer status) {
		this.status = status;
	}

	@Override
	public Integer value() {
		return status;
	}

}
