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
import com.legendshop.user.dto.ShopUserDTO;
import com.legendshop.user.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 服务间调用接口
 *
 * @author legendshop
 */
@FeignClient(contextId = "shopUserApi", value = ServiceNameConstants.USER_SERVICE)
public interface ShopUserApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	@GetMapping(value = PREFIX + "/s/user/getUserInfo")
	R<UserInfo> getUserInfo(@RequestParam(value = "username") String username);


	@PostMapping(value = PREFIX + "/s/user/update/avatar")
	R<Void> updateAvatar(@RequestParam(value = "userId") Long userId, @RequestParam(value = "avatar") String avatar);

	@GetMapping(value = PREFIX + "/s/user/getShopUserInfo")
	R<ShopUserDTO> getShopUserInfo(@RequestParam(value = "shopUserId") Long shopUserId);

	@PostMapping(value = PREFIX + "/s/user/getByIds")
	R<List<ShopUserDTO>> getByIds(@RequestBody List<Long> ids);

	/**
	 * 根据店铺ID来返回用户商家用户信息
	 *
	 * @param shopUserId
	 */
	@GetMapping(value = PREFIX + "/s/user/getByShopId")
	R<ShopUserDTO> getByShopId(@RequestParam(value = "shopUserId") Long shopUserId);

	/**
	 * 获取所有上线的店铺
	 *
	 * @return
	 */
	@GetMapping(PREFIX + "/s/user/queryAllShop")
	R<List<ShopUserDTO>> queryAllShop();

	/**
	 * 锁定用户
	 *
	 * @param username
	 * @param status
	 */
	@GetMapping(PREFIX + "/s/user/updateStatusByUserName")
	R updateStatusByUserName(@RequestParam(value = "username") String username, @RequestParam(value = "status") Boolean status);
}
