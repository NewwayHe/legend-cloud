/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.validator;

import java.util.LinkedList;

/**
 * 在FluentValidator内部调用使用的验证器链
 *
 * @author legendshop
 */
public class ValidatorElementList {
	/**
	 * 验证器链表
	 */
	private LinkedList<ValidatorElement> validatorElementLinkedList = new LinkedList<>();

	/**
	 * 将验证器加入链表
	 *
	 * @param element
	 */
	public void add(ValidatorElement element) {
		validatorElementLinkedList.add(element);
	}

	/**
	 * 获取验证器链表
	 *
	 * @return
	 */
	public LinkedList<ValidatorElement> getList() {
		return validatorElementLinkedList;
	}

	/**
	 * 验证器链表是否为空
	 *
	 * @return
	 */
	public boolean isEmpty() {
		return validatorElementLinkedList.isEmpty();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (ValidatorElement element : validatorElementLinkedList) {
			sb.append("[");
			sb.append(element.getValidator().getClass().getSimpleName());
			sb.append("]->");
		}
		return sb.toString();
	}
}
