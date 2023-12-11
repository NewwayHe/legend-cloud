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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动订单表(DataActivityOrder)DTO
 *
 * @author legendshop
 * @since 2021-07-16 14:50:15
 */
@Data
@Schema(description = "活动订单表DTO")
public class DataActivityOrderDTO implements Serializable {

	private static final long serialVersionUID = -67598900174433588L;

	/**
	 * ID
	 */
	@Schema(description = "ID")
	private Long id;

	/**
	 * 活动id
	 */
	@Schema(description = "活动id")
	private Long activityId;

	/**
	 * 订单项id
	 */
	@Schema(description = "订单项id")
	private Long orderItemId;

	/**
	 * 订单id
	 */
	@Schema(description = "订单id")
	private Long orderId;

	/**
	 * 实付金额
	 */
	@Schema(description = "实付金额")
	private BigDecimal amount;

	/**
	 * 商品数量
	 */
	@Schema(description = "商品数量")
	private Integer basketCount;

	/**
	 * 活动类型
	 */
	@Schema(description = "活动类型")
	private String activityType;

	@Schema(description = "用户id")
	private Long userId;

	@Schema(description = "订单创建时间")
	private Date createTime;

}
