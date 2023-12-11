/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.search.entity.SearchRebuildIndexLog;

/**
 * 搜索重建索引日志(SearchRebuildIndexLog)表数据库访问层
 *
 * @author legendshop
 * @since 2022-02-18 10:50:55
 */
public interface SearchRebuildIndexLogDao extends GenericDao<SearchRebuildIndexLog, Long> {

	/**
	 * 更新重建索引记录状态
	 *
	 * @param id
	 * @param status
	 * @param remark
	 * @return
	 */
	int updateState(Long id, Integer status, String remark);

}
