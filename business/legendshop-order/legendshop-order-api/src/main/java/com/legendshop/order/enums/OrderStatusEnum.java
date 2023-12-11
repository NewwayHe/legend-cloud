/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

	/**
	 * 全部
	 */
	ALL(0, "全部"),

	/**
	 * （待付款）没有付款.
	 */
	UNPAID(1, "待付款"),

	/**
	 * 等待成团
	 */
	WAIT(2, "待成团"),


	/**
	 * 预售商品已付定金
	 */
	PRESALE_DEPOSIT(3, "已付定金"),
	/**
	 * （待发货）已经付款,但卖家没有发货.
	 */
	WAIT_DELIVERY(5, "待发货"),

	/**
	 * （待签收）发货，导致实际库存减少.
	 */
	CONSIGNMENT(10, "待签收"),

	/**
	 * （待收货）投递完成，待用户确认收货.
	 */
	TAKE_DELIVER(15, "待收货"),

	/**
	 * todo 暂时没用上
	 */
	TREAT_COMMENT(18, "待评价"),
	/**
	 * （已完成）交易成功（确认收货后），购买数增加1.
	 */
	SUCCESS(20, "已完成"),

	/**
	 * (已取消)交易失败,还原库存
	 */
	CLOSE(-5, "已取消"),

	;

	private final Integer value;
	private final String des;

	public static OrderStatusEnum instance(Integer value) {
		OrderStatusEnum[] enums = values();
		for (OrderStatusEnum statusEnum : enums) {
			if (statusEnum.getValue().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}

	/**
	 * 获取des
	 *
	 * @param value
	 * @return
	 */
	public static String getDes(Integer value) {
		for (OrderStatusEnum orderStatus : OrderStatusEnum.values()) {
			if (orderStatus.getValue().equals(value)) {
				return orderStatus.getDes();
			}
		}
		return null;
	}
}
