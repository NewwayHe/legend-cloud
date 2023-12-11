/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.search.dao.SearchRebuildIndexLogDao;
import com.legendshop.search.entity.SearchRebuildIndexLog;
import org.springframework.stereotype.Repository;

/**
 * 搜索重建索引日志(SearchRebuildIndexLog)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-02-18 10:50:56
 */
@Repository
public class SearchRebuildIndexLogDaoImpl extends GenericDaoImpl<SearchRebuildIndexLog, Long> implements SearchRebuildIndexLogDao {

	@Override
	public int updateState(Long id, Integer status, String remark) {
		return super.update("UPDATE ls_search_rebuild_index_log SET status = ? , update_time = NOW(), remark = ? WHERE id = ?", status, remark, id);
	}
}
