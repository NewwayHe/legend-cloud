/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.legendshop.pay.dao.PaySettlementOrderDao;
import com.legendshop.pay.dto.PaySettlementOrderDTO;
import com.legendshop.pay.service.PaySettlementOrderService;
import com.legendshop.pay.service.convert.PaySettlementOrderConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class PaySettlementOrderServiceImpl implements PaySettlementOrderService {

	final PaySettlementOrderDao paySettlementOrderDao;

	final PaySettlementOrderConverter paySettlementOrderConverter;


	@Override
	public boolean batchSavePaySettlementOrder(List<PaySettlementOrderDTO> paySettlementOrderList) {
		List<Long> result = this.paySettlementOrderDao.save(this.paySettlementOrderConverter.from(paySettlementOrderList));
		return !CollectionUtil.isEmpty(result);
	}

	@Override
	public List<PaySettlementOrderDTO> queryOrderBySn(String settlementSn) {
		return this.paySettlementOrderConverter.to(this.paySettlementOrderDao.queryOrderBySn(settlementSn));
	}

	@Override
	public List<PaySettlementOrderDTO> querySnByOrderNumber(String orderNumber) {
		return this.paySettlementOrderConverter.to(this.paySettlementOrderDao.querySnByOrderNumber(orderNumber));
	}

	@Override
	public List<PaySettlementOrderDTO> querySnByOrderNumber(List<String> orderNumber) {
		return this.paySettlementOrderConverter.to(this.paySettlementOrderDao.querySnByOrderNumber(orderNumber));
	}
}
