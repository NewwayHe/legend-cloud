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
import com.legendshop.basic.query.OperatorLogQuery;
import com.legendshop.basic.service.OperatorLogService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.OperatorLogDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志表(OperatorLog)表控制层
 *
 * @author legendshop
 * @since 2023-08-30 11:09:23
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "操作日志表管理")
@RequestMapping(value = "/operatorLog", produces = MediaType.APPLICATION_JSON_VALUE)
public class OperatorLogController {

	/**
	 * 操作日志表(OperatorLog)服务对象
	 */
	private final OperatorLogService operatorLogService;

	/**
	 * 根据id查询操作日志表
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@Operation(summary = "根据id查询操作日志表")
	public R<OperatorLogDTO> getById(@PathVariable("id") Long id) {
		return operatorLogService.getById(id);
	}

	@PostMapping("/page")
	@Operation(summary = "分页查询操作日志表")
	public R<PageSupport<OperatorLogDTO>> page(@RequestBody OperatorLogQuery operatorLogQuery) {
		return operatorLogService.page(operatorLogQuery);
	}

}
