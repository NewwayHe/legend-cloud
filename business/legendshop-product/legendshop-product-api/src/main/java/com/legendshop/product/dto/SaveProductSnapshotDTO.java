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
 * 保存商品快照DTO
 *
 * @author legendshop
 */
@Schema(description = "保存商品快照DTO")
@Data
public class SaveProductSnapshotDTO implements Serializable {

	private static final long serialVersionUID = 1394069474111212447L;


	/**
	 * 产品ID
	 */
	@Schema(description = "产品ID")
	private Long productId;

	/**
	 * skuId
	 */
	@Schema(description = "skuId")
	private Long skuId;

	/**
	 * 原价
	 */
	@Schema(description = "原价")
	private BigDecimal originalPrice;

	/**
	 * 现价
	 */
	@Schema(description = "销售价")
	private BigDecimal price;

	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String pic;


	/**
	 * 属性json
	 */
	@Schema(description = "属性json")
	private String attribute;

	/**
	 * 购买数量
	 */
	@Schema(description = "购买数量")
	private Integer basketCount;


}
