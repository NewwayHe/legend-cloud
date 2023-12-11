/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Builder
@Schema(description = "新增成交金额折线图")
public class MarketingDataDealLineDTO {


	/**
	 * 满减满折
	 */
	@Schema(description = "满减满折成交金额")
	private BigDecimal cashAmount;

	/**
	 * 限时折扣
	 */
	@Schema(description = "限时折扣成交金额")
	private BigDecimal discountAmount;

	/**
	 * 优惠卷
	 */
	@Schema(description = "优惠卷成交金额")
	private BigDecimal couponAmount;

	/**
	 * 红包
	 */
	@Schema(description = "红包成交金额")
	private BigDecimal platAmount;

	/**
	 * 时间
	 */
	@Schema(description = "时间")
	@JsonFormat(
			pattern = "yyyy-MM-dd",
			timezone = "GMT+8"
	)
	private Date time;
}
