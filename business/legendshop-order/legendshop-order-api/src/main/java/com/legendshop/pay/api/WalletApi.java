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
import com.legendshop.pay.dto.DistributionWalletDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author legendshop
 */
@FeignClient(contextId = "walletApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface WalletApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	@GetMapping(value = PREFIX + "/wallet/p/get/distributionWallet")
	R<DistributionWalletDTO> getByUserId(@RequestParam(value = "userId") Long userId, @RequestParam(value = "withdrawType", required = false) String withdrawType);
}
