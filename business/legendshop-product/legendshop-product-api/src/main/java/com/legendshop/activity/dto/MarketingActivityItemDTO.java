/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 促销活动DTO
 *
 * @author legendshop
 */
@Data
public class MarketingActivityItemDTO implements Serializable {

	private static final long serialVersionUID = 6072061535711784628L;
	/**
	 * 必填入参
	 */
	@Schema(description = "商家Id", required = true)
	private Long shopId;

	@Schema(description = "SkuId", required = true)
	private Long skuId;

	@Schema(description = "sku单品销售价", required = true)
	private BigDecimal price;

	@Schema(description = "skuItem购买数量", required = true)
	private Integer totalCount;

	@Schema(description = "item销售价，sku单品*购买数量", required = true)
	private BigDecimal totalPrice;

	@Schema(description = "可用优惠券列表", required = true)
	private List<CouponDTO> couponDTOList;

	/**
	 * 选中状态
	 */
	@Schema(description = "选中状态")
	private Boolean selectFlag;

	public MarketingActivityItemDTO() {
		this.selectFlag = true;
	}
}
