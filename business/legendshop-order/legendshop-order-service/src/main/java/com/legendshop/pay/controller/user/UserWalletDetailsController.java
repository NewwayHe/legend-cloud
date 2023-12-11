/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.dto.OrdinaryUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.enums.UserWalletAmountTypeEnum;
import com.legendshop.pay.query.UserWalletDetailsQuery;
import com.legendshop.pay.service.UserWalletDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户钱包收支记录详情(UserWalletDetails)表控制层
 *
 * @author legendshop
 * @since 2021-03-13 14:44:01
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/p/wallet/details", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "用户钱包流水")
public class UserWalletDetailsController {

	private final UserWalletDetailsService userWalletDetailsService;


	@GetMapping(value = "/page")
	@Operation(summary = "【用户】钱包收支记录")
	public R<PageSupport<UserWalletDetailsDTO>> page(UserWalletDetailsQuery query) {
		OrdinaryUserDetail user = SecurityUtils.getUser();
		query.setUserId(user.getUserId());
		query.setAmountType(UserWalletAmountTypeEnum.AVAILABLE_AMOUNT.name());
		return R.ok(this.userWalletDetailsService.pageList(query));
	}

	@GetMapping(value = "/detail/{id}")
	@Operation(summary = "【用户】获取钱包明细详情")
	public R<UserWalletDetailsDTO> getDetailById(@PathVariable("id") Long id) {
		return R.ok(this.userWalletDetailsService.getDetailById(id));
	}

}
