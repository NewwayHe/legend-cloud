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
import com.legendshop.basic.dao.OperatorLogDao;
import com.legendshop.basic.entity.OperatorLog;
import com.legendshop.basic.query.OperatorLogQuery;
import org.springframework.stereotype.Repository;

/**
 * 操作日志表(OperatorLog)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2023-08-29 14:13:56
 */
@Repository
public class OperatorLogDaoImpl extends GenericDaoImpl<OperatorLog, Long> implements OperatorLogDao {

	@Override
	public PageSupport<OperatorLog> getOperatorLogPage(OperatorLogQuery operatorLogQuery) {
		CriteriaQuery cq = new CriteriaQuery(OperatorLog.class, operatorLogQuery, true);
		cq.eq("userId", operatorLogQuery.getUserId());
		cq.eq("userType", operatorLogQuery.getUserType());
		cq.eq("eventType", operatorLogQuery.getEventType());
		cq.like("title", operatorLogQuery.getUserName(), MatchMode.ANYWHERE);
		cq.addDescOrder("id");
		return queryPage(cq);
	}
}
