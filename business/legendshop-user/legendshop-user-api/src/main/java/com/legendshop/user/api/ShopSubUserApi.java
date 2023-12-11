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
import com.legendshop.user.dto.ShopSubUserDTO;
import com.legendshop.user.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 服务间调用接口
 *
 * @author legendshop
 **/
@FeignClient(contextId = "shopSubUserApi", value = ServiceNameConstants.USER_SERVICE)
public interface ShopSubUserApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	/**
	 * 根据id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = PREFIX + "/s/shop/subUser/getById")
	R<ShopSubUserDTO> getById(@RequestParam(value = "id") Long id);

	@GetMapping(value = PREFIX + "/s/shop/subUser/getUserInfo")
	R<UserInfo> getUserInfo(@RequestParam(value = "username") String username);

	@GetMapping(PREFIX + "/s/shop/subUser/queryAllShopSubUser")
	R<List<ShopSubUserDTO>> queryAllShopSubUser();

	@GetMapping(PREFIX + "/s/shop/subUser/updateStatusByUserName")
	R updateStatusByUserName(@RequestParam(value = "username") String username, @RequestParam(value = "status") Boolean status);

}


