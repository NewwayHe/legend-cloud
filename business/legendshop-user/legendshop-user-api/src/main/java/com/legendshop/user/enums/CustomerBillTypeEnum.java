/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.enums;


import com.legendshop.common.core.enums.StringEnum;

/**
 * 交易类型
 *
 * @author legendshop
 */
public enum CustomerBillTypeEnum implements StringEnum {

	/**
	 * 商品交易
	 */
	GOODS("PAYMENT_GOODS"),

	/**
	 * 退款
	 */
	REFUND("REFUND"),


	/**
	 * 充值,	提现, 暂时没有此业务场景
	 */
	RECHARGE("RECHARGE"),


	/**
	 * 提现, 暂时没有此业务场景
	 */
	WITHDRAW("WITHDRAW"),


	/**
	 * 保证金回退， 暂时没有此业务场景
	 */
	DEPOSIT_ROLLBACK("DEPOSIT_ROLLBACK"),


	/**
	 * 其他
	 */
	OTHER("OTHER");

	private String value;

	CustomerBillTypeEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return value;
	}

	public static CustomerBillTypeEnum getInstance(String value) {
		for (CustomerBillTypeEnum moneyStatusEnum : CustomerBillTypeEnum.values()) {
			if (moneyStatusEnum.value.equals(value)) {
				return moneyStatusEnum;
			}
		}
		return null;
	}
}
