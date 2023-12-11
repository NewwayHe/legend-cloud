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
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.pay.dao.PayRefundSettlementDao;
import com.legendshop.pay.entity.PayRefundSettlement;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (PayRefundSettlement)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-05-12 18:09:16
 */
@Repository
public class PayRefundSettlementDaoImpl extends GenericDaoImpl<PayRefundSettlement, Long> implements PayRefundSettlementDao {

	@Override
	public List<PayRefundSettlement> queryByRefundSn(String refundSn) {
		return super.queryByProperties(new EntityCriterion().eq("refundSn", refundSn));
	}

	@Override
	public PayRefundSettlement queryByRefundSnAndType(String refundSn, String refundType) {
		return super.getByProperties(new EntityCriterion().eq("refundSn", refundSn).eq("payRefundType", refundType));
	}

	@Override
	public PayRefundSettlement getByExternalRefundSn(String externalRefundSn) {
		return super.getByProperties(new EntityCriterion().eq("externalRefundSn", externalRefundSn));
	}

	@Override
	public PayRefundSettlement getByPayRefundSn(String payRefundSn) {
		if (StrUtil.isEmpty(payRefundSn)) {
			return null;
		}
		return super.getByProperties(new EntityCriterion().eq("payRefundSn", payRefundSn));
	}
}
