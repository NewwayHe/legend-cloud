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
 * 消费用户DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "消费用户DTO")
public class UserDataConsumerDTO {

	/**
	 * 消费用户总数
	 */
	@Schema(description = "消费用户总数")
	private Integer consumerUser;

	/**
	 * 消费用户日增量
	 */
	@Schema(description = "消费用户日增量")
	private Integer consumerUserByDay;

	/**
	 * 消费用户周增量
	 */
	@Schema(description = "消费用户周增量")
	private Integer consumerUserByWeek;

	/**
	 * 消费用户月增量
	 */
	@Schema(description = "消费用户月增量")
	private Integer consumerUserByMonth;

}
