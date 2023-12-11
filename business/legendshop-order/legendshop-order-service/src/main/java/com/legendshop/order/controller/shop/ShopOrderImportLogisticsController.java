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
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.security.dto.ShopUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.order.dto.OrderImportLogisticsDTO;
import com.legendshop.order.dto.WaitDeliveryOrderDTO;
import com.legendshop.order.excel.OrderImportLogisticsExportDTO;
import com.legendshop.order.query.OrderImportLogisticsQuery;
import com.legendshop.order.service.OrderImportLogisticsDetailService;
import com.legendshop.order.service.OrderImportLogisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * (OrderImportLogistics)表控制层
 *
 * @author legendshop
 * @since 2022-04-27 10:30:08
 */
@RestController
@RequestMapping(value = "/s/order/import/logistics", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "导入物流信息")
public class ShopOrderImportLogisticsController {


	private OrderImportLogisticsService importLogisticsService;

	private OrderImportLogisticsDetailService importLogisticsDetailService;

	@GetMapping("/page")
	@Operation(summary = "【商家】商家订单批量发货查询", description = "")
	public R<PageSupport<OrderImportLogisticsDTO>> page(OrderImportLogisticsQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(importLogisticsService.page(query));
	}

	@GetMapping("/template")
	@ExportExcel(name = "待发货订单列表", sheet = "待发货订单列表")
	@Operation(summary = "【商家】下载物流导入模板", description = "")
	public List<WaitDeliveryOrderDTO> template() {
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		return this.importLogisticsService.template(shopUser.getShopId());
	}

	@GetMapping("/export/detail/{importId}")
	@Operation(summary = "【商家】导入详情数据导出", description = "")
	@ExportExcel(name = "物流详情数据导出", sheet = "导入物流详情数据导出")
	public List<OrderImportLogisticsExportDTO> orderExport(@PathVariable Long importId) {
		return importLogisticsDetailService.exportByImportId(importId);
	}
}
