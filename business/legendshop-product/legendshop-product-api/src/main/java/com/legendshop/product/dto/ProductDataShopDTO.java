/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺下拉菜单数据
 *
 * @author legendshop
 */
@Data
@Schema(description = "店铺下拉菜单数据")
public class ProductDataShopDTO implements Serializable {


	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Integer id;

	/**
	 * 店铺名
	 */
	@Schema(description = "店铺名")
	private String shopName;

	/**
	 * 店铺地址
	 */
	@Schema(description = "店铺地址")
	private String shopAddress;


}
