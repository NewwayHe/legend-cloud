/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.AllArgsConstructor;

/**
 * 系统配置参数
 *
 * @author legendshop
 */
@AllArgsConstructor
public enum SysParameterEnum {

	/**
	 * 是否发送短信.
	 */
	SEND_SMS(Boolean.class),


	;

	/**
	 * The clazz.
	 */
	private final Class<?> clazz;


}
