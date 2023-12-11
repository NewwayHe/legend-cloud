/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class OrderQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = -4382525558397306881L;

	@Schema(description = "用户Id")
	private Long userId;

	@Schema(description = "商家Id")
	private Long shopId;

	@Schema(description = "订单编码")
	private String orderNumber;

	@Schema(description = "商品名称")
	private String productName;

	@Schema(description = "商家名称")
	private String shopName;

	@Schema(description = "用户名称")
	private String userName;

	@Schema(description = "订单状态")
	private Integer status;

	@Schema(description = "订单类型")
	private Integer type;

	@Schema(description = "售后状态")
	private Integer afterSaleStatus;

	@Schema(description = "下单开始时间")
	private Date startTime;

	@Schema(description = "下单结束时间")
	private Date endTime;

	@Schema(description = "活动id")
	private Long activityId;
}
