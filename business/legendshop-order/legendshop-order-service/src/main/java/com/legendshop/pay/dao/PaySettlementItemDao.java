/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.entity.PaySettlementItem;
import com.legendshop.pay.query.PaySettlementQuery;

import java.util.List;

/**
 * 支付单据dao
 *
 * @author legendshop
 */
public interface PaySettlementItemDao extends GenericDao<PaySettlementItem, Long> {

	/**
	 * 获取所有的支付单据项
	 *
	 * @param paySettlementSn
	 * @return
	 */
	List<PaySettlementItem> queryByPaySettlementSn(String paySettlementSn);

	List<PaySettlementItem> queryByPaySettlementSnList(List<String> paySettlementSnList);

	List<PaySettlementItem> queryByPaySettlementSnListAndState(List<String> paySettlementSnList, Integer state);

	/**
	 * 获取订单支付方式
	 *
	 * @param orderNumber
	 * @return
	 */
	List<PaySettlementItemDTO> queryPaidByOrderNumber(String orderNumber);

	/**
	 * 查询异常的支付单列表
	 *
	 * @param query
	 * @return
	 */
	PageSupport<PaySettlementItemDTO> querySettlementExceptionList(PaySettlementQuery query);
}
