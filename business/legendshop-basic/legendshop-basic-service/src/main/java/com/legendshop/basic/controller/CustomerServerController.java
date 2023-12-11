/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.service.SysParamItemService;
import com.legendshop.common.core.constant.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 客服
 *
 * @author legendshop
 * @create: 2021-06-15 16:44
 */
@RestController
@RequestMapping(value = "/customer/server")
public class CustomerServerController {

	/**
	 * (SysParamItem)服务对象
	 */
	@Autowired
	private SysParamItemService sysParamItemService;

	@GetMapping
	public R<String> getCustomerServerUrl() {
		List<SysParamItemDTO> dtoList = sysParamItemService.getListByParentId(200L);
		Optional<SysParamItemDTO> optional = Optional.ofNullable(dtoList).orElse(Collections.emptyList()).stream().findFirst();
		if (optional.isPresent()) {
			SysParamItemDTO sysParamItemDTO = optional.get();
			return R.ok(sysParamItemDTO.getValue());
		}
		return R.ok();
	}

}
