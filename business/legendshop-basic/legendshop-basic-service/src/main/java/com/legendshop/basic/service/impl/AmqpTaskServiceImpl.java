/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.legendshop.basic.dao.AmqpTaskDao;
import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.basic.entity.AmqpTask;
import com.legendshop.basic.service.AmqpTaskService;
import com.legendshop.basic.service.convert.AmqpTaskConverter;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 队列任务表(AmqpTask)表服务实现类
 *
 * @author legendshop
 * @since 2022-04-29 14:16:53
 */
@Slf4j
@Service
public class AmqpTaskServiceImpl implements AmqpTaskService {

	@Autowired
	private AmqpTaskDao amqpTaskDao;

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;

	@Autowired
	private AmqpTaskConverter amqpTaskConverter;

	@Override
	public R<Void> convertAndSend(AmqpTaskDTO dto) {

		log.info("convertAndSend 开始保存延时队列任务，交换机：{}，路由key：{}，时间：{}，数据：{}", dto.getExchange(), dto.getRoutingKey(), dto.getDelayTime(), dto.getMessage());

		AmqpTask amqpTask = amqpTaskConverter.from(dto);

		// 如果是当日的，则直接发mq
		if (DateUtil.endOfDay(DateUtil.date()).isAfter(dto.getDelayTime())) {
			log.info("convertAndSend 当前任务延时时间在当日，直接发送延时队列~");
			amqpTask.setStatus(1);
			long between = DateUtil.between(DateUtil.date(), amqpTask.getDelayTime(), DateUnit.SECOND, false);
			amqpSendMsgUtil.convertAndSend(amqpTask.getExchange(), amqpTask.getRoutingKey(), amqpTask.getMessage(), between > 0 ? between + 3L : 3L, ChronoUnit.SECONDS);
		} else {
			amqpTask.setStatus(0);
		}

		amqpTask.setCreateTime(DateUtil.date());
		amqpTask.setUpdateTime(DateUtil.date());
		amqpTaskDao.save(amqpTask);
		return R.ok();
	}

	@Override
	public List<AmqpTaskDTO> queryExecutable() {
		List<AmqpTask> amqpTasks = amqpTaskDao.queryExecutable();
		return amqpTaskConverter.to(amqpTasks);
	}

	@Override
	public void updateList(List<AmqpTaskDTO> amqpTasks) {

		List<AmqpTask> amqpTaskList = amqpTaskConverter.from(amqpTasks);
		amqpTaskDao.update(amqpTaskList);
	}
}
