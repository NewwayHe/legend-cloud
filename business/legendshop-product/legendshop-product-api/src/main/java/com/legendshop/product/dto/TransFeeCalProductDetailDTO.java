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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 运费计算所需要的商品信息(用于前端商品详情页)
 *
 * @author legendshop
 */
@Data
public class TransFeeCalProductDetailDTO {

	private static final long serialVersionUID = 794211143988377768L;

	/**
	 * 城市id
	 */
	@Schema(description = "cityId")
	@NotNull(message = "城市id不能为空")
	private Long cityId;

	/**
	 * skuId
	 */
	@Schema(description = "skuId")
	@NotNull(message = "商家id不能为空")
	private Long skuId;

	/**
	 * 购买数量
	 */
	@Schema(description = "购买数量")
	@NotNull(message = "购买数量不能为空")
	@Min(value = 1, message = "购买数量不能小于1")
	private Integer productCount;

}
