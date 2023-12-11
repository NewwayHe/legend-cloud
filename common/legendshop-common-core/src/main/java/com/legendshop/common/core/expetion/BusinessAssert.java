/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.expetion;

/**
 * 自定义断言接口
 *
 * @author legendshop
 */
public interface BusinessAssert {
	/**
	 * 创建异常
	 *
	 * @param args
	 * @return
	 */
	BaseException newException(Object... args);

	/**
	 * 创建异常
	 *
	 * @param t
	 * @param args
	 * @return
	 */
	BaseException newException(Throwable t, Object... args);

	/**
	 * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
	 *
	 * @param obj 待判断对象
	 */
	default void assertNotNull(Object obj) {
		if (obj == null) {
			throw newException(obj);
		}
	}

	/**
	 * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
	 * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
	 *
	 * @param obj  待判断对象
	 * @param args message占位符对应的参数列表
	 */
	default void assertNotNull(Object obj, Object... args) {
		if (obj == null) {
			throw newException(args);
		}
	}
}
