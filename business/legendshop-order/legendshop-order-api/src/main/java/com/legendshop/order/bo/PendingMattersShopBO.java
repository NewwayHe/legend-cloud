/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商家待处理事项数量BO")
@Builder
public class PendingMattersShopBO {

	/**
	 * 被举报商品
	 */
	@Schema(description = "被举报商品")
	private Integer reportedProductCount;

	/**
	 * 待发货订单
	 */
	@Schema(description = "待发货订单")
	private Integer pendingOrderCount;

	/**
	 * 待处理售后订单
	 */
	@Schema(description = "待处理售后订单")
	private Integer pendingAfterSalesOrderCount;

	/**
	 * 待开发票
	 */
	@Schema(description = "待开发票")
	private Integer toBeInvoicedOrderCount;
}
