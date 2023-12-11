/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/s/logistics/company", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "物流公司管理")
public class ShopLogisticsCompanyController {

	@Resource
	private LogisticsCompanyService logisticsCompanyService;

	@Operation(summary = "【商家】物流公司列表", description = "")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('s_logistics_company_page')")
	public R<PageSupport<LogisticsCompanyDTO>> queryPage(LogisticsCompanyQuery logisticsCompanyQuery) {
		logisticsCompanyQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		PageSupport<LogisticsCompanyDTO> page = logisticsCompanyService.queryPage(logisticsCompanyQuery);
		return R.ok(page);
	}


	@Operation(summary = "【商家】获取全部物流公司列表", description = "")
	@GetMapping("/all/list")
	@PreAuthorize("@pms.hasPermission('s_logistics_company_all_list')")
	public R<List<LogisticsCompanyDTO>> queryAll() {
		List<LogisticsCompanyDTO> list = logisticsCompanyService.queryAll(SecurityUtils.getShopUser().getShopId());
		return R.ok(list);
	}

	@Operation(summary = "【商家】新增物流公司查询的列表", description = "")
	@GetMapping("/add/page")
	@PreAuthorize("@pms.hasPermission('s_logistics_company_queryPlatePage')")
	public R<PageSupport<LogisticsCompanyDTO>> queryPlatePage(LogisticsCompanyQuery logisticsCompanyQuery) {
		logisticsCompanyQuery.setShopId(-1L);
		PageSupport<LogisticsCompanyDTO> page = logisticsCompanyService.queryPage(logisticsCompanyQuery);
		List<LogisticsCompanyDTO> resultList = page.getResultList();
		//获取商家已选的物流公司
		List<LogisticsCompanyDTO> list = logisticsCompanyService.getList(SecurityUtils.getShopUser().getShopId());
		//parentIds即平台维护的物流公司的主键
		List<Long> parentIds = list.stream().map(LogisticsCompanyDTO::getParentId).collect(Collectors.toList());
		resultList.forEach(l -> {
			l.setAllowFlag(true);
			if (parentIds.contains(l.getId())) {
				l.setAllowFlag(false);
			}
		});
		return R.ok(page);
	}

	@Operation(summary = "【商家】保存物流公司", description = "")
	@PostMapping
	@SystemLog(value = "保存物流公司")
	@PreAuthorize("@pms.hasPermission('admin_logistics_company_save')")
	public R save(@Valid @RequestBody List<LogisticsCompanyDTO> logisticsCompanyDTOs) {
		logisticsCompanyService.batchAddLogisticsCompanyDTO(logisticsCompanyDTOs, SecurityUtils.getShopUser().getShopId());
		return R.ok();
	}

	@Operation(summary = "【商家】删除物流公司", description = "")
	@Parameter(name = "id", description = "id", required = true)
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_logistics_company_del')")
	public R del(@PathVariable Long id) {
		logisticsCompanyService.deleteById(id);
		return R.ok();
	}
}
