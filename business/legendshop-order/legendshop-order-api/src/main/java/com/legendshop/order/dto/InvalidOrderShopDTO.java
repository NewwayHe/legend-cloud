/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 失效店铺商品信息
 *
 * @author legendshop
 */
@Data
@Schema(description = "失效店铺商品信息")
public class InvalidOrderShopDTO implements Serializable {

	/**
	 * 店铺信息
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	@Schema(description = "店铺名称")
	private String shopName;


	/**
	 * 失效商品集合
	 */
	@Schema(description = "失效商品集合")
	private List<InvalidOrderSkuDTO> invalidOrderSkuList;

}
