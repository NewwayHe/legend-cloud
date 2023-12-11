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
 * 结算单状态.
 *
 * @author legendshop
 */
public enum ShopOrderBillStatusEnum implements IntegerEnum {

	/**
	 * 初始状态
	 */
	INIT(1, "待商家确认"),

	/**
	 * 商家确认
	 */
	SHOP_CONFIRM(2, "商家确认"),

	/**
	 * 平台确认
	 **/
	ADMIN_CONFIRM(3, "平台确认"),

	/**
	 * 结算完成
	 **/
	FINISH(4, "结算完成");

	/**
	 * The num.
	 */
	private final Integer num;

	private final String statusName;

	@Override
	public Integer value() {
		return num;
	}

	public String getStatusName() {
		return statusName;
	}

	ShopOrderBillStatusEnum(Integer num, String statusName) {
		this.num = num;
		this.statusName = statusName;
	}

	public static String getStatusName(Integer value) {
		for (ShopOrderBillStatusEnum statusEnum : ShopOrderBillStatusEnum.values()) {
			if (statusEnum.value().equals(value)) {
				return statusEnum.getStatusName();
			}
		}
		return null;
	}
}
