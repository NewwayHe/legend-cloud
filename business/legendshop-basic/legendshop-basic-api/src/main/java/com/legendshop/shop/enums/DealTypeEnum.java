/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import com.legendshop.common.core.enums.StringEnum;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * 平台资金流水交易类型
 *
 * @author legendshop
 */
public enum DealTypeEnum implements StringEnum {
	/**
	 * 商家结算
	 */
	SHOP_BILL("1", "商家结算"),

	/**
	 * 预存款提现
	 */
	PD_WITH_DRAW_CASH("2", "预存款提现"),

	/**
	 * 订单退款
	 */
	REFUND("3", "订单退款"),

	/**
	 * 定金退款
	 */
	DEPOSIT_REFUND("4", "定金退款"),

	/**
	 * 保证金支付
	 */
	PAY_AUCTION("5", "保证金支付"),

	/**
	 * 预存款充值
	 */
	PD_RECHARGE("6", "预存款充值"),

	/**
	 * 订单支付
	 */
	PAY_ORDER("7", "订单支付"),

	/**
	 * 定金支付
	 */
	PAY_DEPOSIT("8", "定金支付");

	final String value;

	final String des;

	DealTypeEnum(String value, String des) {
		this.value = value;
		this.des = des;
	}

	@Override
	public String value() {
		return value;
	}

	public String getDes() {
		return des;
	}


	public static String getRemarks(DealTypeEnum typeEnum, BigDecimal amount, Long userId, String orderNumber, String flag) {
		Assert.notNull(typeEnum, "平台资金流水交易类型不能为空！");
		StringBuffer sb = new StringBuffer();
		switch (typeEnum) {
			case SHOP_BILL:
				sb.append(typeEnum.getDes());
				sb.append("，档期：");
				sb.append(flag);
				sb.append("，商家ID：");
				sb.append(userId);
				sb.append("，结算金额：￥");
				sb.append(amount);
				sb.append("，结算方式：");
				break;
			case PD_WITH_DRAW_CASH:
				sb.append(typeEnum.getDes());
				sb.append("，用户ID：");
				sb.append(userId);
				sb.append("，预存款提现金额");
				sb.append(amount);
				sb.append("，提现方式：");
				break;
			case REFUND:
			case DEPOSIT_REFUND:
				break;
			default:
				break;
		}
		return sb.toString();
	}


	/**
	 * 获取des
	 *
	 * @param value
	 * @return
	 */
	public static String getDes(String value) {
		for (DealTypeEnum dealTypeEnum : DealTypeEnum.values()) {
			if (dealTypeEnum.value().equalsIgnoreCase(value)) {
				return dealTypeEnum.getDes();
			}
		}
		return null;
	}
}
