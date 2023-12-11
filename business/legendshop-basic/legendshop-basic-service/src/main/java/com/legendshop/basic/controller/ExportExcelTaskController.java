/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import com.legendshop.basic.service.ExportExcelTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ExportExcelTaskController
 * @date 2022/4/26 16:15
 * @description：
 */
@Slf4j
@Tag(name = "文件导出列表")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/export/excel")
public class ExportExcelTaskController {

	private final ExportExcelTaskService exportExcelTaskService;

	@GetMapping("/download")
	@Operation(summary = "【公共】文件下载")
	public void download(HttpServletResponse servletResponse, @RequestParam(value = "exportId") Long exportId) {
		exportExcelTaskService.download(servletResponse, exportId);
	}
}
