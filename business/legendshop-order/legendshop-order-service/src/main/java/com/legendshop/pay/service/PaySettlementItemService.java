/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.query.PaySettlementQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface PaySettlementItemService {

	List<PaySettlementItemDTO> queryBySn(String sn);

	List<PaySettlementItemDTO> queryBySnList(List<String> snList);

	Long save(PaySettlementItemDTO paySettlementItemDTO);

	List<PaySettlementItemDTO> queryBySnListAndState(List<String> snList, Integer state);

	boolean savePaySettlementItem(PaySettlementItemDTO paySettlementItemDTO);

	boolean savePaySettlementItem(List<PaySettlementItemDTO> itemList);

	int update(PaySettlementItemDTO item);

	int update(List<PaySettlementItemDTO> itemList);


	/**
	 * 获取订单的支付方式
	 *
	 * @param orderNumber
	 * @return
	 */
	R<List<PaySettlementItemDTO>> queryPaidByOrderNumber(String orderNumber);

	PageSupport<PaySettlementItemDTO> querySettlementExceptionList(PaySettlementQuery query);
}
