/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.query.OrdinaryUserQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "OrdinaryUserApi", value = ServiceNameConstants.USER_SERVICE)
public interface OrdinaryUserApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	@PostMapping(value = PREFIX + "/ordinary/user/getByIds")
	R<List<OrdinaryUserDTO>> getByIds(@RequestBody List<Long> userIds);

	@GetMapping(value = PREFIX + "/ordinary/user/getById")
	R<OrdinaryUserDTO> getById(@RequestParam(value = "userId") Long userId);

	@GetMapping(value = PREFIX + "/ordinary/user/getUserInfo")
	R<UserInfo> getUserInfo(@RequestParam(value = "username") String username);

	@PostMapping(value = PREFIX + "/ordinary/user/queryByMobile")
	R<List<OrdinaryUserDTO>> queryByMobile(@RequestBody List<String> mobileList);

	@PostMapping(value = PREFIX + "/ordinary/user/getByMobile")
	R<OrdinaryUserDTO> getByMobile(@RequestParam(value = "invitationMobile") String invitationMobile);

	@GetMapping(value = PREFIX + "/ordinary/user")
	R<OrdinaryUserDTO> getUser(@RequestParam(value = "identifier") String identifier);

	@GetMapping(value = PREFIX + "/ordinary/user/ids")
	R<List<Long>> getUserIds(@RequestParam(value = "off") Integer off, @RequestParam(value = "size") Integer size);

	@GetMapping(value = PREFIX + "/ordinary/user/queryAllUser")
	R<PageSupport<OrdinaryUserDTO>> queryAllUser(OrdinaryUserQuery ordinaryUserQuery);

	@GetMapping(value = PREFIX + "/ordinary/user/getByLikeMobile")
	R<List<Long>> getByLikeMobile(@RequestParam(value = "mobile") String mobile);

	@GetMapping(value = PREFIX + "/ordinary/user/updateStatusByUserName")
	R updateStatusByUserName(@RequestParam(value = "username") String username, @RequestParam(value = "status") Boolean status);
}

