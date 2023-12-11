/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.entity.AccusationType;
import com.legendshop.product.query.AccusationTypeQuery;

import java.util.List;

/**
 * 举报类型Dao
 *
 * @author legendshop
 */
public interface AccusationTypeDao extends GenericDao<AccusationType, Long> {

	PageSupport<AccusationType> queryPage(AccusationTypeQuery query);

	List<AccusationType> queryAllOnLine();

	int batchUpdateStatus(List<Long> ids, Integer status);
}
