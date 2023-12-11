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
import com.legendshop.pay.dto.UserWalletDTO;
import com.legendshop.pay.dto.UserWalletPayDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author legendshop
 */
@FeignClient(contextId = "userWalletApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface UserWalletApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	/**
	 * 当前登录用户支付钱包信息
	 */
	@GetMapping(value = PREFIX + "/p/wallet/pay/info")
	R<UserWalletPayDTO> payInfo();

	@PostMapping(value = PREFIX + "/p/wallet/password/validation")
	R<Void> passwordValidation(@RequestParam(value = "userId") Long userId, @RequestParam(value = "payPassword") String payPassword);

	/**
	 * 测试
	 */
	@GetMapping(value = PREFIX + "/p/wallet/feasibility/test")
	R<Void> feasibilityTest(@RequestParam(value = "param") String param);


	@GetMapping(value = PREFIX + "/p/wallet/pay/getByUserId")
	R<UserWalletDTO> getByUserId(@RequestParam(value = "userId") Long userId);


}
