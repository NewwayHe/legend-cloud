/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.service.UserWalletDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title WalletDetailsController
 * @date 2022/4/27 18:08
 * @description：
 */
@Slf4j

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user/wallet/details")
public class WalletDetailsController {

	private final UserWalletDetailsService userWalletDetailsService;

	@GetMapping("/findDetailsByBusinessId")
	public R<List<UserWalletDetailsDTO>> findDetailsByBusinessId(@RequestParam(value = "businessId") Long businessId, @RequestParam(value = "type") WalletBusinessTypeEnum type) {
		return R.ok(userWalletDetailsService.findDetailsByBusinessId(businessId, type));
	}
}
