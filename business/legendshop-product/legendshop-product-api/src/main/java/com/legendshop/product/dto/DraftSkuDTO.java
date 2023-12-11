/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单品SKU草稿表(DraftSku)DTO
 *
 * @author legendshop
 * @since 2022-05-08 10:13:21
 */
@Data
@Schema(description = "单品SKU草稿表DTO")
public class DraftSkuDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 944404880402603786L;

	/**
	 * ID
	 */
	@Schema(description = "ID")
	private Long id;

	/**
	 * 单品ID
	 */
	@Schema(description = "单品ID")
	private Long skuId;

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	/**
	 * 商品规格属性组合（中文）
	 */
	@Schema(description = "商品规格属性组合（中文）")
	private String cnProperties;

	/**
	 * 商品规格属性id组合（颜色，大小，等等，可通过类目API获取某类目下的销售属性）,e.g 223:673;224:689
	 */
	@Schema(description = "商品规格属性id组合（颜色，大小，等等，可通过类目API获取某类目下的销售属性）,e.g 223:673;224:689")
	private String properties;

	/**
	 * 用户自定义的销售属性，key:value 格式
	 */
	@Schema(description = "用户自定义的销售属性，key:value 格式")
	private String userProperties;

	/**
	 * 原价
	 */
	@Schema(description = "原价")
	private BigDecimal originalPrice;

	/**
	 * 成本价
	 */
	@Schema(description = "成本价")
	private BigDecimal costPrice;

	/**
	 * 销售价
	 */
	@Schema(description = "销售价")
	private BigDecimal price;

	/**
	 * SKU名称
	 */
	@Schema(description = "SKU名称")
	private String name;

	/**
	 * 商家编码
	 */
	@Schema(description = "商家编码")
	private String partyCode;

	/**
	 * 商品条形码
	 */
	@Schema(description = "商品条形码")
	private String modelId;

	/**
	 * sku图片
	 */
	@Schema(description = "sku图片")
	private String pic;

	/**
	 * 物流体积
	 */
	@Schema(description = "物流体积")
	private Double volume;

	/**
	 * 物流重量(千克)
	 */
	@Schema(description = "物流重量(千克)")
	private Double weight;

}
