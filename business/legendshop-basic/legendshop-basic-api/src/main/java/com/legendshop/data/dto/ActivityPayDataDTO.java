/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@Schema(description = "营销活动支付数据")
public class ActivityPayDataDTO {

	@Schema(description = "实际支付金额")
	private BigDecimal actualTotalPrice;

	@Schema(description = "原价格")
	private BigDecimal totalPrice;

	@Schema(description = "支付商品数量")
	private Integer productQuantity;

	@Schema(description = "支付订单数量")
	private Integer orderNum;

}
