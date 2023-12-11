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
import java.util.Date;

/**
 * (DataActivityView)DTO
 *
 * @author legendshop
 * @since 2021-07-07 16:14:44
 */
@Data
@Schema(description = "活动浏览记录DTO")
public class DataActivityViewDTO implements Serializable {

	private static final long serialVersionUID = 204798721202911241L;

	/**
	 * id
	 */
	@Schema(description = "id")
	private Long id;

	/**
	 * 活动id
	 */
	@Schema(description = "活动id")
	private Long activityId;

	@Schema(description = "活动类型")
	private String type;

	@Schema(description = "创建时间")
	private Date createTime;

	@Schema(description = "用户id")
	private Long userId;

}
