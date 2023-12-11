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
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.user.dto.CustomerBillCreateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "customerBillApi", value = ServiceNameConstants.USER_SERVICE)
public interface CustomerBillApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	/**
	 * 保存单个账单信息
	 *
	 * @param customerBillCreateDTO 单个账单信息
	 * @return 操作结果
	 */
	@PostMapping(value = PREFIX + "/customer/bill/save")
	R<Void> save(@RequestBody CustomerBillCreateDTO customerBillCreateDTO);

	/**
	 * 批量保存账单信息
	 *
	 * @param customerBillCreateList 账单信息列表
	 * @return 操作结果
	 */
	@PostMapping(value = PREFIX + "/customer/bill/saveList")
	R<Void> save(@RequestBody List<CustomerBillCreateDTO> customerBillCreateList);


}
