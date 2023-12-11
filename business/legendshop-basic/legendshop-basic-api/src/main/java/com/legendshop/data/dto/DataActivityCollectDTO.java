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
 * 营销活动汇总表(DataActivityCollect)DTO
 *
 * @author legendshop
 * @since 2021-06-30 20:35:39
 */
@Data
@Schema(description = "营销活动汇总表DTO")
public class DataActivityCollectDTO implements Serializable {

	private static final long serialVersionUID = -63975264034558715L;

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
	 * 成交金额
	 */
	@Schema(description = "成交金额")
	private BigDecimal dealAmount;

	/**
	 * 成交商品数
	 */
	@Schema(description = "成交商品数")
	private Integer dealNumber;

	@Schema(description = "成交用户数")
	private Integer dealUserNum;

	/**
	 * 成交新用户数
	 */
	@Schema(description = "成交新用户数")
	private Integer dealNewUser;

	/**
	 * 成交旧用户数
	 */
	@Schema(description = "成交旧用户数")
	private Integer dealOldUser;

	/**
	 * 访问人数
	 */
	@Schema(description = "访问人数")
	private Integer viewPeople;

	/**
	 * 访问次数
	 */
	@Schema(description = "访问次数")
	private Integer viewFrequency;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 活动状态
	 */
	@Schema(description = "活动状态 -2：拒绝  -1：失效 0:未完成 1：成功")
	private Integer status;

	/**
	 * 活动类型
	 */
	@Schema(description = "活动类型")
	private String type;

	@Schema(description = "店铺id")
	private Long shopId;

	@Schema(description = "审核状态  -1: 拒绝  0:待审核 1:通过")
	private Integer opStatus;

	@Schema(description = "活动状态")
	private Integer activityStatus;

}
