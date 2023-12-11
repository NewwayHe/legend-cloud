/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.validator;

import com.legendshop.common.validator.exception.ValidateException;
import lombok.extern.slf4j.Slf4j;

/**
 * 链式调用验证器实现
 *
 * @author legendshop
 */
@Slf4j
public class FluentValidator {
	/**
	 * 验证器链，惰性求值期间就是不断的改变这个链表，及时求值期间就是遍历链表依次执行验证
	 */
	private final ValidatorElementList validatorElementList = new ValidatorElementList();

	/**
	 * 验证器上下文
	 */
	private ValidatorContext context = new ValidatorContext();

	/**
	 * 私有构造方法,只能通过checkAll创建对象
	 */
	private FluentValidator() {
	}

	/**
	 * 创建FluentValidator对象
	 *
	 * @return
	 */
	public static FluentValidator checkAll() {
		return new FluentValidator();
	}

	/**
	 * 使用验证器进行验证
	 *
	 * @param businessValidator 验证器
	 * @return
	 */
	public <T> FluentValidator on(BusinessValidator<T> businessValidator) {
		validatorElementList.add(new ValidatorElement(null, businessValidator));
		return this;
	}

	/**
	 * 使用验证器验证指定对象
	 *
	 * @param t                 待验证对象
	 * @param businessValidator 验证器
	 * @return
	 */
	public <T> FluentValidator on(T t, BusinessValidator<T> businessValidator) {
		validatorElementList.add(new ValidatorElement(t, businessValidator));
		return this;
	}

	/**
	 * 使用验证器验证指定对象
	 *
	 * @param t                 待验证对象
	 * @param businessValidator 验证器
	 * @param condition         条件，为true时才会将验证器加入验证器列表中
	 * @return
	 */
	public <T> FluentValidator on(T t, BusinessValidator<T> businessValidator, boolean condition) {
		if (condition) {
			validatorElementList.add(new ValidatorElement(t, businessValidator));
		}
		return this;
	}


	/**
	 * 执行各个验证器中的验证逻辑
	 *
	 * @return
	 */
	public FluentValidator exec() {
		if (validatorElementList.isEmpty()) {
			log.info("Nothing to validate");
			return null;
		}
		long start = System.currentTimeMillis();
		log.info("Start to validate,validatorElementList={}", validatorElementList.toString());
		String validatorName;
		try {
			for (ValidatorElement element : validatorElementList.getList()) {
				Object target = element.getTarget();
				BusinessValidator businessValidator = element.getValidator();
				validatorName = businessValidator.getClass().getSimpleName();
				log.info("{} is running", validatorName);
				businessValidator.validate(context, target);
			}
		} catch (ValidateException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			log.info("End to validate,time consuming {} ms", (System.currentTimeMillis() - start));
		}
		return this;
	}

	/**
	 * 将键值对放入上下文
	 *
	 * @param key   键
	 * @param value 值
	 * @return FluentValidator
	 */
	public FluentValidator putAttribute2Context(String key, Object value) {
		if (context == null) {
			context = new ValidatorContext();
		}
		context.setAttribute(key, value);
		return this;
	}

	/**
	 * 获取验证器上下文
	 *
	 * @return
	 */
	public ValidatorContext getContext() {
		return context;
	}
}
