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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityAnalysisDTO {

	@Schema(description = "商品销售总价格")
	private BigDecimal productTotalAmount;


	@Schema(description = "订单营销活动优惠后的价格")
	private BigDecimal actualAmount;

	@Schema(description = "成交的订单数")
	private Integer count;

	@Schema(description = "成交的商品数")
	private Integer totalCount;


}
