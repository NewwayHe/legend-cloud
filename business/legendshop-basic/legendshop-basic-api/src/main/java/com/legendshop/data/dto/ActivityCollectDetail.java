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
@Schema(description = "营销分析DTO")
public class ActivityCollectDetail {

	@Schema(description = "累计发布活动数")
	private Integer totalUsage;

	@Schema(description = "有效活动数")
	private Integer effectiveUsage;

	@Schema(description = "无效活动数")
	private Integer unEffectiveUsage;

	@Schema(description = "营销支付金额")
	private BigDecimal payAmount;

	@Schema(description = "营销支付金额占比")
	private BigDecimal payPercentage;

	@Schema(description = "连带率 付款件数/付款订单数")
	private BigDecimal rate;

}
