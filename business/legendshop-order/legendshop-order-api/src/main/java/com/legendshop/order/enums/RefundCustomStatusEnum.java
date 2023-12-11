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
 * 退款搜索自定义状态码枚举
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum RefundCustomStatusEnum {

	/**
	 * 待商家确认
	 */
	AUDIT_WAIT_SELLER(1),

	/**
	 * 已拒绝
	 */
	DISAGREE(2),

	/**
	 * 待平台审核
	 */
	APPLY_WAIT_ADMIN(3),

	/**
	 * 待买家退货
	 */
	WAIT_SHIP(4),

	/**
	 * 待收货
	 */
	WAIT_RECEIVE(5),

	/**
	 * 已完成
	 */
	FINISH(6),

	/**
	 * 已取消
	 */
	UNDO(7),
	;
	private final Integer value;
}
