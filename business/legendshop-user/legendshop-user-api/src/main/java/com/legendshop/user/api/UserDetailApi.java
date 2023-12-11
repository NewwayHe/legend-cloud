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
import com.legendshop.user.dto.UserDetailDTO;
import com.legendshop.user.dto.UserInformationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "userDetailApi", value = ServiceNameConstants.USER_SERVICE)
public interface UserDetailApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	@GetMapping(value = PREFIX + "/detail/getUserDetailById")
	R<UserDetailDTO> getUserDetailById(@RequestParam(value = "userId") Long userId);

	@PostMapping(value = PREFIX + "/detail/getUserDetailByIds")
	R<List<UserDetailDTO>> getUserDetailByIds(@RequestBody List<Long> userIds);

	@GetMapping(value = PREFIX + "/detail/getUserInfoById")
	R<UserInformationDTO> getUserInfoById(@RequestParam(value = "userId") Long userId);

	/**
	 * 通过用户id获取用户信息
	 */
	@PostMapping(value = PREFIX + "/detail/getUserInfoByIds")
	R<List<UserInformationDTO>> getUserInfoByIds(@RequestBody List<Long> userIds);

	@PostMapping(value = PREFIX + "/detail/updateConsumptionStatistics")
	R<Void> updateConsumptionStatistics(@RequestParam(value = "userId") Long userId, @RequestParam(value = "amount") BigDecimal amount, @RequestParam(value = "count") Integer count);

	/**
	 * 更新绑定分销员
	 */
	@PostMapping(value = PREFIX + "/detail/updateDistribution")
	R<Void> updateDistribution(@RequestParam(value = "userId") Long userId, @RequestParam(value = "distributionUserId") Long distributionUserId);


	@GetMapping(value = PREFIX + "/detail/verification/pay/password")
	R<Void> verificationPayPassword(@RequestParam(value = "userId") Long userId, @RequestParam(value = "password") String password);


	@GetMapping(value = PREFIX + "/detail/feasibility/test")
	R<Void> feasibilityTest(@RequestParam(value = "param") String param);

	@GetMapping(value = PREFIX + "/detail/notAddres/list")
	R<List<UserDetailDTO>> queryByNotAddress();

	@GetMapping(value = PREFIX + "/detail/update")
	R update(List<UserDetailDTO> userDetailList);
}

