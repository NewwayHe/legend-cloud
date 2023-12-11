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
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.PreSellOrderBillDTO;
import com.legendshop.order.dto.PreSellOrderDTO;

import java.util.Date;
import java.util.List;

/**
 * 预售订单服务
 *
 * @author legendshop
 */
public interface PreSellOrderService {

	/**
	 * 账单结算
	 * 查询当前期 所有已支付定金未支付尾款但尾款支付时间已过且已关闭的订单
	 *
	 * @param endDate     the 尾款支付结束时间
	 * @param orderStatus the 订单状态
	 * @return list
	 */
	List<PreSellOrderBillDTO> getBillUnPayFinalPreSellOrder(Date endDate, Integer orderStatus);


	/**
	 * 根据基础订单信息订单保存预售订单
	 */
	Long savePreSellOrder(OrderDTO order, Long preSellProductId);

	int update(List<PreSellOrderDTO> preSellOrderList);

	int update(PreSellOrderDTO preSellOrderDTO);


	List<PreSellOrderDTO> queryByOrderIds(List<Long> orderIds);


	/**
	 * 预售尾款未支付处理
	 */
	R<Void> balanceHandler();

	int cancelOrderByIds(List<Long> orderIds);

	int cancelPreSellOrderList(List<PreSellOrderDTO> orderList);

	R<Void> shopActiveCancelPreSellOrder(Long shopId, Long orderId, String reason);

	PreSellOrderDTO getById(Long id);

	PreSellOrderDTO getByOrderId(Long id);
}
