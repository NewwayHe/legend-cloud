/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.expetion;


import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.enums.BusinessResponseInterface;

/**
 * 业务异常
 *
 * @author legendshop
 */
public class BusinessException extends BaseException {

	private static final long serialVersionUID = 1L;

	public BusinessException(BusinessResponseInterface businessResponseInterface, Object[] args, String msg) {
		super(businessResponseInterface, args, msg);
	}

	public BusinessException(BusinessResponseInterface businessResponseInterface, Object[] args, String msg, Throwable cause) {
		super(businessResponseInterface, args, msg, cause);
	}

	/**
	 * 业务异常
	 *
	 * @param code 状态码
	 * @param msg  描述
	 */
	public BusinessException(int code, String msg) {
		super(code, msg);
	}

	/**
	 * 业务异常
	 *
	 * @param msg 描述
	 */
	public BusinessException(String msg) {
		super(CommonConstants.FAIL, msg);
	}
}
