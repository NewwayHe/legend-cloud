/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.submit;


import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;

/**
 * 提交订单策略
 *
 * @author legendshop
 */
public interface SubmitOrderStrategy {

	/**
	 * 提交订单
	 *
	 * @param confirmOrderBo 预订单BO
	 * @return R
	 */
	R<Object> submit(ConfirmOrderBO confirmOrderBo);


}
