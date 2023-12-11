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
import java.math.BigDecimal;

/**
 * 失效商品信息
 *
 * @author legendshop
 */
@Data
@Schema(description = "失效商品信息")
public class InvalidOrderSkuDTO implements Serializable {

	private static final long serialVersionUID = -2159280085965706733L;

	/**
	 * 商品信息
	 */
	@Schema(description = "商品编码唯一标识")
	private Long skuId;

	@Schema(description = "商品ID")
	private Long productId;

	@Schema(description = "商品名称")
	private String productName;

	@Schema(description = "商品图片")
	private String pic;

	@Schema(description = "规格属性")
	private String cnProperties;

	@Schema(description = "原价")
	private BigDecimal originalPrice;

	@Schema(description = "销售价")
	private BigDecimal price;

	@Schema(description = "数量")
	private Integer totalCount;

	@Schema(description = "商品状态异常标识")
	private Boolean statusFlag;

	@Schema(description = "所属商家ID")
	private Long shopId;
}
