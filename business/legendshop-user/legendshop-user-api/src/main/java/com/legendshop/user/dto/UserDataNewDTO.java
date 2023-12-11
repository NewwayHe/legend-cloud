/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新增用户数量DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "新增用户数量DTO")
public class UserDataNewDTO {

	/**
	 * 新增用户总数
	 */
	@Schema(description = "新增用户总数")
	private Integer newUser;

	/**
	 * 昨日新增用户数
	 */
	@Schema(description = "昨日新增用户数")
	private Integer newUserByDay;

	/**
	 * 一周内新增用户数
	 */
	@Schema(description = "一周内新增用户数")
	private Integer newUserByWeek;

	/**
	 * 一月内新增用户数
	 */
	@Schema(description = "一月内新增用户数")
	private Integer newUserByMonth;

}
