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
import com.legendshop.basic.query.SystemLogQuery;
import com.legendshop.basic.service.SystemLogService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.SystemLogDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统日志记录
 *
 * @author legendshop
 */
@Tag(name = "系统日志记录")
@RestController
@RequestMapping(value = "/admin/system/log", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminSystemLogController {

	private final SystemLogService systemLogService;

	/**
	 * 分页查询日志记录
	 *
	 * @param systemLogQuery
	 * @return
	 */
	@GetMapping("/page")
	@Operation(summary = "【后台】分页查询日志记录")
	public R<PageSupport<SystemLogDTO>> page(SystemLogQuery systemLogQuery) {
		return R.ok(systemLogService.page(systemLogQuery));
	}

}
