/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 备货类型  SinceMentionStockUpTypeEnum
 *
 * @author legendshop
 */
@Getter
public enum SinceMentionStockUpTypeEnum {
	/**
	 * 最早当天可提货，下单 天 小时 分钟 后，可提货
	 */
	PICKUPTHEGOODS_THEEARLIESTSAMEDAY(0);
	private Integer code;


	SinceMentionStockUpTypeEnum(Integer code) {
		this.code = code;
	}

	SinceMentionStockUpTypeEnum findByCode(Integer code) {
		return Arrays.stream(SinceMentionStockUpTypeEnum.values()).filter((item) -> item.getCode().equals(code)).findFirst().orElse(null);
	}
}
