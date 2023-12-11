/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.task.job.basic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.legendshop.basic.api.AmqpTaskApi;
import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 延时队列补偿定时任务
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AmqpTaskJob {

	private final AmqpTaskApi amqpTaskApi;
	private final AmqpSendMsgUtil amqpSendMsgUtil;

	@XxlJob("execAmqpTask")
	public ReturnT<String> execAmqpTask(String param) {

		XxlJobHelper.log("execAmqpTaskJob - 开始处理延时队列");
		log.info("execAmqpTaskJob - 开始处理延时队列");

		R<List<AmqpTaskDTO>> amqpTasksResult = amqpTaskApi.queryExecutable();
		if (!amqpTasksResult.success()) {
			XxlJobHelper.log("execAmqpTaskJob - 未找到数据，定时任务结束");
			log.info("execAmqpTaskJob - 未找到数据，定时任务结束");
			return ReturnT.SUCCESS;
		}

		List<AmqpTaskDTO> amqpTasks = amqpTasksResult.getData();
		if (CollUtil.isEmpty(amqpTasks)) {
			XxlJobHelper.log("execAmqpTaskJob - 未找到数据，定时任务结束");
			log.info("execAmqpTaskJob - 未找到数据，定时任务结束");
			return ReturnT.SUCCESS;
		}

		for (AmqpTaskDTO amqpTask : amqpTasks) {
			DateTime nowDate = DateUtil.date();
			long between = DateUtil.between(amqpTask.getDelayTime(), nowDate, DateUnit.SECOND, false);
			XxlJobHelper.log("execAmqpTaskJob - 开始发送队列任务，ID：{}，延时时间：{}，当前时间：{}，相隔时间：{}s", amqpTask.getId(), amqpTask.getDelayTime(), nowDate, between);

			amqpSendMsgUtil.convertAndSend(amqpTask.getExchange(), amqpTask.getRoutingKey(), amqpTask.getMessage(), between, ChronoUnit.SECONDS);
			amqpTask.setStatus(1);
			amqpTask.setUpdateTime(nowDate);
		}

		// 更新状态
		amqpTaskApi.update(amqpTasks);

		XxlJobHelper.log("execAmqpTaskJob - 定时任务完成");
		log.info("execAmqpTaskJob - 定时任务完成");
		return ReturnT.SUCCESS;
	}
}
