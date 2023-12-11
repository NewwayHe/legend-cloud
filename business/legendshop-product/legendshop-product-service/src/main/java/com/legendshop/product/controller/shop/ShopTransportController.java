/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.TransportBO;
import com.legendshop.product.dto.TransFeeCalculateDTO;
import com.legendshop.product.dto.TransportDTO;
import com.legendshop.product.query.TransportQuery;
import com.legendshop.product.service.TransFeeCalculateService;
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
@RequestMapping(value = "/s/transport", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopTransportController {

	@Autowired
	private TransportService transportService;

	@Autowired
	private TransFeeCalculateService transFeeCalculateService;

	@GetMapping("/{id}")
	@Parameter(name = "id", description = "运费模板ID", required = true)
	@Operation(summary = "【商家】运费模板详情")
	public R<TransportBO> getById(@PathVariable Long id) {
		return R.ok(transportService.getTransport(id));
	}

	@SystemLog(type = SystemLog.LogType.SHOP, value = "添加运费模板")
	@PostMapping
	@Operation(summary = "【商家】添加运费模板")
	public R save(@Valid @RequestBody TransportDTO transportDTO) {
		transportDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		transportService.saveTransport(transportDTO);
		return R.ok();
	}

	@SystemLog(type = SystemLog.LogType.SHOP, value = "修改运费模板")
	@PutMapping
	@Operation(summary = "【商家】修改运费模板")
	public R update(@Valid @RequestBody TransportDTO transportDTO) {
		transportDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		transportService.updateTransport(transportDTO);
		return R.ok();
	}

	@DeleteMapping("/{id}")
	@Parameter(name = "id", description = "运费模板ID", required = true)
	@Operation(summary = "【商家】删除运费模板")
	public R del(@PathVariable Long id) {
		return R.ok(transportService.deleteTransport(id));
	}


	@GetMapping("/page")
	@Operation(summary = "【商家】运费模板分页查询")
	public R<PageSupport<TransportBO>> page(TransportQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(transportService.queryTransportPage(query));
	}

	@GetMapping("/choose/page")
	@Operation(summary = "【商家】商品发布选择运费模板")
	public R<PageSupport<TransportBO>> pageChooseTransport(TransportQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(transportService.queryTransportChoosePage(query));
	}


	@PostMapping("/test/trans/fee")
	@Operation(summary = "【商家】测试运费计算")
	public R testTransFee(@Valid @RequestBody TransFeeCalculateDTO transFeeCalculateDTO) {
		transFeeCalculateDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		return transFeeCalculateService.calTransFee(transFeeCalculateDTO);
	}
}
