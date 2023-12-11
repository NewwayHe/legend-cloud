/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.CustomerBillCreateDTO;
import com.legendshop.user.service.CustomerBillService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户账单
 *
 * @author legendshop
 */
@RestController
@AllArgsConstructor
public class CustomerBillController {

	final CustomerBillService customerBillService;

	@PostMapping(value = "/customer/bill/saveList")
	public R<Void> save(@RequestBody List<CustomerBillCreateDTO> customerBillCreateList) {
		customerBillService.save(customerBillCreateList);
		return R.ok();
	}
}
