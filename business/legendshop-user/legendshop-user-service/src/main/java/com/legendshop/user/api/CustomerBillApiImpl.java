/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.CustomerBillCreateDTO;
import com.legendshop.user.service.CustomerBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class CustomerBillApiImpl implements CustomerBillApi {

	final CustomerBillService customerBillService;

	@Override
	public R<Void> save(CustomerBillCreateDTO customerBillCreateDTO) {
		customerBillService.save(customerBillCreateDTO);
		return R.ok();
	}

	@Override
	public R<Void> save(List<CustomerBillCreateDTO> customerBillCreateList) {
		customerBillService.save(customerBillCreateList);
		return R.ok();
	}
}
