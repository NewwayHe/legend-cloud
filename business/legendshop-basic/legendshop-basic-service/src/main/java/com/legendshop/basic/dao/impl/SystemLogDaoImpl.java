/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.SystemLogDao;
import com.legendshop.basic.entity.SystemLog;
import com.legendshop.basic.query.SystemLogQuery;
import org.springframework.stereotype.Repository;

/**
 * @author legendshop
 */
@Repository
public class SystemLogDaoImpl extends GenericDaoImpl<SystemLog, Long> implements SystemLogDao {

	@Override
	public PageSupport<SystemLog> page(SystemLogQuery systemLogQuery) {
		CriteriaQuery cq = new CriteriaQuery(SystemLog.class, systemLogQuery.getPageSize(), systemLogQuery.getCurPage());
		cq.like("title", systemLogQuery.getTitle(), MatchMode.ANYWHERE);
		cq.addDescOrder("createTime");
		return queryPage(cq);
	}
}
