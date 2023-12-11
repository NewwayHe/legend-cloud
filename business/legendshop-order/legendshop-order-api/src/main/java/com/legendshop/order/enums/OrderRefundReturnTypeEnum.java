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
 * 退款及退货的类型
 *
 * @author legendshop
 */
public enum OrderRefundReturnTypeEnum implements IntegerEnum {

/** ------ 操作类型 ------ */

	/**
	 * 仅退款 1
	 */
	REFUND(1),

	/**
	 * 退款及退货 2
	 */
	REFUND_RETURN(2),

	/**
	 * 不用退货 1
	 */
	NO_NEED_GOODS(1),

	/**
	 * 需要退货 2
	 */
	NEED_GOODS(2),

	/**
	 * 商家主动申请帮用户取消退款 3
	 */
	REFUND_CANEL(3),
	;


	private Integer type;

	OrderRefundReturnTypeEnum(Integer type) {
		this.type = type;
	}

	@Override
	public Integer value() {
		return type;
	}

}
