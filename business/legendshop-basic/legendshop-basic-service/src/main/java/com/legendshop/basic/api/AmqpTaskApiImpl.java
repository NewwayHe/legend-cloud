/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.basic.service.AmqpTaskService;
import com.legendshop.common.core.constant.R;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 队列任务表(AmqpTask)表Feign调用
 *
 * @author legendshop
 * @since 2022-04-29 14:16:54
 */
@RestController
public class AmqpTaskApiImpl implements AmqpTaskApi {

	/**
	 * 队列任务表(AmqpTask)服务对象
	 */
	@Autowired
	private AmqpTaskService amqpTaskService;


	/**
	 * 保存队列任务表
	 *
	 * @param amqpTaskDTO
	 * @return
	 */
	@Override
	public R convertAndSend(@Valid @RequestBody AmqpTaskDTO amqpTaskDTO) {
		return amqpTaskService.convertAndSend(amqpTaskDTO);
	}

	@Override
	public R<List<AmqpTaskDTO>> queryExecutable() {

		return R.ok(amqpTaskService.queryExecutable());
	}

	@Override
	public R update(List<AmqpTaskDTO> amqpTasks) {
		amqpTaskService.updateList(amqpTasks);
		return R.ok();
	}

}
