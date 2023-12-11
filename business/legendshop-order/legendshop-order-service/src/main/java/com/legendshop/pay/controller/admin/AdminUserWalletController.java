/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.pay.bo.UserWalletBO;
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.excel.UserWalletDetailsExcelDTO;
import com.legendshop.pay.query.UserWalletDetailsQuery;
import com.legendshop.pay.service.UserWalletDetailsService;
import com.legendshop.pay.service.UserWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * (UserWallet)表控制层
 *
 * @author legendshop
 * @since 2021-03-13 14:09:48
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "用户钱包相关")
@RequestMapping(value = "/admin/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserWalletController {

	private final UserWalletService userWalletService;

	private final UserWalletDetailsService userWalletDetailsService;

	/**
	 * 平台用户补偿
	 */
	@PostMapping(value = "/compensation")
	@Operation(summary = "【后台】平台用户补偿（充钱）")
	public R<Void> platformCompensation(@RequestParam(value = "identifier") String identifier, @RequestParam(value = "amount") BigDecimal amount) {
		return this.userWalletService.platformCompensation(identifier, amount);
	}

	/**
	 * 用户钱包流水详情
	 */
	@GetMapping(value = "/page")
	@Operation(summary = "【后台】用户钱包流水分页")
	public R<PageSupport<UserWalletDetailsDTO>> page(UserWalletDetailsQuery query) {
		return R.ok(this.userWalletDetailsService.associatePage(query));
	}


	@GetMapping(value = "/commission/{id}")
	@Operation(summary = "【后台】分销员佣金数据")
	public R<UserWalletBO> getCommissionByUserId(@PathVariable(value = "id") Long id) {
		return this.userWalletService.getCommissionByUserId(id);
	}


	@GetMapping("/excel")
	@Operation(summary = "[后台] 预存款明细导出")
	@ExportExcel(name = "预存款明细导出", sheet = "预存款明细导出")
	public List<UserWalletDetailsExcelDTO> walletExcel(UserWalletDetailsQuery query) {
		return this.userWalletDetailsService.walletExcel(query);
	}

}
