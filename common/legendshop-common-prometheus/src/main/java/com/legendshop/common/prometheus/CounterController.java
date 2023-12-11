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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
public class CounterController {

	@Autowired
	private JobMetrics jobMetrics;

	@RequestMapping(value = "/counter1", method = RequestMethod.GET)
	public void counter1() {
		jobMetrics.job2Counter.increment();
	}

	@RequestMapping(value = "/counter2", method = RequestMethod.GET)
	public void counter2() {
		jobMetrics.job2Counter.increment();
	}

	@RequestMapping(value = "/gauge", method = RequestMethod.GET)
	public void gauge(@RequestParam(value = "x") String x) {
		System.out.println("gauge controller x" + x);
		jobMetrics.map.put("x", Double.valueOf(x));
	}

}
