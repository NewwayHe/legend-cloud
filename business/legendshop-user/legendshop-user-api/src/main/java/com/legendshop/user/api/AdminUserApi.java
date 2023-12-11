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
import com.legendshop.user.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "adminUserApi", value = ServiceNameConstants.USER_SERVICE)
public interface AdminUserApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	@GetMapping(value = PREFIX + "/admin/user/getUserInfo")
	R<UserInfo> getUserInfo(@RequestParam(value = "username") String username);

	@GetMapping(value = PREFIX + "/admin/user/getMenuById")
	R<List<Long>> queryUsersByMenuId(@RequestParam(value = "menuId") int menuId);

	/**
	 * @param menuName 根据菜单名字查询所有具有菜单全选的平台用户ID
	 * @return
	 */
	@GetMapping(value = PREFIX + "/admin/user/queryUserIdsByMenuName")
	R<List<Long>> queryUserIdsByMenuName(@RequestParam(value = "menuName") String menuName);

	@GetMapping(value = PREFIX + "/getAdamin")
	R<List<Long>> getAdamin(@RequestParam(value = "productId") Long productId);

	@GetMapping(value = PREFIX + "/admin/user/updateStatusByUserName")
	R updateStatusByUserName(@RequestParam(value = "username") String username, @RequestParam(value = "status") Boolean status);
}
