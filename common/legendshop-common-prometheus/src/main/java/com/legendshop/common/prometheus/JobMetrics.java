/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author legendshop
 */
@Component
public class JobMetrics implements MeterBinder {
	public Counter job1Counter;
	public Counter job2Counter;

	public Map<String, Double> map;

	JobMetrics() {
		map = new HashMap<>();
	}

	@Override
	public void bindTo(MeterRegistry meterRegistry) {
		this.job1Counter = Counter.builder("counter_builder_job_counter1")
				.tags(new String[]{"name", "tag_job_counter1"})
				.description("description-Job counter1 execute count").register(meterRegistry);

		this.job2Counter = Counter.builder("counter_builder_job_counter2")
				.tags(new String[]{"name", "tag_job_counter2"})
				.description("description-Job counter2 execute count ").register(meterRegistry);

		Gauge.builder("gauge_builder_job_gauge", map, x -> x.get("x"))
				.tags("name", "tag_job_gauge")
				.description("description-Job gauge")
				.register(meterRegistry);
	}
}
