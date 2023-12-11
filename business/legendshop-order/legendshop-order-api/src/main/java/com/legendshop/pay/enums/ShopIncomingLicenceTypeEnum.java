/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 进件身份类型枚举
 * <p>
 * ID_CARD(法人身份证)
 * PASSPORT(护照)
 * HMT_VISITORPASS(港澳台居民往来内地通行证)
 * SOLDIER(士兵证)
 * OFFICERS(军官证)
 * OVERSEAS_CARD(境外证件)
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ShopIncomingLicenceTypeEnum {

	/**
	 *
	 */
	ID_CARD("ID_CARD", "法人身份证"),

	PASSPORT("PASSPORT", "护照"),

	HMT_VISITORPASS("HMT_VISITORPASS", "港澳台居民往来内地通行证"),

	SOLDIER("SOLDIER", "士兵证"),

	OFFICERS("OFFICERS", "军官证"),

	OVERSEAS_CARD("OVERSEAS_CARD", "境外证件");

	private String value;

	private String desc;
}
