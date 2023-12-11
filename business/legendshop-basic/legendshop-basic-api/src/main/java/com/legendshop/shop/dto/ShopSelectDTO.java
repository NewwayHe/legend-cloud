/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 商家店铺下拉选择框数据
 *
 * @author legendshop
 */
@Data
@Schema(description = "商家店铺下拉选择框数据")
public class ShopSelectDTO implements Serializable {


	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long id;


	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

}
