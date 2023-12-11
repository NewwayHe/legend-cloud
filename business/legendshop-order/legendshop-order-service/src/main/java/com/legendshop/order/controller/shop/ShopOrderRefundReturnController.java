/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.shop;

import cn.hutool.core.lang.Assert;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.order.bo.OrderRefundReturnBO;
import com.legendshop.order.dto.ApplyOrderCancelDTO;
import com.legendshop.order.dto.OrderCancelReasonDTO;
import com.legendshop.order.excel.OrderCancelExportDTO;
import com.legendshop.order.excel.OrderRefundExportDTO;
import com.legendshop.order.query.OrderBulkRefundQuery;
import com.legendshop.order.query.OrderCancelReasonQuery;
import com.legendshop.order.query.OrderRefundReturnQuery;
import com.legendshop.order.service.OrderRefundReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商家端售后控制器
 *
 * @author legendshop
 */
@Tag(name = "售后管理")
@RestController
@RequestMapping(value = "/s/order/refund", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopOrderRefundReturnController {

	@Autowired
	private OrderRefundReturnService orderRefundReturnService;

	@Operation(summary = "【商家】售后分页列表", description = "")
	@GetMapping("/page")
	public R<PageSupport<OrderRefundReturnBO>> page(OrderRefundReturnQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(orderRefundReturnService.page(query));
	}

	@GetMapping("/export")
	@ExportExcel(name = "售后订单", sheet = "售后记录")
	@Operation(summary = "【商家】售后记录导出", description = "")
	public List<OrderRefundExportDTO> export(OrderRefundReturnQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return this.orderRefundReturnService.export(query);
	}

	@Operation(summary = "【商家】查看售后详情", description = "")
	@Parameter(name = "refundId", description = "退款订单id", required = true)
	@GetMapping
	public R<OrderRefundReturnBO> get(@RequestParam Long refundId) {
		return R.ok(orderRefundReturnService.getShopRefundDetail(refundId, SecurityUtils.getShopUser().getShopId()));
	}

	/**
	 * 审核退款订单
	 */
	@Operation(summary = "【商家】审核退款订单", description = "")
	@Parameters({
			@Parameter(name = "refundId", description = "退款编号", required = true),
			@Parameter(name = "auditFlag", description = "是否通过售后", required = true),
			@Parameter(name = "sellerMessage", description = "卖家备注", required = false),
	})
	@PostMapping("/audit/refund")
	public R auditRefund(@RequestParam Long refundId, @RequestParam Boolean auditFlag, String sellerMessage) {
		return R.process(orderRefundReturnService.auditRefund(refundId, auditFlag, sellerMessage, SecurityUtils.getShopUser().getShopId()), "审核失败");
	}

	@Operation(summary = "【商家】批量审核退款订单", description = "")
	@PostMapping("/batch/audit/refund")
	public R batchAuditRefund(@RequestBody OrderBulkRefundQuery orderBulkRefundQuery) {
		return R.process(orderRefundReturnService.batchAuditRefund(orderBulkRefundQuery), "审核失败");
	}

	@Operation(summary = "【商家】批量审核退货订单", description = "")
	@PostMapping("/batch/audit/refund/good")
	public R batchAuditRefundGood(@RequestBody OrderBulkRefundQuery orderBulkRefundQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.process(orderRefundReturnService.batchAuditRefundGood(shopId, orderBulkRefundQuery), "审核失败");
	}


	@Operation(summary = "【商家】审核退货订单", description = "")
	@Parameters({
			@Parameter(name = "refundId", description = "退款编号", required = true),
			@Parameter(name = "auditFlag", description = "是否通过售后", required = true),
			@Parameter(name = "abandonedGoodFlag", description = "是否弃货", required = true),
			@Parameter(name = "sellerMessage", description = "卖家备注", required = false)
	})
	@PostMapping("/audit/refund/good")
	public R auditRefundGood(@RequestParam Long refundId, @RequestParam Boolean auditFlag, @RequestParam Boolean abandonedGoodFlag, @RequestParam String sellerMessage) {
		Assert.isTrue(sellerMessage.length() <= 50, "审核意见字数过多!");
		return R.process(orderRefundReturnService.auditRefundGood(refundId, auditFlag, abandonedGoodFlag, sellerMessage, SecurityUtils.getShopUser().getShopId()), "审核失败");
	}


	@Operation(summary = "【商家】商家确认收货", description = "")
	@Parameter(name = "refundId", description = "售后订单ID", required = true)
	@PostMapping("/confirm/deliver")
	public R confirmDeliver(@RequestParam Long refundId) {
		if (orderRefundReturnService.confirmDeliver(refundId, SecurityUtils.getShopUser().getShopId())) {
			return R.ok();
		}
		return R.fail("确认收货失败");
	}

	@Operation(summary = "【商家】申请列表", description = "")
	@GetMapping("/pageCancelOrder")
	public R<PageSupport<OrderCancelReasonDTO>> papeCancelOrder(OrderCancelReasonQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(orderRefundReturnService.pageCancelOrder(query));
	}

	@Operation(summary = "【商家】取消原因下拉框", description = "")
	@PostMapping("/cancel/reason")
	public R<List<SysParamItemDTO>> queryCancelReason() {
		return R.ok(orderRefundReturnService.queryCancelReason(SysParamNameEnum.ORDER_CANCEL_REASON.name()));
	}

	@GetMapping("/excel/cancel")
	@ExportExcel(name = "商家申请列表", sheet = "申请列表导出")
	@Operation(summary = "商家申请列表导出", description = "")
	public List<OrderCancelExportDTO> getFlowExcelCancel(OrderCancelReasonQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return this.orderRefundReturnService.getFlowExcelCancel(query);
	}

	@Operation(summary = "【商家】申请申请列表撤回", description = "")
	@PostMapping("/audit/withdraw/good")
	public R<String> auditWithdrawGood(@RequestBody List<Long> refundIds) {
		return orderRefundReturnService.auditWithdrawGood(refundIds, SecurityUtils.getShopUser().getShopId());
	}

	@Operation(summary = "【商家】申请取消订单项", description = "")
	@PostMapping("/apply/cancel")
	public R<String> applyOrderCancel(@Validated @RequestBody ApplyOrderCancelDTO applyOrderCancelDTO) {
		applyOrderCancelDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		applyOrderCancelDTO.setOperator(SecurityUtils.getShopUser().getUsername());
		return orderRefundReturnService.applyCancelOrderRefund(applyOrderCancelDTO);
	}

	@Operation(summary = "【商家】售后原因", description = "")
	@PostMapping("/afterSales/reason")
	public List<String> queryAfterSalesReason(Long applyType) {
		return orderRefundReturnService.queryAfterSalesReason(SecurityUtils.getShopUser().getShopId(), applyType);
	}
}
