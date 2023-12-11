/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;


import com.legendshop.common.core.enums.StringEnum;

/**
 * @author legendshop
 */
public enum OrderHistoryEnum implements StringEnum {

	/**
	 * The ORDER_capture.
	 * 下订单
	 */
	ORDER_SUBMIT("CA"),
	/**
	 * 订单支付
	 */
	ORDER_PAY("PY"),

	/**
	 * 订单退货退款 - RG
	 */
	ORDER_RETURNGOOD("RG"),

	/**
	 * 订单退款 - RM
	 */
	ORDER_RETURNMONEY("RM"),

	/**
	 * 商家同意退货退款 - ARG
	 */
	AGREED_RETURNGOOD("ARG"),

	/**
	 * 商家拒绝退货退款 - DARG
	 */
	DISAGREED_RETURNGOOD("DARG"),

	/**
	 * 超时未发货,自动取消售后 - ROT
	 */
	REFUND_OVER_TIME("ROT"),

	/**
	 * 商家同意退款 - ARM
	 */
	AGREED_RETURNMONEY("ARM"),

	/**
	 * 商家拒绝退款 - DARM
	 */
	DISAGREED_RETURNMONEY("DARM"),

	/**
	 * 平台审核通过退货退款 - URG
	 */
	AUDIT_RETURNGOOD("URG"),

	/**
	 * 平台审核通过退款 - URM
	 */
	AUDIT_RETURNMONEY("URM"),

	/**
	 * 买家退货 - BRG
	 */
	BUYERS_RETURNGOOD("BRG"),

	/**
	 * 退货物流已签收 - LR
	 */
	LOGISTICS_RECEIVING("LR"),

	/**
	 * 卖家收到退货 - SRG
	 */
	SELLER_RETURNGOOD("SRG"),

	/**
	 * 确认发货 - DG
	 */
	DELIVER_GOODS("DG"),

	/**
	 * 已收货 - TD
	 */
	TAKE_DELIVER("TD"),

	/**
	 * 确认收货 - CD
	 */
	CONFIRM_DELIVER("CD"),

	/**
	 * 超时,自动确认收货 - TDT
	 */
	TAKE_DELIVER_TIME("TDT"),

	/**
	 * 评价订单 - TD
	 */
	EVALUATE_ORDER("TD"),

	/**
	 * 追加评价订单 - ATD
	 */
	APPEND_EVALUATE_ORDER("ATD"),

	/**
	 * 删除订单 - DE
	 */
	ORDER_DEL("DE"),

	/**
	 * 永久删除订单 - PDE
	 */
	ORDER_PERMANENT_DEL("PDE"),

	/**
	 * 取消订单 - OC
	 */
	ORDER_CANCEL("OC"),


	/**
	 * 超时,自动取消订单
	 */
	ORDER_OVER_TIME("OT"),

	/**
	 * 改价格
	 * /
	 * PRICE_CHANGE("PC"),
	 * <p>
	 * /**
	 * 增加积分
	 */
	CREDIT_SCORE("CS"),

	/**
	 * 使用积分
	 */
	DEBIT_SCORE("DS"),

	/**
	 * 订单状态变化
	 */
	CHANGE_STATUS("ST"),
	/**
	 * 订单信息变化
	 */
	CHANGE_INFO("SI");

	/**
	 * The value.
	 */
	private final String value;

	/**
	 * Instantiates a new sub status enum.
	 *
	 * @param value the value
	 */
	private OrderHistoryEnum(String value) {
		this.value = value;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.StringEnum#value()
	 */
	@Override
	public String value() {
		return this.value;
	}


	/**
	 * Gets the value.
	 *
	 * @param name the name
	 * @return the value
	 */
	public static String getValue(String name) {
		OrderHistoryEnum[] licenseEnums = values();
		for (OrderHistoryEnum licenseEnum : licenseEnums) {
			if (licenseEnum.name().equals(name)) {
				return licenseEnum.value();
			}
		}
		return null;
	}


}
