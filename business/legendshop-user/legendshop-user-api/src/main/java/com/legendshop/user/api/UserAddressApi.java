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
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.dto.UserAddressDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "userAddressApi", value = ServiceNameConstants.USER_SERVICE)
public interface UserAddressApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	/**
	 * 获取用户默认地址
	 */
	@GetMapping(value = PREFIX + "/p/address/getDefaultAddress")
	R<UserAddressBO> getDefaultAddress(@RequestParam(value = "userId") Long userId);

	@GetMapping(value = PREFIX + "/p/address/getById")
	R<UserAddressDTO> getById(@RequestParam(value = "id") Long id);

	@GetMapping(value = PREFIX + "/p/address/getAddressInfo")
	R<UserAddressBO> getAddressInfo(@RequestParam(value = "id") Long id);

	@GetMapping(value = PREFIX + "/p/address/getUserAddressForOrder")
	R<UserAddressBO> getUserAddressForOrder(@RequestParam(value = "userId") Long userId, @RequestParam(value = "addressId", required = false) Long addressId);

	/**
	 * 获取用户的默认收货地址
	 *
	 * @param userId
	 * @return
	 */
	@GetMapping(value = PREFIX + "/p/address/getCommonAddress")
	R<UserAddressBO> getCommonAddress(@RequestParam(value = "userId", required = false) Long userId);

	@GetMapping(value = PREFIX + "/p/address/query")
	R<List<UserAddressDTO>> queryByUserId(@RequestParam(value = "userId") Long userId);
}
