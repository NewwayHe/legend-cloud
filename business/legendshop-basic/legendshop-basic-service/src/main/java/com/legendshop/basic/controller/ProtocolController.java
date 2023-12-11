/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import com.legendshop.basic.dto.ProtocolDTO;
import com.legendshop.basic.service.ProtocolService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统协议管理控制器.
 *
 * @author legendshop
 */
@Tag(name = "协议管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/protocol")
public class ProtocolController {


	private final ProtocolService protocolService;


	@Operation(summary = "【公共】获取相关协议")
	@GetMapping("/get")
	public R<ProtocolDTO> query(String code) {
		return protocolService.getByCode(code);
	}

}
