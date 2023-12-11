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
 * 验证器包装类
 *
 * @author legendshop
 */
public class ValidatorElement {
	/**
	 * 待验证对象
	 */
	private Object target;

	/**
	 * 验证器
	 */
	private BusinessValidator businessValidator;

	public ValidatorElement(Object target, BusinessValidator businessValidator) {
		this.target = target;
		this.businessValidator = businessValidator;
	}

	public Object getTarget() {
		return target;
	}

	public BusinessValidator getValidator() {
		return businessValidator;
	}
}
