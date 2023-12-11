/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import com.legendshop.basic.dto.SmsSendParamDTO;
import com.legendshop.basic.service.SmsSendService;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/smsSend")
public class SmsSendController {

	final SmsSendService smsSendService;

	@PostMapping(value = "/sendSms")
	public R<String> sendSms(@RequestBody SmsSendParamDTO sendParamDTO) {
		return this.smsSendService.sendSms(sendParamDTO);
	}
}
