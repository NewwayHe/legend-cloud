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
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 运费计算DTO
 *
 * @author legendshop
 */
@Data
@Accessors(chain = true)
@Schema(description = "运费计算DTO")
public class TransFeeCalculateDTO implements Serializable {

	private static final long serialVersionUID = 794211128988377768L;

	/**
	 * 商家id
	 */
	@Schema(description = "商家id")
	private Long shopId;

	/**
	 * 城市id
	 */
	@Schema(description = "城市id")
	private Long cityId;

	/**
	 * 计算运费商品信息
	 */
	@Schema(description = "计算运费商品信息")
	private List<TransFeeCalProductDTO> calProductDTOS;

}
