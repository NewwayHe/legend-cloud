/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.shop.bo.PlateCapitalFlowAmountBO;
import com.legendshop.shop.dto.PlateCapitalFlowDTO;
import com.legendshop.shop.excel.PlateCapitalFlowExportDTO;
import com.legendshop.shop.query.PlateCapitalFlowQuery;
import com.legendshop.shop.service.PlateCapitalFlowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台资金流水(PlateCapitalFlow)表控制层
 *
 * @author legendshop
 * @since 2020-09-18 17:26:10
 */
@RestController
@RequestMapping(value = "/plate/capital/flow", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "平台资金流水")
public class AdminPlateCapitalFlowController {

	/**
	 * 平台资金流水(PlateCapitalFlow)服务对象
	 */
	@Autowired
	private PlateCapitalFlowService plateCapitalFlowService;

	@Operation(summary = "【后台】平台资金流水分页查询")
	@GetMapping("/page")
	public R<PageSupport<PlateCapitalFlowDTO>> page(PlateCapitalFlowQuery plateCapitalFlowQuery) {
		PageSupport<PlateCapitalFlowDTO> page = plateCapitalFlowService.queryPage(plateCapitalFlowQuery);
		return R.ok(page);
	}


	@Operation(summary = "【后台】平台总金额查询")
	@GetMapping("/amount")
	public R<PlateCapitalFlowAmountBO> getAmount() {
		return R.ok(plateCapitalFlowService.getPlateAmount());
	}


	@Operation(summary = "【后台】导出excel")
	@GetMapping("/export/excel")
	@ExportExcel(name = "平台资金流水", sheet = "平台资金流水")
	public List<PlateCapitalFlowExportDTO> export(PlateCapitalFlowQuery plateCapitalFlowQuery) {
		return plateCapitalFlowService.queryExportList(plateCapitalFlowQuery);
	}
}
