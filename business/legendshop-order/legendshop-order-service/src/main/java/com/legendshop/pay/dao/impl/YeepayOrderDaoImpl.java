/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.pay.dao.YeepayOrderDao;
import com.legendshop.pay.entity.YeepayOrder;
import org.springframework.stereotype.Repository;

/**
 * 易宝支付订单信息(YeepayOrder)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-03-26 15:59:27
 */
@Repository
public class YeepayOrderDaoImpl extends GenericDaoImpl<YeepayOrder, Long> implements YeepayOrderDao {

	@Override
	public YeepayOrder getByOrderNumber(String orderNumber) {
		if (null == orderNumber) {
			return null;
		}

		LambdaEntityCriterion<YeepayOrder> criterion = new LambdaEntityCriterion<>(YeepayOrder.class);
		criterion.eq(YeepayOrder::getOrderNumber, orderNumber);
		return getByProperties(criterion);
	}

	@Override
	public YeepayOrder getByPaySettlementSn(String paySettlementSn) {
		if (StrUtil.isBlank(paySettlementSn)) {
			return null;
		}

		LambdaEntityCriterion<YeepayOrder> criterion = new LambdaEntityCriterion<>(YeepayOrder.class);
		criterion.eq(YeepayOrder::getPaySettlementSn, paySettlementSn);
		return getByProperties(criterion);
	}
}
