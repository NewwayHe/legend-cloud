/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.dao.CustomerBillDao;
import com.legendshop.user.entity.CustomerBill;
import com.legendshop.user.query.CustomerBillQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户账单DaoImpl.
 *
 * @author legendshop
 */
@Repository("expensesRecordDao")
public class CustomerBillDaoImpl extends GenericDaoImpl<CustomerBill, Long> implements CustomerBillDao {


	@Override
	public PageSupport<CustomerBill> queryPage(CustomerBillQuery customerBillQuery) {
		CriteriaQuery cq = new CriteriaQuery(CustomerBill.class, customerBillQuery.getPageSize(), customerBillQuery.getCurPage());
		if (null != customerBillQuery.getOwnerId()) {
			cq.eq("ownerId", customerBillQuery.getOwnerId());
			cq.eq("delFlag", false);
		}
		cq.like("tradeExplain", customerBillQuery.getTradeExplain(), MatchMode.ANYWHERE);
		cq.eq("mode", customerBillQuery.getMode());
		cq.eq("type", customerBillQuery.getType());
		cq.like("payTypeId", customerBillQuery.getPayTypeId(), MatchMode.ANYWHERE);
		if (null != customerBillQuery.getStartDate() && null != customerBillQuery.getEndDate()) {
			cq.between("createTime", DateUtil.beginOfDay(customerBillQuery.getStartDate()), DateUtil.endOfDay(customerBillQuery.getEndDate()));
		}
		cq.addDescOrder("createTime");
		return this.queryPage(cq);
	}

	@Override
	public int updateDelFlag(Long id) {
		return update("UPDATE ls_customer_bill SET del_flag=1 WHERE id=?", id);
	}

	@Override
	public List<CustomerBill> getRelatedBizOrderList(String relatedBizOrderNo) {
		return queryByProperties(new EntityCriterion().eq("relatedBizOrderNo", relatedBizOrderNo));
	}

	@Override
	public BigDecimal calculateAmountByMonthAndMode(String month, String mode, Long ownerId) {
		String sql = "SELECT SUM(c.amount) FROM ls_customer_bill c WHERE DATE_FORMAT(c.create_time, '%Y-%m') = ? AND c.mode = ? AND c.owner_id = ?";
		return get(sql, BigDecimal.class, month, mode, ownerId);
	}

}
