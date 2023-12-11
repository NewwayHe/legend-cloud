/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.order.bo.OrderRefundReturnBO;
import com.legendshop.order.dto.*;
import com.legendshop.order.query.OrderRefundReturnQuery;
import com.legendshop.order.service.LogisticsCompanyService;
import com.legendshop.order.service.OrderRefundReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端售后控制器
 *
 * @author legendshop
 */
@Tag(name = "售后管理")
@RestController
@RequestMapping(value = "/p/order/refund", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class UserOrderRefundReturnController {

	private final OrderRefundReturnService orderRefundReturnService;
	private final LogisticsCompanyService logisticsCompanyService;


	@Operation(summary = "【用户】售后分页列表", description = "")
	@GetMapping("/page")
	public R<PageSupport<OrderRefundReturnBO>> page(OrderRefundReturnQuery query) {
		query.setUserId(SecurityUtils.getUserId());
		return R.ok(orderRefundReturnService.pageOrderRefundUser(query));
	}


	@Operation(summary = "【用户】查看售后详情", description = "")
	@Parameter(name = "refundId", description = "退款订单id", required = true)
	@GetMapping
	public R<OrderRefundReturnBO> get(@RequestParam Long refundId) {
		return R.ok(orderRefundReturnService.getUserRefundDetail(refundId, SecurityUtils.getUserId()));
	}


	@Operation(summary = "【用户】申请全额退款", description = "")
	@Parameters({
			@Parameter(name = "orderId", description = "订单id", required = true),
			@Parameter(name = "reason", description = "退款原因", required = true),
			@Parameter(name = "buyerMessage", description = "退款说明", required = false)
	})
	@PostMapping("/apply/order")
	public R applyOrder(@Valid ApplyOrderRefundDTO applyOrderRefundDTO) {
		applyOrderRefundDTO.setUserId(SecurityUtils.getUserId());
		if (orderRefundReturnService.applyOrderRefund(applyOrderRefundDTO)) {
			return R.ok();
		}
		return R.fail();
	}

	/**
	 * 订单项退款一次只能退一单
	 * TODO 增加一个退货数量，还是只能退一次
	 */
	@Operation(summary = "【用户】申请订单项退款", description = "")
	@Parameter(name = "refundAmount", description = "退款金额", required = true)
	@PostMapping("/apply/orderItem")
	public R<String> applyOrder(@Valid @RequestBody ApplyOrderItemRefundDTO applyOrderItemRefundDTO) {
		applyOrderItemRefundDTO.setUserId(SecurityUtils.getUserId());
		return orderRefundReturnService.applyOrder(applyOrderItemRefundDTO);
	}

	/**
	 * 撤销售后申请
	 */
	@Operation(summary = "【用户】撤销售后申请", description = "")
	@Parameter(name = "refundSn", description = "退款编号", required = true)
	@PostMapping("/cancel/apply")
	public R cancelApply(@RequestParam String refundSn) {
		return R.ok(orderRefundReturnService.cancelApply(refundSn, SecurityUtils.getUserId()));
	}


	@Operation(summary = "【用户】确认发货", description = "")
	@Parameters({
			@Parameter(name = "refundId", description = "退款订单id", required = true),
			@Parameter(name = "logisticsNumber", description = "物流单号", required = true),
			@Parameter(name = "company", description = "物流公司", required = true)
	})


	@PostMapping("/confirm/ship")
	public R confirmShip(@RequestBody UserReturnReturnLogisticsDTO userReturnReturnLogisticsDTO) {
		userReturnReturnLogisticsDTO.setUserId(SecurityUtils.getUserId());
		orderRefundReturnService.confirmShip(userReturnReturnLogisticsDTO);
		return R.ok();
	}


	/**
	 * 物流公司列表
	 *
	 * @return
	 */
	@Operation(summary = "【用户】物流公司列表", description = "")
	@GetMapping("/logistics")
	public R<List<LogisticsCompanyDTO>> logisticsPage() {
		return R.ok(logisticsCompanyService.getList(-1L));
	}


	/**
	 * 申请退款
	 *
	 * @param orderId 订单ID
	 * @return
	 */
	@Operation(summary = "申请退款", description = "跳转到 '订单申请退款退货'页面")
	@Parameter(name = "orderId", description = "订单id", required = true)
	@PostMapping(value = "/apply")
	public R<ApplyRefundReturnDTO> applyRefund(@RequestParam("orderId") Long orderId, Long orderItemId) {
		try {
			Long userId = SecurityUtils.getUserId();
			return orderRefundReturnService.getApplyRefundReturn(orderId, orderItemId, userId);
		} catch (Exception e) {
			log.error("订单退款异常!", e);
			return R.fail("系统异常");
		}
	}

	/**
	 * 获取退款原因
	 *
	 * @return
	 */
	@PostMapping(value = "/refundReason")
	@Operation(summary = "获取退款原因", description = "获取退款原因")
	public R<List<String>> refundReason() {
		try {
			if (null == SecurityUtils.getUserId()) {
				return R.fail("请登录");
			}
			return R.ok(orderRefundReturnService.getRefundReason());
		} catch (Exception e) {
			log.error("订单退款异常!", e);
			return R.fail();
		}
	}

	@Operation(summary = "【用户】取消原因下拉框", description = "")
	@PostMapping("/cancel/reason")
	public R<List<SysParamItemDTO>> queryCancelReason() {
		return R.ok(orderRefundReturnService.queryCancelReason(SysParamNameEnum.ORDER_CANCEL_REASON.name()));
	}
}
