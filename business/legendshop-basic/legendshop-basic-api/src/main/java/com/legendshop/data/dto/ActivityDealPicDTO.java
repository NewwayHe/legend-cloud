/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "营销统计新增成交金额折线图DTO")
public class ActivityDealPicDTO {


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
	 * 时间
	 */
	@Schema(description = "时间")
	@JsonFormat(
			pattern = "yyyy-MM-dd",
			timezone = "GMT+8"
	)
	private Date time;

	public ActivityDealPicDTO() {
		this.cashAmount = BigDecimal.ZERO;
		this.discountAmount = BigDecimal.ZERO;
		this.couponAmount = BigDecimal.ZERO;
	}
}
