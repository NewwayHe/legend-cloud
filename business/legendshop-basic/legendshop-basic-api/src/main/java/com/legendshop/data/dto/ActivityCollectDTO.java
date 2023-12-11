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
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "营销活动汇总DTO")
public class ActivityCollectDTO {

	@Schema(description = "活动id")
	private Long activityId;

	@Schema(description = "活动创建时间")
	private Date createTime;

	@Schema(description = "活动开始时间")
	private Date startTime;

	@Schema(description = "活动结束时间")
	private Date endTime;

	@Schema(description = "活动状态")
	private Integer activityStatus;

	@Schema(description = "活动审核状态")
	private Integer opStatus;

	@Schema(description = "团购是否成团")
	private Integer success;

	@Schema(description = "实付款")
	private BigDecimal actualTotalPrice;

	@Schema(description = "订单状态")
	private Integer orderStatus;

	@Schema(description = "售后状态")
	private Integer refundStatus;

	@Schema(description = "售后截止时间")
	private Date finalReturnDeadline;

	@Schema(description = "商品原价")
	private BigDecimal price;

	@Schema(description = "商品数量")
	private Integer productQuantity;

	@Schema(description = "用户id")
	private Long userId;

	@Schema(description = "拼团编号")
	private String groupNumber;

	@Schema(description = "礼券提供方是否为店铺")
	private Integer shopProviderFlag;

	@Schema(description = "限时折扣减免金额")
	private BigDecimal limitDiscountsMarketingPrice;

	@Schema(description = "满减满折减免金额")
	private BigDecimal rewardMarketingPrice;

	@Schema(description = "满减满折类型")
	private Integer marketingType;

	@Schema(description = "订单时间")
	private Date orderTime;

	@Schema(description = "店铺id")
	private Long shopId;

	@Schema(description = "订单项id")
	private Long orderItemId;

	@Schema(description = "订单id")
	private Long orderId;

	@Schema(description = "订单项商品数量")
	private Integer basketCount;
}
