/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller.user;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.dto.OrdinaryUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.pay.dto.UserWalletDTO;
import com.legendshop.pay.dto.UserWalletPayDTO;
import com.legendshop.pay.dto.UserWithdrawApplyDTO;
import com.legendshop.pay.service.UserWalletService;
import com.legendshop.user.api.UserDetailApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * (UserWallet)表控制层
 *
 * @author legendshop
 * @since 2021-03-13 14:09:48
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/p/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "用户钱包")
public class UserWalletController {

	private final UserDetailApi userDetailApi;

	private final UserWalletService userWalletService;

	/**
	 * 用户钱包信息
	 */
	@GetMapping
	@Operation(summary = "用户钱包信息")
	public R<UserWalletDTO> info() {
		OrdinaryUserDetail user = SecurityUtils.getUser();
		return R.ok(this.userWalletService.getByUserId(user.getUserId()));
	}

	/**
	 * 用户钱包支付信息
	 */
	@GetMapping(value = "/pay/info")
	public R<UserWalletPayDTO> payInfo() {
		OrdinaryUserDetail user = SecurityUtils.getUser();
		UserWalletPayDTO userWalletPayDTO = this.userWalletService.payInfo(user.getUserId());
		if (ObjectUtil.isNotNull(userWalletPayDTO)) {
			userWalletPayDTO.setPayPassword(null);
		}
		return R.ok(userWalletPayDTO);
	}

	/**
	 * 用户提现
	 * 默认使用微信付款到零钱
	 */
	@Operation(summary = "用户提现")
	@PostMapping(value = "/withdraw")
	public R<Void> withdraw(@RequestBody UserWithdrawApplyDTO apply, @RequestHeader(value = "source") String source) {
		OrdinaryUserDetail user = SecurityUtils.getUser();
		apply.setUserId(user.getUserId());
		if (StrUtil.isBlank(source)) {
			return R.fail("提现失败，请求头信息有误~");
		}
		apply.setSource(source);
		return this.userWalletService.userWithdraw(apply);
	}

	@PostMapping(value = "/password/validation")
	public R<Void> passwordValidation(@RequestParam(value = "userId") Long userId, @RequestParam(value = "payPassword") String payPassword) {
		return this.userWalletService.passwordValidation(userId, payPassword);
	}

	@GetMapping(value = "/feasibility/test")
	@SentinelResource(value = "/feasibility/test", fallback = "testFallback")
	public R<Void> feasibilityTest(@RequestParam(value = "param") String param) {
		return this.userDetailApi.feasibilityTest(param);
	}

	public R<Void> testFallback(String param, Throwable e) {
		log.error("服务异常，降级处理！");
		if (e instanceof RuntimeException) {
			log.error("异常：" + e.getMessage());
		}
		return R.fail(e.getMessage());
	}

	@GetMapping(value = "/pay/getByUserId")
	public R<UserWalletDTO> getByUserId(@RequestParam(value = "userId") Long userId) {
		return R.ok(userWalletService.getByUserId(userId));
	}

}
