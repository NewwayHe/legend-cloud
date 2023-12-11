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

/**
 * @author legendshop
 */
@Data
@Schema(description = "确认订单商品DTO")
public class ConfirmOrderItemDTO implements Serializable {

	private static final long serialVersionUID = 2026693213777057773L;


	@Schema(description = "skuId", required = true)
	private Long skuId;


	@Schema(description = "商品数量", required = true)
	private Integer count;


	@Schema(description = "购物车选择的促销活动ID(普通订单立即购买以及其他订单类型不用传)")
	private Long marketId;

}
