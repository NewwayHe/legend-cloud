/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.basic.dao.AmqpTaskDao;
import com.legendshop.basic.entity.AmqpTask;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 队列任务表(AmqpTask)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-04-29 14:16:53
 */
@Repository
public class AmqpTaskDaoImpl extends GenericDaoImpl<AmqpTask, Long> implements AmqpTaskDao {

	@Override
	public List<AmqpTask> queryExecutable() {
		LambdaEntityCriterion<AmqpTask> criterion = new LambdaEntityCriterion<>(AmqpTask.class);
		criterion.eq(AmqpTask::getStatus, 0);
		criterion.le(AmqpTask::getDelayTime, DateUtil.endOfDay(DateUtil.date()));
		return queryByProperties(criterion);
	}
}
