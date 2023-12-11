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
import lombok.Getter;

/**
 * 账单订单类型
 *
 * @author legendshop
 */
@Getter
public enum ShopOrderBillOrderTypeEnum implements IntegerEnum {

	/**
	 * 订单金额
	 */
	ORDER_AMOUNT(1),

	/**
	 * 退款金额
	 */
	REFUND_AMOUNT(2),

	/**
	 * 分销佣金
	 */
	DISTRIBUTION_AMOUNT(3),

	/**
	 * 预售定金
	 */
	PRE_SELL_DEPOSIT(4),

	/**
	 * 拍卖保证金
	 */
	AUCTION_MARGIN(5),

	/**
	 * 积分订单
	 */
	INTEGRAL(6),
	;

	private Integer value;

	ShopOrderBillOrderTypeEnum(Integer value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}

	public static ShopOrderBillOrderTypeEnum fromCode(Integer value) {
		if (null != value) {
			for (ShopOrderBillOrderTypeEnum typeEnum : ShopOrderBillOrderTypeEnum.values()) {
				if (typeEnum.value().equals(value)) {
					return typeEnum;
				}
			}
		}
		return null;
	}
}
