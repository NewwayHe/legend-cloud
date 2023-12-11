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
import com.legendshop.basic.dto.ExportExcelTaskDTO;
import com.legendshop.basic.query.ExportExcelTaskQuery;
import com.legendshop.basic.service.ExportExcelTaskService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 * @version 1.0.0
 * @title AdminExportExcelTaskController
 * @date 2022/4/26 15:59
 * @description：
 */
@Tag(name = "文件导出列表")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/export/excel")
public class AdminExportExcelTaskController {

	private final ExportExcelTaskService exportExcelTaskService;

	@GetMapping("/page")
	@Operation(summary = "【平台】文件导出分页查询")
	public R<PageSupport<ExportExcelTaskDTO>> page(ExportExcelTaskQuery query) {
		return exportExcelTaskService.page(query);
	}
}
