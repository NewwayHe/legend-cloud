/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.validator;

/**
 * 验证器接口, 泛型T表示待验证对象的类型
 *
 * @param <T>
 * @author legendshop
 */
public interface BusinessValidator<T> {
	/**
	 * 执行验证,如果验证失败,则抛出ValidateException异常
	 *
	 * @param context 验证上下文
	 * @param t       待验证对象
	 */
	void validate(ValidatorContext context, T t);
}
