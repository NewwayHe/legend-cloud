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
import com.legendshop.common.core.dto.SystemLogDTO;
import com.legendshop.user.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/history")
public class LoginHistoryController {

	private final LoginHistoryService loginHistoryService;

	@GetMapping(value = "test")
	public R<Void> test() {
		SystemLogDTO systemLogDTO = new SystemLogDTO();
		systemLogDTO.setTitle("test");
		systemLogDTO.setRequestUri("1");
		systemLogDTO.setSource("1");
		systemLogDTO.setService("1");
		systemLogDTO.setRemoteIp("1");
		systemLogDTO.setMethod("1");
		systemLogDTO.setUserAgent("1");
		systemLogDTO.setParams("1");
		systemLogDTO.setTime(0L);
		systemLogDTO.setCode(200);
		systemLogDTO.setRequestUser("1");
		systemLogDTO.setUserType("1");
		systemLogDTO.setUserId(0L);
		systemLogDTO.setCreateTime(new Date());
		this.loginHistoryService.saveLoginHistory(systemLogDTO, 1);
		return R.ok();
	}
}
