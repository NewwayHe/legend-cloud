/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.security.dto.OrdinaryUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.user.dto.BaseUserQuery;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.dto.VerifyOrdinaryUserDTO;
import com.legendshop.user.query.OrdinaryUserQuery;
import com.legendshop.user.service.BaseUserService;
import com.legendshop.user.service.OrdinaryUserService;
import com.legendshop.user.service.UserDetailService;
import com.legendshop.user.util.PasswordGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author legendshop
 */
@Slf4j
@RestController
@Tag(name = "普通用户管理")
@RequiredArgsConstructor
@RequestMapping(value = "/ordinary/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrdinaryUserController {

	final HttpServletRequest request;

	final UserTokenUtil userTokenUtil;

	final BaseUserService baseUserService;

	final UserDetailService userDetailService;

	final OrdinaryUserService ordinaryUserService;

	@Operation(summary = "【用户】用户是否登录")
	@GetMapping("/login/flag")
	public R<Boolean> loginFlag() {
		try {
			OrdinaryUserDetail user = SecurityUtils.getUser();
			if (null == user) {
				return R.ok(false);
			}
			return R.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取用户信息异常！");
			return R.fail(false);
		}
	}


	/**
	 * 用户端注册
	 *
	 * @param userDTO
	 * @return
	 */
	@Operation(summary = "【用户】用户端注册")
	@PostMapping
	public R register(HttpServletRequest request, @Validated({PasswordGroup.class, Default.class}) @RequestBody VerifyOrdinaryUserDTO userDTO) {
		userDTO.setRegIp(request.getRemoteAddr());
		userDTO.setVisitorId(request.getHeader(RequestHeaderConstant.USER_KEY));
		return this.ordinaryUserService.register(userDTO);
	}

	/**
	 * 修改密码
	 *
	 * @param userDTO the userDTO
	 * @return result
	 */
	@Operation(summary = "【用户】修改密码; 需要参数：手机号，验证码，新密码")
	@PutMapping("/reset/password")
	public R<Void> resetPassword(@Validated({PasswordGroup.class}) @RequestBody VerifyOrdinaryUserDTO userDTO) {

		Long userId = userTokenUtil.getUserId(request);
		if (null != userId && 0L != userId) {
			String mobile = baseUserService.getMobile(new BaseUserQuery(userId, UserTypeEnum.USER.getLoginType()));
			userDTO.setMobile(mobile);
		}
		return this.ordinaryUserService.updatePassword(userDTO);
	}

	/**
	 * 获取所有在线用户
	 *
	 * @param ordinaryUserQuery
	 * @return
	 */
	@Operation(summary = "【用户】获取所有在线用户")
	@PostMapping(value = "/queryAllUser")
	public R<PageSupport<OrdinaryUserDTO>> queryAllUser(OrdinaryUserQuery ordinaryUserQuery) {
		return R.ok(ordinaryUserService.queryAllUser(ordinaryUserQuery));
	}
}
