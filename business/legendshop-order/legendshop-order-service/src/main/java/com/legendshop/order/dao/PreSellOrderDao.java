/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.order.dto.PreSellOrderBillDTO;
import com.legendshop.order.dto.ShopOrderBillPreSellDTO;
import com.legendshop.order.entity.PreSellOrder;
import com.legendshop.order.excel.ShopOrderBillPreSellExportDTO;
import com.legendshop.order.query.ShopOrderBillOrderQuery;

import java.util.Date;
import java.util.List;

/**
 * 预售订单dao
 *
 * @author legendshop
 */
public interface PreSellOrderDao extends GenericDao<PreSellOrder, Long> {

	/**
	 * 删除
	 */
	@Override
	int delete(PreSellOrder preSellOrder);

	/**
	 * 保存
	 */
	@Override
	Long save(PreSellOrder preSellOrder);

	/**
	 * 更新
	 */
	@Override
	int update(PreSellOrder PreSellOrder);


	List<PreSellOrder> queryByOrderIds(List<Long> orderIds);

	List<PreSellOrder> queryUnpaidBalanceOrderByDate(Date date);

	/**
	 * 查询当前期 所有已支付定金未支付尾款但尾款支付时间已过且已关闭的订单
	 *
	 * @param endDate
	 * @param orderStatus
	 * @return
	 */
	List<PreSellOrderBillDTO> getBillUnPayFinalPreSellOrder(Date endDate, Integer orderStatus);


	PreSellOrder getByOrderId(Long orderId);

	/**
	 * 账单结算预售订单列表
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	PageSupport<ShopOrderBillPreSellDTO> getShopOrderBillPreSellPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	/**
	 * 账单结算预售订单列表导出
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	List<ShopOrderBillPreSellExportDTO> exportShopOrderBillPreSellPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);
}
