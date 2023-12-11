/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.data.dao.SearchDataDao;
import com.legendshop.data.entity.SearchHistoryEntity;
import org.springframework.stereotype.Repository;

/**
 * @author legendshop
 */
@Repository
public class SearchDataDaoImpl extends GenericDaoImpl<SearchHistoryEntity, Long> implements SearchDataDao {


	@Override
	public void saveSearchLog(SearchHistoryEntity searchHistoryEntity) {
		save(searchHistoryEntity);
	}
}
