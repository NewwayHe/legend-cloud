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
import com.legendshop.data.dto.DataActivityCollectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author legendshop
 */
@FeignClient(contextId = "dataApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface DataApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	@GetMapping(PREFIX + "/data/lasted/collect")
	R<DataActivityCollectDTO> getLastedCollectData();

	@GetMapping(PREFIX + "/data/activity/collect")
	R<Boolean> activityCollect(@RequestParam(value = "flag") Boolean flag);

	/**
	 * 用户数量统计定时任务处理
	 *
	 * @return
	 */
	@GetMapping(PREFIX + "/data/userAmount")
	R<Void> dataUserAmountJobHandle();


}
