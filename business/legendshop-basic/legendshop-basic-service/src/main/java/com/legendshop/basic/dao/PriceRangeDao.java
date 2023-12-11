/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageParams;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.entity.PriceRange;

import java.util.List;

/**
 * 统计报表的价格范围.
 *
 * @author legendshop
 */
public interface PriceRangeDao extends GenericDao<PriceRange, Long> {

	List<PriceRange> queryPriceRange(Long shopId, Integer type);

	PageSupport<PriceRange> getPriceRange(PageParams pageParams, Long shopId, Integer type);

}
