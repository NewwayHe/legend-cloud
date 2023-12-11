/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "passportApi", value = ServiceNameConstants.USER_SERVICE)
public interface PassportApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	@GetMapping(value = PREFIX + "/passport/getOpenIdByUserId")
	R<String> getOpenIdByUserId(@RequestParam(value = "userId") Long userId, @RequestParam(value = "source") String source);

	@GetMapping(value = PREFIX + "/passport/getOpenIdByUserIds")
	R<List<String>> getOpenIdByUserIds(@RequestParam(value = "userIds") List<Long> userIds, @RequestParam(value = "source") String source);

	@PostMapping(value = PREFIX + "/passport/unboundUserPassport")
	R<Boolean> unboundUserPassport(@RequestParam(value = "userId") Long userId, @RequestParam(value = "type") String type, @RequestParam(value = "source") String source);


}
