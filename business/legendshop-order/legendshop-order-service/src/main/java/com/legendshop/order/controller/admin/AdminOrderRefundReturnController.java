/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.order.bo.OrderRefundReturnBO;
import com.legendshop.order.dto.ConfirmRefundCanelListDTO;
import com.legendshop.order.dto.ConfirmRefundDTO;
import com.legendshop.order.dto.ConfirmRefundListDTO;
import com.legendshop.order.dto.OrderCancelReasonDTO;
import com.legendshop.order.excel.OrderCancelExportDTO;
import com.legendshop.order.excel.OrderRefundExportDTO;
import com.legendshop.order.query.OrderCancelReasonQuery;
import com.legendshop.order.query.OrderRefundReturnQuery;
import com.legendshop.order.service.OrderRefundReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台端售后控制器
 *
 * @author legendshop
 */
@Tag(name = "售后管理")
@RestController
@RequestMapping(value = "/admin/order/refund", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminOrderRefundReturnController {

	@Autowired
	private OrderRefundReturnService orderRefundReturnService;

	@PreAuthorize("@pms.hasPermission('admin_order_refund_page')")
	@Operation(summary = "【平台】售后分页列表", description = "")
	@GetMapping("/page")
	public R<PageSupport<OrderRefundReturnBO>> page(OrderRefundReturnQuery query) {
		return R.ok(orderRefundReturnService.page(query));
	}

	@GetMapping("/export")
	@ExportExcel(name = "售后订单", sheet = "售后记录")
	@Operation(summary = "【平台】售后记录导出", description = "")
	public List<OrderRefundExportDTO> export(OrderRefundReturnQuery query) {
		return this.orderRefundReturnService.export(query);
	}

	@PreAuthorize("@pms.hasPermission('admin_order_refund_get')")
	@Operation(summary = "【平台】查看售后详情", description = "")
	@Parameter(name = "refundId", description = "退款订单id", required = true)
	@GetMapping
	public R<OrderRefundReturnBO> get(@RequestParam Long refundId) {
		return R.ok(orderRefundReturnService.getAdminRefundDetail(refundId));
	}

	@PreAuthorize("@pms.hasPermission('admin_order_refund_confirmRefund')")
	@Operation(summary = "【平台】平台确认退款", description = "")
	@PostMapping("/confirm/refund")
	public R confirmRefund(@Valid @RequestBody ConfirmRefundDTO confirmRefundDTO) {
		return this.orderRefundReturnService.confirmRefund(confirmRefundDTO);
	}

	@PreAuthorize("@pms.hasPermission('admin_order_refund_confirmRefund_batch')")
	@Operation(summary = "【平台】平台批量确认退款", description = "")
	@PostMapping("/confirm/refundList")
	public R confirmRefundList(@Valid @RequestBody ConfirmRefundListDTO confirmRefundDTO) {
		return this.orderRefundReturnService.confirmRefundList(confirmRefundDTO);
	}

	@PreAuthorize("@pms.hasPermission('admin_order_refund_insertRemark')")
	@Operation(summary = "【平台】添加备注 admin_order_refund_insertRemark", description = "")
	@Parameters({
			@Parameter(name = "refundId", description = "退款id", required = true),
			@Parameter(name = "remark", description = "备注内容", required = true)
	})
	@PostMapping("/insert/remake")
	public R insertRemark(@RequestParam Long refundId, @RequestParam String remark) {
		if (orderRefundReturnService.insertRemark(refundId, remark)) {
			return R.ok();
		}
		return R.fail("操作失败");
	}

	@Operation(summary = "【平台】申请列表", description = "")
	@GetMapping("/pageCancelOrder")
	public R<PageSupport<OrderCancelReasonDTO>> papeCancelOrder(OrderCancelReasonQuery query) {
		return R.ok(orderRefundReturnService.pageCancelOrder(query));
	}

	@GetMapping("/excel/cancel")
	@ExportExcel(name = "商家申请列表", sheet = "申请列表导出")
	@Operation(summary = "商家申请列表导出", description = "")
	public List<OrderCancelExportDTO> getFlowExcelCancel(OrderCancelReasonQuery query) {
		return this.orderRefundReturnService.getFlowExcelCancel(query);
	}

	@Operation(summary = "【平台】平台批量审核取消订单", description = "")
	@PostMapping("/confirm/refund/cancelList")
	public R applyOrderCancelList(@RequestBody ConfirmRefundCanelListDTO confirmRefundDTO) {
		confirmRefundDTO.setAdminOperator(SecurityUtils.getAdminUser().getUsername());
		return this.orderRefundReturnService.confirmRefundCanelList(confirmRefundDTO);
	}

	@Operation(summary = "【平台】售后原因", description = "")
	@PostMapping("/afterSales/reason")
	public List<String> queryAfterSalesReason(Long applyType) {
		return orderRefundReturnService.queryAfterSalesReason(null, applyType);
	}

	@Operation(summary = "【商家】取消原因下拉框", description = "")
	@PostMapping("/cancel/reason")
	public R<List<SysParamItemDTO>> queryCancelReason() {
		return R.ok(orderRefundReturnService.queryCancelReason(SysParamNameEnum.ORDER_CANCEL_REASON.name()));
	}
}
