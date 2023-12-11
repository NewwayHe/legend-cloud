/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 店铺状态和审核状态
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum ShopDetailStatusEnum {
	/**
	 * 上线正常营业
	 */
	ONLINE(1),

	/**
	 * 店铺下线
	 */
	OFFLINE(0),

	/**
	 * 关闭店铺
	 */
	CLOSED(-1);

	/**
	 * The num.
	 */
	private final Integer status;

	public static ShopDetailStatusEnum getStatusEnum(Integer status) {
		for (ShopDetailStatusEnum value : ShopDetailStatusEnum.values()) {
			if (value.getStatus().equals(status)) {
				return value;
			}
		}
		return null;
	}
}
