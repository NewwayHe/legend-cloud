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

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Builder
@Schema(description = "新增使用次数折线图")
public class MarketingDataViewLineDTO {


	/**
	 * 满减满折
	 */
	@Schema(description = "满减满折使用次数")
	private Integer cashNum;

	/**
	 * 限时折扣
	 */
	@Schema(description = "限时折扣使用次数")
	private Integer discountNum;

	/**
	 * 优惠卷
	 */
	@Schema(description = "优惠卷使用次数")
	private Integer couponNum;

	/**
	 * 红包
	 */
	@Schema(description = "红包使用次数")
	private Integer platNum;

	/**
	 * 时间
	 */
	@Schema(description = "时间")
	@JsonFormat(
			pattern = "yyyy/MM/dd",
			timezone = "GMT+8"
	)
	private Date time;


}
