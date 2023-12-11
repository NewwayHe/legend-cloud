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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "营销投入产出额和成交数据报表参数")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityCouponCountQuery implements Serializable {

	/**
	 * 开始日期
	 */
	@Schema(description = "开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date beginTime;

	/**
	 * 结束日期
	 */
	@Schema(description = "结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date endTime;

	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 渠道来源
	 */
	@Schema(description = "渠道来源")
	private String source;

	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private Long couponId;

}
