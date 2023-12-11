/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.pay.dao.PaySettlementOrderDao;
import com.legendshop.pay.entity.PaySettlementOrder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class PaySettlementOrderDaoImpl extends GenericDaoImpl<PaySettlementOrder, Long> implements PaySettlementOrderDao {


	@Override
	public List<PaySettlementOrder> queryOrderBySn(String settlementSn) {
		if (StrUtil.isBlank(settlementSn)) {
			return Collections.emptyList();
		}
		return super.queryByProperties(new EntityCriterion().eq("paySettlementSn", settlementSn));
	}

	@Override
	public List<PaySettlementOrder> querySnByOrderNumber(String orderNumber) {
		return super.queryByProperties(new EntityCriterion().eq("orderNumber", orderNumber));
	}

	@Override
	public List<PaySettlementOrder> querySnByOrderNumber(List<String> orderNumber) {
		if (CollUtil.isEmpty(orderNumber)) {
			return Collections.emptyList();
		}

		return super.queryByProperties(new EntityCriterion().in("orderNumber", orderNumber));
	}
}
