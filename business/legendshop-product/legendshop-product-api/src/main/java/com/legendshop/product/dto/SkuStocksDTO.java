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
 * skuId 库存操作对象
 *
 * @author legendshop
 */
@Data
public class SkuStocksDTO implements Serializable {

	@Schema(description = "SKU ID")
	private Long skuId;

	@Schema(description = "活动ID")
	private Long activityId;

	@Schema(description = "使用数量")
	private Integer count;

	public SkuStocksDTO() {
	}

	public SkuStocksDTO(Long skuId, Integer count) {
		this.skuId = skuId;
		this.count = count;
	}

	public SkuStocksDTO(Long skuId, Long activityId, Integer count) {
		this.skuId = skuId;
		this.activityId = activityId;
		this.count = count;
	}
}
