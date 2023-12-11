/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.enums;

/**
 * 基础枚举类
 *
 * @param <E> 类型,String 或者 Integer
 * @author legendshop
 */
public interface BaseEnum<E> {

	/**
	 * 比较是否存在
	 *
	 * @param value
	 * @return
	 */
	boolean contains(E value);
}
