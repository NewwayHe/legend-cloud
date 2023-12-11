/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.UserOperationLogDTO;
import com.legendshop.basic.query.UserOperationLogQuery;
import com.legendshop.basic.service.UserOperationLogService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户行为操作日志
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "用户行为操作信息")
@RequestMapping(value = "/operation/log")
public class UserOperationLogController {


	final UserOperationLogService userOperationLogService;


	@PostMapping
	@Operation(summary = "【平台】保存用户操作记录")
	public R<Void> operationLog(@RequestBody UserOperationLogDTO log) {
		String mobile = SecurityUtils.getUserMobile();
		log.setUserMobile(mobile);
		if (null == mobile) {
			log.setUserMobile("00000000000");
		}
		log.setUserName(SecurityUtils.getUserName());
		this.userOperationLogService.save(log);
		return R.ok();
	}

	/**
	 * 查询操作按钮类型
	 */
	@GetMapping(value = "/findOperationType")
	@Operation(summary = "【平台】查询操作类型")
	public R<List<UserOperationLogDTO>> findOperationType(@RequestParam(value = "type") String type) {
		return R.ok(this.userOperationLogService.findOperationType(type));
	}

	/**
	 * 分页查询用户操作记录
	 */
	@GetMapping(value = "/findDataPageList")
	@Operation(summary = "【平台】分页查询用户操作记录")
	public R<PageSupport<UserOperationLogDTO>> findDataPageList(UserOperationLogQuery query) {
		return R.ok(this.userOperationLogService.findDataPageList(query));
	}

}
