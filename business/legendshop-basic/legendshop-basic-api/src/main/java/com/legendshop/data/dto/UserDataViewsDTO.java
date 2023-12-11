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

/**
 * 用户浏览总数DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "用户浏览总数DTO")
public class UserDataViewsDTO implements Serializable {

	private static final long serialVersionUID = -5552202010259579150L;

	/**
	 * 用户浏览总数
	 */
	@Schema(description = "用户浏览总数")
	private Integer userViews;

	/**
	 * 用户浏览数日增量
	 */
	@Schema(description = "用户浏览数日增量")
	private Integer userViewsByDay;

	/**
	 * 用户浏览数周增量
	 */
	@Schema(description = "用户浏览数周增量")
	private Integer userViewsByWeek;

	/**
	 * 用户浏览数月增量
	 */
	@Schema(description = "用户浏览数月增量")
	private Integer userViewsByMonth;

	public UserDataViewsDTO() {
		this.userViews = 0;
		this.userViewsByDay = 0;
		this.userViewsByWeek = 0;
		this.userViewsByMonth = 0;
	}
}
