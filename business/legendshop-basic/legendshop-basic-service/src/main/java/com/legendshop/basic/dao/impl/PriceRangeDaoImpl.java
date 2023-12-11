/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageParams;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.PriceRangeDao;
import com.legendshop.basic.entity.PriceRange;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 统计报表的价格范围.
 *
 * @author legendshop
 */
@Repository
public class PriceRangeDaoImpl extends GenericDaoImpl<PriceRange, Long> implements PriceRangeDao {


	@Override
	public List<PriceRange> queryPriceRange(Long shopId, Integer type) {
		return this.queryByProperties(new EntityCriterion().eq("shopId", shopId).eq("type", type));
	}

	@Override
	public PageSupport<PriceRange> getPriceRange(PageParams pageParams, Long shopId, Integer type) {
		CriteriaQuery cq = new CriteriaQuery(PriceRange.class, pageParams.getPageSize(), pageParams.getCurPage());
		cq.eq("shopId", shopId);
		cq.eq("type", type);
		return queryPage(cq);
	}

}
