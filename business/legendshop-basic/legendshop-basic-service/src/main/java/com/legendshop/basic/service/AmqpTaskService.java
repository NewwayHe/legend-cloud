/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.common.core.constant.R;

import java.util.List;

/**
 * 队列任务表(AmqpTask)表服务接口
 *
 * @author legendshop
 * @since 2022-04-29 14:16:53
 */
public interface AmqpTaskService {

	/**
	 * 保存并发送mq
	 *
	 * @param amqpTaskDTO
	 * @return
	 */
	R<Void> convertAndSend(AmqpTaskDTO amqpTaskDTO);

	/**
	 * 查询可执行列表
	 *
	 * @return
	 */
	List<AmqpTaskDTO> queryExecutable();

	/**
	 * 更新
	 *
	 * @param amqpTasks
	 */
	void updateList(List<AmqpTaskDTO> amqpTasks);
}
