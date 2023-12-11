/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.expetion;


import com.legendshop.common.core.enums.BusinessResponseInterface;
import lombok.Getter;

/**
 * <p>基础异常类，所有自定义异常类都需要继承本类</p>
 *
 * @author legendshop
 */
@Getter
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 返回码
	 */
	protected BusinessResponseInterface businessResponseInterface;
	/**
	 * 异常消息参数
	 */
	protected Object[] args;

	public BaseException(BusinessResponseInterface businessResponseInterface) {
		super(businessResponseInterface.getMsg());
		this.businessResponseInterface = businessResponseInterface;
	}

	public BaseException(int code, String msg) {
		super(msg);
		this.businessResponseInterface = new BusinessResponseInterface() {
			@Override
			public int getCode() {
				return code;
			}

			@Override
			public String getMsg() {
				return msg;
			}
		};
	}

	public BaseException(BusinessResponseInterface businessResponseInterface, Object[] args, String message) {
		super(message);
		this.businessResponseInterface = businessResponseInterface;
		this.args = args;
	}

	public BaseException(BusinessResponseInterface businessResponseInterface, Object[] args, String message, Throwable cause) {
		super(message, cause);
		this.businessResponseInterface = businessResponseInterface;
		this.args = args;
	}

}
