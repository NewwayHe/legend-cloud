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
 * 等级分布统计
 *
 * @author legendshop
 */
@Data
@Schema(description = "等级分布统计")
public class UserDataGradeDTO {

	/**
	 * 用户等级
	 */
	@Schema(description = "用户等级")
	private Integer grade;

	/**
	 * 人数
	 */
	@Schema(description = "人数")
	private Integer userNum;

	/**
	 * 百分比
	 */
	@Schema(description = "百分比")
	private Double percentage;

}
