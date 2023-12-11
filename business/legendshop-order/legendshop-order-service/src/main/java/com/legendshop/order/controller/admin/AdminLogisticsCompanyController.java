/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.admin;

import cn.hutool.core.lang.Assert;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.order.dto.LogisticsCompanyDTO;
import com.legendshop.order.query.LogisticsCompanyQuery;
import com.legendshop.order.service.LogisticsCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/admin/logistics/company", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "物流公司管理")
public class AdminLogisticsCompanyController {


	@Resource
	private LogisticsCompanyService logisticsCompanyService;

	@Operation(summary = "【后台】物流公司列表", description = "")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin_logistics_company_page')")
	public R<PageSupport<LogisticsCompanyDTO>> queryPage(LogisticsCompanyQuery logisticsCompanyQuery) {
		logisticsCompanyQuery.setShopId(-1L);
		PageSupport<LogisticsCompanyDTO> page = logisticsCompanyService.queryPage(logisticsCompanyQuery);
		return R.ok(page);
	}


	@Operation(summary = "【后台】保存物流公司", description = "")
	@PostMapping
	@SystemLog(value = "保存物流公司")
	@PreAuthorize("@pms.hasPermission('admin_logistics_company_save')")
	public R save(@Valid @RequestBody LogisticsCompanyDTO logisticsCompanyDTO) {
		logisticsCompanyDTO.setShopId(-1L);
		Long save = logisticsCompanyService.save(logisticsCompanyDTO);
		Assert.isTrue(save > 0, "保存失败");
		return R.ok();
	}

	@Operation(summary = "【后台】修改物流公司", description = "")
	@PutMapping
	@SystemLog(value = "修改物流公司")
	@PreAuthorize("@pms.hasPermission('admin_logistics_company_update')")
	public R update(@Valid @RequestBody LogisticsCompanyDTO logisticsCompanyDTO) {
		Assert.notNull(logisticsCompanyDTO.getId(), "id不能为空");
		logisticsCompanyService.update(logisticsCompanyDTO);
		return R.ok();
	}

	@Operation(summary = "【后台】获取物流公司", description = "")
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin_logistics_company_get')")
	public R<LogisticsCompanyDTO> getById(@PathVariable Long id) {
		LogisticsCompanyDTO logisticsCompanyDTO = logisticsCompanyService.getById(id);
		return R.ok(logisticsCompanyDTO);
	}


	@Operation(summary = "【后台】删除物流公司", description = "")
	@Parameter(name = "id", description = "id", required = true)
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin_logistics_company_del')")
	public R del(@PathVariable Long id) {
		logisticsCompanyService.deleteById(id);
		return R.ok();
	}
}
