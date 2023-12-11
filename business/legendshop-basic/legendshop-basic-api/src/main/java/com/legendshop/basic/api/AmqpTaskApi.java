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
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @date 2022/5/5 10:37
 * @description：
 */
@FeignClient(contextId = "AmqpTaskApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface AmqpTaskApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	@PostMapping(PREFIX + "/amqpTask")
	R<Void> convertAndSend(@RequestBody AmqpTaskDTO amqpTaskDTO);

	/**
	 * 查询可执行的队列
	 *
	 * @return
	 */
	@GetMapping(PREFIX + "/executable")
	R<List<AmqpTaskDTO>> queryExecutable();

	/**
	 * 更新队列状态
	 *
	 * @param amqpTasks
	 * @return
	 */
	@GetMapping(PREFIX + "/updata")
	R update(List<AmqpTaskDTO> amqpTasks);
}
