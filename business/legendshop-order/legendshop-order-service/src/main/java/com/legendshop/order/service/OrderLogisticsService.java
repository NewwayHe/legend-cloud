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
import com.legendshop.order.bo.OrderBO;
import com.legendshop.order.bo.OrderRefundReturnBO;
import com.legendshop.order.dto.OrderLogisticsDTO;
import com.legendshop.order.dto.RefundReturnLogisticsDTO;

/**
 * 订单物流
 *
 * @author legendshop
 */
public interface OrderLogisticsService {

	/**
	 * 根据订单ID获取物流信息
	 *
	 * @return
	 */
	OrderLogisticsDTO getByOrderId(Long orderId);


	/**
	 * 根据订单号调用快递100查询物流信息
	 *
	 * @param orderNumber
	 * @return
	 */
	R<OrderLogisticsDTO> queryLogisticsInformation(String orderNumber);


	/**
	 * 根据订单号调用快递100查询物流信息
	 *
	 * @param orderBO
	 * @return
	 */
	R<OrderLogisticsDTO> queryLogisticsInformation(OrderBO orderBO);


	/**
	 * 根据订单号调用快递100查询退货物流信息
	 *
	 * @param orderRefundReturnBO
	 * @return
	 */
	R<RefundReturnLogisticsDTO> queryRefundReturnLogisticsInformation(OrderRefundReturnBO orderRefundReturnBO);


	/**
	 * 更新
	 *
	 * @param orderLogisticsDTO
	 */
	void update(OrderLogisticsDTO orderLogisticsDTO);

	Long save(OrderLogisticsDTO data);


}
