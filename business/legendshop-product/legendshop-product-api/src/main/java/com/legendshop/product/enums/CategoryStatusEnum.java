/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;


import com.legendshop.common.core.enums.IntegerEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品类目的状态.
 * -1：无效，该类目下的商品也跟着无效，不能再访问，此状态下需要把该类目下的索引给删除掉；0：下线，不可以再该类目下增加商品；1：上线
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum CategoryStatusEnum implements IntegerEnum {

	/**
	 * 下线的类目，不可以再该类目下增加商品.
	 */
	CATEGORY_OFFLINE(0),

	/**
	 * 上线的类目.
	 */
	CATEGORY_ONLINE(1),

	/**
	 * 无效，该类目下的商品也跟着无效，不能再访问，此状态下需要把该类目下的索引给删除掉
	 **/
	CATEGORY_OFF(-1);

	/**
	 * The num.
	 */
	private final Integer value;

	@Override
	public Integer value() {
		return value;
	}


	public static CategoryStatusEnum codeValue(Integer value) {
		if (value != null) {
			for (CategoryStatusEnum typeEnum : CategoryStatusEnum.values()) {
				if (value.equals(typeEnum.value)) {
					return typeEnum;
				}
			}
		}
		return null;
	}
}
