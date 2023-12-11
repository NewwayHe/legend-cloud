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

import java.math.BigDecimal;

/**
 * 用户购买力排行统计数据
 *
 * @author legendshop
 */
@Data
@Schema(description = "用户购买力排行统计数据")
public class UserDataPurchasingPowerDTO {

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 手机号
	 */
	@Schema(description = "手机号")
	private String mobile;

	/**
	 * 累计下单金额
	 */
	@Schema(description = "累计下单金额")
	private BigDecimal money;
}
