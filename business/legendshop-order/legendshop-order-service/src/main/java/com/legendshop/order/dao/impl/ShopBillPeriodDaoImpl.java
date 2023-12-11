/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import cn.legendshop.jpaplus.util.StringUtils;
import com.legendshop.order.dao.ShopBillPeriodDao;
import com.legendshop.order.entity.ShopBillPeriod;
import com.legendshop.order.excel.ShopBillPeriodExportDTO;
import com.legendshop.order.query.ShopBillPeriodQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 结算档期Dao
 *
 * @author legendshop
 */
@Repository
public class ShopBillPeriodDaoImpl extends GenericDaoImpl<ShopBillPeriod, Long> implements ShopBillPeriodDao {

	@Override
	public PageSupport<ShopBillPeriod> getShopBillPeriod(ShopBillPeriodQuery shopBillPeriodQuery) {
		CriteriaQuery cq = new CriteriaQuery(ShopBillPeriod.class, shopBillPeriodQuery.getPageSize(), shopBillPeriodQuery.getCurPage());
		cq.like("flag", shopBillPeriodQuery.getFlag(), MatchMode.ANYWHERE);
		if (StringUtils.isNotBlank(shopBillPeriodQuery.getStartTime())) {
			cq.gt("recDate", DateUtil.beginOfDay(DateUtil.parse(shopBillPeriodQuery.getStartTime())));
			cq.lt("recDate", DateUtil.endOfDay(DateUtil.parse(shopBillPeriodQuery.getStartTime())));
		}
		cq.addDescOrder("recDate");
		return queryPage(cq);
	}

	@Override
	public List<ShopBillPeriodExportDTO> exportShopBillPeriod(ShopBillPeriodQuery query) {
		LambdaEntityCriterion<ShopBillPeriodExportDTO> criterion = new LambdaEntityCriterion<>(ShopBillPeriodExportDTO.class);
		criterion.like(ShopBillPeriodExportDTO::getFlag, query.getFlag(), MatchMode.ANYWHERE);
		if (StringUtils.isNotBlank(query.getStartTime())) {
			criterion
					.ge(ShopBillPeriodExportDTO::getRecDate, DateUtil.beginOfDay(DateUtil.parse(query.getStartTime())))
					.le(ShopBillPeriodExportDTO::getRecDate, DateUtil.endOfDay(DateUtil.parse(query.getStartTime())));
		}
		criterion.addDescOrder(ShopBillPeriodExportDTO::getRecDate);
		return queryDTOByProperties(criterion);
	}

}
