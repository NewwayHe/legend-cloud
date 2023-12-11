/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "后台首页未审核营销活动BO")
public class ActivityIndexBO implements Serializable {

	private static final long serialVersionUID = -82122133808447056L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;


	/**
	 * 活动类型
	 */
	@Schema(description = "活动类型")
	private String activityType;

	/**
	 * 活动类型名称
	 */
	@Schema(description = "活动类型名称")
	private String activityTypeName;

	/**
	 * 活动名称
	 */
	@Schema(description = "活动名称")
	private String activityName;

	/**
	 * 活动开始时间
	 */
	@Schema(description = "活动开始时间")
	private Date startTime;

	/**
	 * 活动结束时间
	 */
	@Schema(description = "活动结束时间")
	private Date endTime;

}
