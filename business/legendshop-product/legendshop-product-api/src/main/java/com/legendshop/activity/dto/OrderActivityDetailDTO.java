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
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单活动详情
 *
 * @author legendshop
 */
@Data
@Schema(description = "订单活动详情")
public class OrderActivityDetailDTO implements Serializable {

	private static final long serialVersionUID = 3015883989425948909L;

	@Schema(description = "活动名")
	private String name;

	@Schema(description = "活动开始时间")
	private Date startTime;

	@Schema(description = "活动结束时间")
	private Date endTime;

	@Schema(description = "活动状态 0:未开始 1：进行中 2：暂停 10：活动结束 15：失效")
	private Integer status;

	@Schema(description = "活动价格")
	private BigDecimal activityPrice;

	@Schema(description = "商品价格")
	private BigDecimal price;
}
