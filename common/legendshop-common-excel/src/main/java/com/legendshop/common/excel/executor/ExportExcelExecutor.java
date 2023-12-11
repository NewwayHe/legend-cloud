/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.executor;

import com.legendshop.common.excel.handler.CustomRejectedExecutionHandler;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ExportExcelExecutor
 * @date 2022/4/26 17:08
 * @description：导出文件线程池
 */
public class ExportExcelExecutor {

	private static ThreadPoolExecutor threadPoolExecutor;

	public ExportExcelExecutor(AmqpSendMsgUtil amqpSendMsgUtil) {
		threadPoolExecutor = new ThreadPoolExecutor(1, 5, 1L, TimeUnit.MINUTES, new ArrayBlockingQueue(1000), new CustomRejectedExecutionHandler(amqpSendMsgUtil));
	}

	public void execute(ExportExcelRunnable runnable) {
		threadPoolExecutor.execute(runnable);
	}
}
