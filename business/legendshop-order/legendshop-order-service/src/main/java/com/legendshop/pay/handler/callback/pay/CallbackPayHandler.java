/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.callback.pay;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @author legendshop
 */
public interface CallbackPayHandler {

	/**
	 * 处理方法入口
	 */
	R<List<PaySettlementItemDTO>> handler(HttpServletRequest request, HttpServletResponse response);


	/**
	 * 支付回调
	 *
	 * @param paySettlementItemDTOS
	 */
	String callbackBusinessHandle(List<PaySettlementItemDTO> paySettlementItemDTOS);
}
