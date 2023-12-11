/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商品销售趋势图")
public class ProductDataGoodSaleDTO {

	@Schema(description = "商品id")
	private Integer productId;

	@Schema(description = "商品名称")
	private String productName;

	@Schema(description = "sku名称")
	private String skuName;

	@Schema(description = "销售价")
	private BigDecimal price;

	@Schema(description = "成交金额")
	private BigDecimal dealAmount;

	@Schema(description = "成交数量")
	private Integer dealGoodNum;

	@Schema(description = "访问次数")
	private Integer viewNum;

	@Schema(description = "商品收藏量")
	private Integer favoriteNum;

	@Schema(description = "累计成交金额")
	private BigDecimal totalDealAmount;

	@Schema(description = "累计成交数量")
	private Integer totalDealGoodNum;

	@Schema(description = "累计访问次数")
	private Integer totalViewNum;

	@Schema(description = "累计商品收藏量")
	private Integer totalFavoriteNum;

	@Schema(description = "时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date time;
}
