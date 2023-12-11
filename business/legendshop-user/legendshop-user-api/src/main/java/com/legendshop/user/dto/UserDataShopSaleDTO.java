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
 * 店铺销售排行DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "店铺销售排行DTO")
public class UserDataShopSaleDTO {

	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Integer shopId;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 店铺累计交易金额
	 */
	@Schema(description = "店铺累计交易金额")
	private BigDecimal money;

}
