/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.refund;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.ConfirmRefundDTO;
import com.legendshop.order.entity.OrderRefundReturn;

/**
 * 平台确认退款策略
 *
 * @author legendshop
 */
public interface ConfirmRefundStrategy {

	/**
	 * 平台确认退款
	 *
	 * @param confirmRefundDTO
	 * @return
	 */
	R confirmRefund(ConfirmRefundDTO confirmRefundDTO);

	/**
	 * 退款处理
	 *
	 * @param orderRefundReturn
	 */
	R<Void> refundHandler(OrderRefundReturn orderRefundReturn);

	/**
	 * 退款成功回调处理
	 */
	R<Void> refundCallback(String refundSn);
}
