/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 配送方式
 *
 * @author legendshop
 */
@Getter
public enum ProductDeliveryTypeEnum {

	/**
	 * 商家配送
	 */
	EXPRESS_DELIVERY(0),

	/**
	 * 到店自提
	 */
	SINCEMENTION(10),

	/**
	 * 商家配送和及到店自提
	 */
	EXPRESS_DELIVERY_ADN_SINCEMENTION(20),
	;
	private Integer code;


	ProductDeliveryTypeEnum(Integer code) {
		this.code = code;
	}

	ProductDeliveryTypeEnum findByCode(Integer code) {
		return Arrays.stream(ProductDeliveryTypeEnum.values()).filter((item) -> item.getCode().equals(code)).findFirst().orElse(null);
	}
}
