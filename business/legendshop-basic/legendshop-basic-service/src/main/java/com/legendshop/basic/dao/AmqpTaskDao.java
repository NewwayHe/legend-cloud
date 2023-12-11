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
import com.legendshop.basic.entity.AmqpTask;

import java.util.List;

/**
 * 队列任务表(AmqpTask)表数据库访问层
 *
 * @author legendshop
 * @since 2022-04-29 14:16:53
 */
public interface AmqpTaskDao extends GenericDao<AmqpTask, Long> {

	/**
	 * 查询可执行的队列
	 *
	 * @return
	 */
	List<AmqpTask> queryExecutable();
}
