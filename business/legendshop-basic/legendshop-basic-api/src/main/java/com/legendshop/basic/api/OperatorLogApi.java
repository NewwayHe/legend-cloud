/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.query.OperatorLogQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.common.core.dto.OperatorLogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 操作日志服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "operatorLogApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface OperatorLogApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	@PostMapping(value = PREFIX + "/operatorLog/page")
	R<PageSupport<OperatorLogDTO>> page(@RequestBody OperatorLogQuery operatorLogQuery);


}