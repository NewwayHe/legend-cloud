/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.data.entity.SearchHistoryEntity;

/**
 * @author legendshop
 */
public interface SearchDataDao extends GenericDao<SearchHistoryEntity, Long> {

	/**
	 * 保存搜索数据
	 *
	 * @param searchHistoryEntity
	 */
	void saveSearchLog(SearchHistoryEntity searchHistoryEntity);
}
