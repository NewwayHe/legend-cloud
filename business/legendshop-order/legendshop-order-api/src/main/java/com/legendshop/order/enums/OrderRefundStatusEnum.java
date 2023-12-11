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
import lombok.Getter;

/**
 * 处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败
 *
 * @author legendshop
 */
@Getter
public enum OrderRefundStatusEnum implements StringEnum {

	/* 未申请售后 */
	PROCESSING("0", "未申请售后"),

	/* 售后中 */
	AFTER_SALES("1", "售后中"),

	/* 退款成功 */
	SUCCESS("2", "售后已完成"),

	/* 退款失败 */
	FAILED("-1", "退款失败"),

	;

	private String status;
	private String des;

	OrderRefundStatusEnum(String status) {
		this.status = status;
	}

	OrderRefundStatusEnum(String status, String des) {
		this.status = status;
		this.des = des;
	}

	@Override
	public String value() {
		return status;
	}

	public static String getDes(String status) {
		for (OrderRefundStatusEnum orderStatus : OrderRefundStatusEnum.values()) {
			if (orderStatus.getStatus().equals(status)) {
				return orderStatus.getDes();
			}
		}
		return null;
	}


}
