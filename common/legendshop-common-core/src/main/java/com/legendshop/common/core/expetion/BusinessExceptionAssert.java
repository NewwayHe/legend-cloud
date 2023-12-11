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

import java.text.MessageFormat;

/**
 * 业务异常断言
 *
 * @author legendshop
 */
public interface BusinessExceptionAssert extends BusinessResponseInterface, BusinessAssert {

	@Override
	default BaseException newException(Object... args) {
		String msg = MessageFormat.format(this.getMsg(), args);
		return new BusinessException(this, args, msg);
	}

	@Override
	default BaseException newException(Throwable t, Object... args) {
		String msg = MessageFormat.format(this.getMsg(), args);
		return new BusinessException(this, args, msg, t);
	}

}
