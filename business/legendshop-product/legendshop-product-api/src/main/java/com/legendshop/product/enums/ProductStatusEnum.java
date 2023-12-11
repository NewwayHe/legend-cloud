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
 * 产品状态.
 * -10：未发布；0：下线；1：上线；3：全部
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ProductStatusEnum implements IntegerEnum {

	/**
	 * 商品未发布
	 */
	UNPUBLISHED(-10, "未发布"),

	/**
	 * 仓库中的商品，用户点击下线.
	 */
	PROD_OFFLINE(0, "下线"),

	/**
	 * 上线的商品，正常销售的商品.
	 */
	PROD_ONLINE(1, "上线"),

	/**
	 * 商品全部状态
	 **/
	PROD_ALL(3, ""),

	;

	/**
	 * The num.
	 */
	private final Integer value;

	private final String desc;

	@Override
	public Integer value() {
		return value;
	}


	public static ProductStatusEnum codeValue(Integer value) {
		if (value != null) {
			for (ProductStatusEnum typeEnum : ProductStatusEnum.values()) {
				if (value.equals(typeEnum.value)) {
					return typeEnum;
				}
			}
		}
		return null;
	}


	public static String fromDesc(Integer value) {
		if (value != null) {
			for (ProductStatusEnum typeEnum : ProductStatusEnum.values()) {
				if (value.equals(typeEnum.value)) {
					return typeEnum.getDesc();
				}
			}
		}
		return null;
	}
}
