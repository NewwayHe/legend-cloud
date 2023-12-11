/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.server.controller;

import cn.legendshop.jpaplus.model.Result;
import cn.legendshop.jpaplus.model.Status;
import com.sankuai.inf.leaf.server.exception.LeafServerException;
import com.sankuai.inf.leaf.server.exception.NoKeyException;
import com.sankuai.inf.leaf.server.service.SegmentService;
import com.sankuai.inf.leaf.server.service.SnowflakeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author legendshop
 */
public class LeafController {
	private Logger logger = LoggerFactory.getLogger(LeafController.class);

	@Autowired
	private SegmentService segmentService;
	@Autowired
	private SnowflakeService snowflakeService;

	//@RequestMapping(value = "/api/segment/get/{key}")
	public String getSegmentId(@PathVariable("key") String key) {
		return get(key, segmentService.getId(key));
	}

	// @RequestMapping(value = "/api/snowflake/get/{key}")
	public String getSnowflakeId(@PathVariable("key") String key) {
		return get(key, snowflakeService.getId(key));
	}

	private String get(@PathVariable("key") String key, Result id) {
		Result result;
		if (key == null || key.isEmpty()) {
			throw new NoKeyException();
		}
		result = id;
		if (result.getStatus().equals(Status.EXCEPTION)) {
			throw new LeafServerException(result.toString());
		}
		return String.valueOf(result.getId());
	}
}
