/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账单分销佣金
 *
 * @author legendshop
 */
@Data
@Schema(description = "账单分销佣金")
public class ShopOrderBillDistributionDTO implements Serializable {

	private static final long serialVersionUID = 7886561681182406359L;

	@Schema(description = "订单项ID")
	private Long orderItemId;

	@Schema(description = "订单编号")
	private String orderNumber;

	@Schema(description = "商品图片")
	private String pic;

	@Schema(description = "商品名称")
	private String productName;

	@Schema(description = "产品动态属性")
	private String attribute;

	@Schema(description = "购买数量")
	private Integer basketCount;

	@Schema(description = "分销比例")
	private BigDecimal distRatio;

	@Schema(description = "分销佣金")
	private BigDecimal distCommissionCash;

	@Schema(description = "分销状态")
	private Integer commissionSettleStatus;

	@Schema(description = "售后状态")
	private Integer refundStatus;

	@Schema(description = "商品金额")
	private BigDecimal actualAmount;

	public ShopOrderBillDistributionDTO() {
		this.distCommissionCash = BigDecimal.ZERO;
	}
}
