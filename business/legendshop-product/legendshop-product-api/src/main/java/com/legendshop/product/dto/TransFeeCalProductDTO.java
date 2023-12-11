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
import java.math.BigDecimal;

/**
 * 运费计算所需要的商品信息
 *
 * @author legendshop
 */
@Data
@Schema(description = "运费计算所需要的商品信息")
public class TransFeeCalProductDTO implements Serializable {


	private static final long serialVersionUID = 794219996888377768L;

	private Long skuId;

	/**
	 * 商品id
	 */
	@Schema(description = "商品id")
	private Long productId;

	/**
	 * 运费模板id
	 */
	@Schema(description = "运费模板id")
	private Long transId;

	/**
	 * 总价格
	 */
	@Schema(description = "总价格")
	private BigDecimal totalPrice;

	/**
	 * 总数量
	 */
	@Schema(description = "总数量")
	private Integer totalCount;

	/**
	 * 总重量
	 */
	@Schema(description = "总重量")
	private Double totalWeight;

	/**
	 * 总体积
	 */
	@Schema(description = "总体积")
	private Double totalVolume;
}
