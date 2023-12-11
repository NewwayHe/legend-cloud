/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.withdraw;

import com.legendshop.common.core.constant.R;

/**
 * 提现处理接口
 *
 * @author legendshop
 */
public interface WithdrawHandler {

	/**
	 * 提现处理接口
	 *
	 * @param withdrawSerialNo 对应的提现流水号
	 * @return
	 */
	R<Void> handler(Long withdrawSerialNo);

}
