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
 * 累计用户数量DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "累计用户数量DTO")
public class UserDataAllDTO {

	/**
	 * 累计用户数量
	 */
	@Schema(description = "累计用户数量")
	private Integer allUserNum;

	/**
	 * 累计用户数量日增量
	 */
	@Schema(description = "累计用户数量日增量")
	private Integer allUserNumByDay;

	/**
	 * 累计用户数量周增量
	 */
	@Schema(description = "累计用户数量周增量")
	private Integer allUserNumByWeek;

	/**
	 * 累计用户数量月增量
	 */
	@Schema(description = "累计用户数量月增量")
	private Integer allUserNumByMonth;

}
