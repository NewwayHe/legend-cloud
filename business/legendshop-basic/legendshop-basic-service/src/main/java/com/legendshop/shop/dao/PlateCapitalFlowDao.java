/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.entity.PlateCapitalFlow;
import com.legendshop.shop.query.PlateCapitalFlowQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * 平台资金流水(PlateCapitalFlow)表数据库访问层
 *
 * @author legendshop
 * @since 2020-09-18 17:26:09
 */
public interface PlateCapitalFlowDao extends GenericDao<PlateCapitalFlow, Long> {

	/**
	 * 平台资金流水列表
	 *
	 * @param plateCapitalFlowQuery
	 * @return
	 */
	PageSupport<PlateCapitalFlow> queryPage(PlateCapitalFlowQuery plateCapitalFlowQuery);


	/**
	 * 平台资金流水列表
	 *
	 * @param plateCapitalFlowQuery
	 * @return
	 */
	List<PlateCapitalFlow> queryList(PlateCapitalFlowQuery plateCapitalFlowQuery);

	/**
	 * @param flowType
	 * @return
	 */
	BigDecimal getSumAmount(String flowType);


}
