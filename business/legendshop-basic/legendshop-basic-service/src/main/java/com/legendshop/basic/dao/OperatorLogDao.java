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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.entity.OperatorLog;
import com.legendshop.basic.query.OperatorLogQuery;

/**
 * 操作日志表(OperatorLog)表数据库访问层
 *
 * @author legendshop
 * @since 2023-08-29 14:13:56
 */
public interface OperatorLogDao extends GenericDao<OperatorLog, Long> {

	/**
	 * 查询操作日志
	 *
	 * @param operatorLogQuery
	 * @return
	 */
	PageSupport<OperatorLog> getOperatorLogPage(OperatorLogQuery operatorLogQuery);
}
