/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.user;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.bo.CustomerBillBO;
import com.legendshop.user.dto.CustomerBillDTO;
import com.legendshop.user.dto.CustomerBillGroupDTO;
import com.legendshop.user.query.CustomerBillQuery;
import com.legendshop.user.service.CustomerBillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 客户账单
 *
 * @author legendshop
 */
@Tag(name = "客户账单")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/p/customer/bill", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserCustomerBillController {

	private final CustomerBillService customerBillService;


	@Operation(summary = "【用户】用户账单分页查询")
	@GetMapping("/page")
	public R<PageSupport<CustomerBillGroupDTO>> page(CustomerBillQuery customerBillQuery) {
		Long userId = SecurityUtils.getUserId();
		customerBillQuery.setOwnerId(userId);
		if (ObjectUtil.isNotEmpty(customerBillQuery.getEndDate())) {
			customerBillQuery.setEndDate(DateUtil.endOfDay(customerBillQuery.getEndDate()));
		}
		PageSupport<CustomerBillGroupDTO> pageSupport = customerBillService.queryPageGroupByDate(customerBillQuery);
		return R.ok(pageSupport);
	}


	@Operation(summary = "【用户】用户账单详情")
	@Parameter(name = "id", description = "id")
	@GetMapping("/{id}")
	public R<CustomerBillBO> get(@PathVariable Long id) {
		return customerBillService.getDetailById(id);
	}


	@Operation(summary = "【用户】用户账单逻辑删除")
	@Parameter(name = "id", description = "id")
	@DeleteMapping("/update/del/flag/{id}")
	public R<String> updateDelFlag(@PathVariable Long id) {
		if (customerBillService.updateDelFlag(id) > 0) {
			return R.ok();
		}
		return R.fail();
	}


	@Operation(summary = "【用户】用户账单批量逻辑删除")
	@PostMapping("/batch/update/del/flag")
	public R<String> batchUpdateDelFlag(@Valid @RequestBody CustomerBillDTO expensesRecordDTO) {
		if (customerBillService.batchUpdateDelFlag(expensesRecordDTO.getIds()) > 0) {
			return R.ok();
		}
		return R.fail();
	}

}
