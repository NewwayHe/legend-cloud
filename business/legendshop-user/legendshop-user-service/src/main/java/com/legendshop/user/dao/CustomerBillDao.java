/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.entity.CustomerBill;
import com.legendshop.user.query.CustomerBillQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户账单Dao.
 *
 * @author legendshop
 */
public interface CustomerBillDao extends GenericDao<CustomerBill, Long> {


	/**
	 * 客户账单分页查询
	 *
	 * @param expensesRecordQuery
	 * @return
	 */
	PageSupport<CustomerBill> queryPage(CustomerBillQuery expensesRecordQuery);


	/**
	 * 客户账单逻辑删除
	 *
	 * @param id
	 * @return
	 */
	int updateDelFlag(Long id);

	/**
	 * 获取关联业务单号相同的账单记录
	 *
	 * @param relatedBizOrderNo
	 * @return
	 */
	List<CustomerBill> getRelatedBizOrderList(String relatedBizOrderNo);

	/**
	 * 根据年月统计收入\支出
	 *
	 * @param month
	 * @param mode
	 * @param ownerId
	 * @return
	 */
	BigDecimal calculateAmountByMonthAndMode(String month, String mode, Long ownerId);

}
