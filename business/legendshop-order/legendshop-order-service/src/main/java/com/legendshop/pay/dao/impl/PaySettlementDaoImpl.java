/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.QueryMap;
import com.legendshop.pay.dao.PaySettlementDao;
import com.legendshop.pay.entity.PaySettlement;
import com.legendshop.pay.enums.PaySettlementStateEnum;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单结算票据中心
 *
 * @author legendshop
 */
@Repository
public class PaySettlementDaoImpl extends GenericDaoImpl<PaySettlement, Long> implements PaySettlementDao {

	@Override
	public List<PaySettlement> queryPaidBySnList(List<String> snList) {
		if (CollectionUtils.isEmpty(snList)) {
			return new ArrayList<>();
		}
		return super.queryByProperties(new EntityCriterion().in("paySettlementSn", snList.toArray()).eq("state", PaySettlementStateEnum.PAID.getCode()));
	}

	@Override
	public PaySettlement getBySn(String settlementSn) {
		return super.getByProperties(new EntityCriterion().eq("paySettlementSn", settlementSn));
	}

	@Override
	public long checkRepeatPay(String orderNumber, String settlementType, Long userId) {

		String sql = "SELECT COUNT(1) FROM ls_pay_settlement lps LEFT JOIN ls_pay_settlement_order lpso ON lps.pay_settlement_sn=lpso.pay_settlement_sn"
				+ " WHERE state=1 AND lps.use_type=? AND lpso.order_number=? AND lpso.user_id=? ";
		return getLongResult(sql, settlementType, orderNumber, userId);
	}

	@Override
	public List<PaySettlement> queryPaySuccessfulButOrderUnPaid() {
		String sql = "SELECT lp.* FROM ls_order lo LEFT JOIN ls_pay_settlement_order lpo ON lo.order_number = lpo.order_number LEFT JOIN ls_pay_settlement lp ON lpo.pay_settlement_sn = lp.pay_settlement_sn WHERE lo.status = 1 AND lp.state = 1 LIMIT 100";
		return query(sql, PaySettlement.class);
	}

	@Override
	public PaySettlement getPaidByOrderNumber(String orderNumber) {
		String sql = "SELECT lp.* FROM ls_order lo LEFT JOIN ls_pay_settlement_order lpo ON lo.order_number = lpo.order_number LEFT JOIN ls_pay_settlement lp ON lpo.pay_settlement_sn = lp.pay_settlement_sn WHERE lp.state = 1 AND lo.order_number = ?";
		return get(sql, PaySettlement.class, orderNumber);
	}

	@Override
	public List<PaySettlement> getPaidByOrderNumberList(List<String> orderNumbers) {
		QueryMap map = new QueryMap();
		map.in("orderNumbers", orderNumbers);
		SQLOperation operation = this.getSQLAndParams("PaySettlement.getPaidByOrderNumberList", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(PaySettlement.class));
	}
}
