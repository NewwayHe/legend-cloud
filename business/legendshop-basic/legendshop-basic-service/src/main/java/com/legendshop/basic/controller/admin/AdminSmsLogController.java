/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.admin;


import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SmsLogDTO;
import com.legendshop.basic.query.SmsLogQuery;
import com.legendshop.basic.service.SmsLogService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@Tag(name = "短信日志")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin/smsLog", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminSmsLogController {

	final SmsLogService smsLogService;


	@Operation(summary = "【后台】获取短信日志")
	@GetMapping("/page")
	public R<PageSupport<SmsLogDTO>> query(SmsLogQuery query) {
		return R.ok(smsLogService.getSmsLogPage(query));
	}
}
