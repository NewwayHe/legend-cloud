/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.data.dto.UserDataViewsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author legendshop
 */
@FeignClient(contextId = "baiDuCountApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface BaiDuCountApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	@GetMapping(PREFIX + "/count/baidu/view/count")
	R<UserDataViewsDTO> getPvUv(@RequestParam(value = "source") String source);

	@GetMapping(PREFIX + "/count/baidu/statistics/archive")
	R baiduStatisticsArchive(@RequestParam(value = "startDate") Date startDate, @RequestParam(value = "endDate") Date endDate);
}
