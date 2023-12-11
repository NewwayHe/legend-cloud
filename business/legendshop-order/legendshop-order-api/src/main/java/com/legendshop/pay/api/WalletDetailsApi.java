/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title WalletDetailsClient
 * @date 2022/4/27 18:38
 * @description：
 */
@FeignClient(contextId = "WalletDetailsApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface WalletDetailsApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	@GetMapping(PREFIX + "/user/wallet/details/findDetailsByBusinessId")
	R<List<UserWalletDetailsDTO>> findDetailsByBusinessId(@RequestParam(value = "businessId") Long businessId, @RequestParam(value = "type") WalletBusinessTypeEnum type);
}
