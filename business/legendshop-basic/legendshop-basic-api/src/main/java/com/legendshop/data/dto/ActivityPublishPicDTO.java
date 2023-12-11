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

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "营销统计新增使用次数折线图DTO")
public class ActivityPublishPicDTO {

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
	 * 时间
	 */
	@Schema(description = "时间")
	@JsonFormat(
			pattern = "yyyy/MM/dd",
			timezone = "GMT+8"
	)
	private Date time;

	public ActivityPublishPicDTO() {
		this.cashNum = 0;
		this.discountNum = 0;
		this.couponNum = 0;
	}

}
