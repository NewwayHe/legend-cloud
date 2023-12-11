/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;


import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;

/**
 * 订单处理工具
 *
 * @author legendshop
 */
public interface OrderUtil {


	/**
	 * 根据收货地址，处理订单运费
	 *
	 * @param userId         用户ID
	 * @param confirmOrderBo 预订单BO
	 * @return R
	 */
	R<ConfirmOrderBO> handlerDelivery(Long userId, ConfirmOrderBO confirmOrderBo);


	/**
	 * 处理发票
	 *
	 * @param confirmOrderBo
	 * @return
	 */
	R<ConfirmOrderBO> handlerInvoice(ConfirmOrderBO confirmOrderBo);

}
