/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.id.api;

import cn.legendshop.jpaplus.model.Result;
import cn.legendshop.jpaplus.model.Results;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Id服务客户端
 *
 * @author legendshop
 */
@FeignClient(contextId = "IdClient", value = ServiceNameConstants.ID_SERVICE)
public interface IdApi {

	String PREFIX = ServiceNameConstants.ID_SERVICE_RPC_PREFIX;

	/**
	 * 号段发号器
	 *
	 * @param key 关键字
	 * @return
	 */
	@GetMapping(PREFIX + "/p/getSegmentId")
	R<Result> getSegmentId(@RequestParam("key") String key);


	/**
	 * 雪花算法
	 */
	@GetMapping(PREFIX + "/p/getSnowflakeId")
	R<Result> getSnowflakeId(@RequestParam("key") String key);

	/**
	 * 号段发号器,批量获取Id，为了提高性能
	 *
	 * @param key   关键字
	 * @param total 一口气获取的总数
	 * @return
	 */
	@GetMapping(PREFIX + "/p/getSegmentIds")
	R<Results> getSegmentIds(@RequestParam("key") String key, @RequestParam("total") int total);

	/**
	 * 雪花算法，批量获取Id，为了提高性能
	 */
	@GetMapping(PREFIX + "/p/getSnowflakeIds")
	R<Results> getSnowflakeIds(@RequestParam("key") String key, @RequestParam("total") int total);
}
