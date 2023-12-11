/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.api;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.dto.OrdinaryUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.pay.dto.UserWalletDTO;
import com.legendshop.pay.dto.UserWalletPayDTO;
import com.legendshop.pay.service.UserWalletService;
import com.legendshop.user.api.UserDetailApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserWalletApiImpl implements UserWalletApi {

	private final UserDetailApi userDetailApi;

	private final UserWalletService userWalletService;

	/**
	 * 用户钱包支付信息
	 */
	@Override
	public R<UserWalletPayDTO> payInfo() {
		OrdinaryUserDetail user = SecurityUtils.getUser();
		UserWalletPayDTO userWalletPayDTO = this.userWalletService.payInfo(user.getUserId());
		if (ObjectUtil.isNotNull(userWalletPayDTO)) {
			userWalletPayDTO.setPayPassword(null);
		}
		return R.ok(userWalletPayDTO);
	}

	@Override
	public R<Void> passwordValidation(@RequestParam(value = "userId") Long userId, @RequestParam(value = "payPassword") String payPassword) {
		return this.userWalletService.passwordValidation(userId, payPassword);
	}

	@Override
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

	@Override
	public R<UserWalletDTO> getByUserId(@RequestParam(value = "userId") Long userId) {
		return R.ok(userWalletService.getByUserId(userId));
	}

}
