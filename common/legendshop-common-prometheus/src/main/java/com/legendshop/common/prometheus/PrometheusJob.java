/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.prometheus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Component
@EnableScheduling
public class PrometheusJob {

	private Integer count1 = 0;

	private Integer count2 = 0;

	@Autowired
	private JobMetrics jobMetrics;

	@Async
	@Scheduled(fixedDelay = 1000)
	public void doSomething() {
		count1++;
		jobMetrics.job1Counter.increment();
		jobMetrics.map.put("x", Double.valueOf(count1));
		if (count1 % 2 == 0) {
			jobMetrics.map.put("x", Double.valueOf(1));
		}

	}

	@Async
	@Scheduled(fixedDelay = 10000)
	public void doSomethingOther() {
		count2++;
		jobMetrics.job2Counter.increment();
//		System.out.println("task2 count:" + count2);
	}
}
