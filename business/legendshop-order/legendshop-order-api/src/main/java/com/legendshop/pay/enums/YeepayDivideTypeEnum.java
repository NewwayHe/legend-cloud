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
 * 分账类型
 *
 * @author legendshop
 * @create: 2021/4/14 18:04
 */
@Getter
@AllArgsConstructor
public enum YeepayDivideTypeEnum {

	/**
	 *
	 */
	APPLY("apply", "申请分账"),

	COMPLETE("complete", "完结分账");

	final String value;
	final String desc;
}
