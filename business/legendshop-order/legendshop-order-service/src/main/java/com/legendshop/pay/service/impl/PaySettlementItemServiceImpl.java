/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dao.PaySettlementItemDao;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.query.PaySettlementQuery;
import com.legendshop.pay.service.PaySettlementItemService;
import com.legendshop.pay.service.convert.PaySettlementItemConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySettlementItemServiceImpl implements PaySettlementItemService {

	final PaySettlementItemDao paySettlementItemDao;

	final PaySettlementItemConverter paySettlementItemConverter;

	@Override
	public List<PaySettlementItemDTO> queryBySn(String sn) {
		return this.paySettlementItemConverter.to(this.paySettlementItemDao.queryByPaySettlementSn(sn));
	}

	@Override
	public List<PaySettlementItemDTO> queryBySnList(List<String> snList) {
		return this.paySettlementItemConverter.to(this.paySettlementItemDao.queryByPaySettlementSnList(snList));
	}

	@Override
	public List<PaySettlementItemDTO> queryBySnListAndState(List<String> snList, Integer state) {
		return this.paySettlementItemConverter.to(this.paySettlementItemDao.queryByPaySettlementSnListAndState(snList, state));
	}

	@Override
	public boolean savePaySettlementItem(PaySettlementItemDTO paySettlementItemDTO) {
		Long result = this.paySettlementItemDao.save(paySettlementItemConverter.from(paySettlementItemDTO));
		return result > 0;
	}

	@Override
	public Long save(PaySettlementItemDTO paySettlementItemDTO) {
		return this.paySettlementItemDao.save(paySettlementItemConverter.from(paySettlementItemDTO));
	}

	@Override
	public boolean savePaySettlementItem(List<PaySettlementItemDTO> itemList) {
		List<Long> result = this.paySettlementItemDao.save(paySettlementItemConverter.from(itemList));
		return result.size() == itemList.size();
	}

	@Override
	public int update(PaySettlementItemDTO item) {
		return this.paySettlementItemDao.update(this.paySettlementItemConverter.from(item));
	}

	@Override
	public int update(List<PaySettlementItemDTO> itemList) {
		return this.paySettlementItemDao.update(this.paySettlementItemConverter.from(itemList));
	}


	@Override
	public R<List<PaySettlementItemDTO>> queryPaidByOrderNumber(String orderNumber) {
		return R.ok(paySettlementItemDao.queryPaidByOrderNumber(orderNumber));
	}

	@Override
	public PageSupport<PaySettlementItemDTO> querySettlementExceptionList(PaySettlementQuery query) {
		return paySettlementItemDao.querySettlementExceptionList(query);
	}
}
