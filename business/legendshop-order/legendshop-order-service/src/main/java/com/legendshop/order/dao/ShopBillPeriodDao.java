/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.order.entity.ShopBillPeriod;
import com.legendshop.order.excel.ShopBillPeriodExportDTO;
import com.legendshop.order.query.ShopBillPeriodQuery;

import java.util.List;

/**
 * 结算档期Dao
 *
 * @author legendshop
 */
public interface ShopBillPeriodDao extends GenericDao<ShopBillPeriod, Long> {

	PageSupport<ShopBillPeriod> getShopBillPeriod(ShopBillPeriodQuery shopBillPeriodQuery);

	/**
	 * 结算档期列表导出
	 *
	 * @param shopBillPeriodQuery
	 * @return
	 */
	List<ShopBillPeriodExportDTO> exportShopBillPeriod(ShopBillPeriodQuery shopBillPeriodQuery);
}
