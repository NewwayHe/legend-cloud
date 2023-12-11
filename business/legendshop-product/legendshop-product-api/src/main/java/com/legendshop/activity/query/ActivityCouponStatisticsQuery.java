/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.query;

import cn.legendshop.jpaplus.support.PageParams;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "营销报表 优惠券列表参数")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityCouponStatisticsQuery extends PageParams {
	/**
	 * 领取开始日期
	 */
	@Schema(description = "领取开始日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date receiveStartTime;

	/**
	 * 领取结束日期
	 */
	@Schema(description = "领取结束日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date receiveEndTime;

	/**
	 * 礼券提供方是否为店铺
	 */
	@Schema(description = "优惠券提供方是否为店铺")
	Boolean shopProviderFlag;


	/**
	 * 排序的字段
	 */
	@Schema(description = "排序的字段，把要排序的字段名返回过来")
	private String prop;

	/**
	 * 排序的方向：asc 或者 desc
	 */
	@Schema(description = "排序的方向：asc 或者 desc")
	private String order;
}
