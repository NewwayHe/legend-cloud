/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.product.bo.TransportBO;
import com.legendshop.product.dto.TransportDTO;
import com.legendshop.product.query.TransportQuery;
import com.legendshop.product.service.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author legendshop
 */
@Tag(name = "运费模板")
@RestController
@RequestMapping(value = "/admin/transport", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminTransportController {

	@Autowired
	private TransportService transportService;

	@GetMapping("/{id}")
	@Parameter(name = "id", description = "运费模板ID", required = true)
	@Operation(summary = "【后台】运费模板详情")
	public R<TransportBO> getById(@PathVariable Long id) {
		return R.ok(transportService.getTransport(id));
	}

	@PostMapping
	@SystemLog(type = SystemLog.LogType.SHOP, value = "添加运费模板")
	@Operation(summary = "【后台】添加运费模板")
	public R save(@Valid @RequestBody TransportDTO transportDTO) {
		transportDTO.setShopId(-1L);
		transportService.saveTransport(transportDTO);
		return R.ok();
	}

	@PutMapping
	@SystemLog(type = SystemLog.LogType.SHOP, value = "修改运费模板")
	@Operation(summary = "【后台】修改运费模板")
	public R update(@Valid @RequestBody TransportDTO transportDTO) {
		transportDTO.setShopId(-1L);
		transportService.updateTransport(transportDTO);
		return R.ok();
	}

	@DeleteMapping("/{id}")
	@Parameter(name = "id", description = "运费模板ID", required = true)
	@Operation(summary = "【后台】删除运费模板")
	public R del(@PathVariable Long id) {
		return R.ok(transportService.deleteTransport(id));
	}


	@GetMapping("/page")
	@Operation(summary = "【后台】运费模板分页查询")
	public R<PageSupport<TransportBO>> page(TransportQuery query) {
		return R.ok(transportService.queryTransportPage(query));
	}
}
