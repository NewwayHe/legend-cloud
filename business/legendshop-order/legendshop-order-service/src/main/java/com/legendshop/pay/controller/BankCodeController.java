/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.BankCodeDTO;
import com.legendshop.pay.query.BankCodeQuery;
import com.legendshop.pay.service.BankCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 银行编码(BankCode)表控制层
 *
 * @author legendshop
 * @since 2021-04-07 09:56:29
 */
@RestController
@Tag(name = "银行编码选择器")
@RequestMapping(value = "/bankCode", produces = MediaType.APPLICATION_JSON_VALUE)
public class BankCodeController {

	/**
	 * 银行编码(BankCode)服务对象
	 */
	@Autowired
	private BankCodeService bankCodeService;

	/**
	 * 根据id查询银行编码
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/loadBankCode")
	@Operation(summary = "【公共】查询银行编码")
	public R<PageSupport<BankCodeDTO>> loadBankCode(BankCodeQuery query) {
		return R.ok(bankCodeService.query(query));
	}

}
