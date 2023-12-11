/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.expetion;

import com.legendshop.common.core.enums.BusinessResponseInterface;
import com.legendshop.common.core.expetion.BusinessException;

/**
 * 没有微信授权业务异常
 *
 * @author legendshop
 * @create: 2022-01-04 15:06
 */
public class NotWeChatAuthorizeBusinessException extends BusinessException {

	private static final Integer DEFAULT_CODE = -1000;

	private static final String DEFAULT_MESSAGE = "没有微信授权";

	private static final long serialVersionUID = 1L;

	public NotWeChatAuthorizeBusinessException(String msg) {
		super(DEFAULT_CODE, msg);
	}

	public NotWeChatAuthorizeBusinessException(int code, String msg) {
		super(DEFAULT_CODE, msg);
	}

	public NotWeChatAuthorizeBusinessException(BusinessResponseInterface businessResponseInterface, Object[] args, String msg, Throwable cause) {
		super(businessResponseInterface, args, msg, cause);
	}

	public NotWeChatAuthorizeBusinessException(BusinessResponseInterface businessResponseInterface, Object[] args, String msg) {
		super(businessResponseInterface, args, msg);
	}

	@Override
	public BusinessResponseInterface getBusinessResponseInterface() {
		return new BusinessResponseInterface() {
			@Override
			public int getCode() {
				return DEFAULT_CODE;
			}

			@Override
			public String getMsg() {
				return "没有微信授权";
			}
		};
	}
}
