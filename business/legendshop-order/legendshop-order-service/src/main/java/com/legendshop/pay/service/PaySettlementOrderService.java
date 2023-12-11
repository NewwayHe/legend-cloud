/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import com.legendshop.pay.dto.PaySettlementOrderDTO;

import java.util.List;

/**
 * @author legendshop
 */
public interface PaySettlementOrderService {


	boolean batchSavePaySettlementOrder(List<PaySettlementOrderDTO> paySettlementOrderList);

	/**
	 * 根据支付单号查询订单项
	 */
	List<PaySettlementOrderDTO> queryOrderBySn(String settlementSn);

	/**
	 * 根据订单号查询对应支付单编号
	 */
	List<PaySettlementOrderDTO> querySnByOrderNumber(String orderNumber);

	/**
	 * 根据订单号查询对应支付单编号
	 */
	List<PaySettlementOrderDTO> querySnByOrderNumber(List<String> orderNumber);
}
