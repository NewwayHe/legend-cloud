/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.handler;

import cn.hutool.core.util.StrUtil;
import com.legendshop.common.excel.dto.ExportExcelUpdateDTO;
import com.legendshop.common.excel.enums.ExportExcelStatusEnum;
import com.legendshop.common.excel.executor.ExportExcelRunnable;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author legendshop
 * @version 1.0.0
 * @title CustomRejectedExecutionHandler
 * @date 2022/4/26 17:03
 * @description：自定义拒绝策略
 */
public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

	private AmqpSendMsgUtil amqpSendMsgUtil;

	public CustomRejectedExecutionHandler(AmqpSendMsgUtil amqpSendMsgUtil) {
		super();
		this.amqpSendMsgUtil = amqpSendMsgUtil;
	}

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		if ((r instanceof ExportExcelRunnable) && StrUtil.isNotBlank(((ExportExcelRunnable) r).getFileName())) {
			String fileName = ((ExportExcelRunnable) r).getFileName();
			ExportExcelUpdateDTO updateDTO = new ExportExcelUpdateDTO();
			updateDTO.setFileName(fileName);
			updateDTO.setStatus(ExportExcelStatusEnum.FAIL);
			updateDTO.setRemark("系统繁忙，请稍后重试！");
			this.amqpSendMsgUtil.convertAndSend(AmqpConst.SYSTEM_EXPORT_EXCEL_EXCHANGE, AmqpConst.SYSTEM_EXPORT_EXCEL_UPDATE_ROUTING_KEY,
					updateDTO, -1L, ChronoUnit.MILLIS);
		} else {
			throw new RejectedExecutionException("Task " + r.toString() +
					" rejected from " +
					executor.toString());
		}
	}
}
