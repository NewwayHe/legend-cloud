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
import com.legendshop.basic.entity.SystemLog;
import com.legendshop.basic.query.SystemLogQuery;

/**
 * @author legendshop
 */
public interface SystemLogDao extends GenericDao<SystemLog, Long> {

	PageSupport<SystemLog> page(SystemLogQuery systemLogQuery);
}
