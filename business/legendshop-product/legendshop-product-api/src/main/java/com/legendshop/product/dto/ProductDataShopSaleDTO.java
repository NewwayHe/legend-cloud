/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "商品销售排行")
public class ProductDataShopSaleDTO {

	@ExcelIgnore
	@Schema(description = "商品id")
	private Integer productId;

	@ExcelIgnore
	@Schema(description = "图片地址")
	private String image;

	@ColumnWidth(30)
	@ExcelProperty("商品名称")
	@Schema(description = "商品名称")
	private String productName;

	@ExcelIgnore
	@ColumnWidth(30)
	@ExcelProperty("sku名称")
	@Schema(description = "sku名称")
	private String skuName;

	@ExcelIgnore
	@Schema(description = "skuId")
	private String skuId;

	@ColumnWidth(30)
	@ExcelProperty("销售价")
	@Schema(description = "销售价")
	private BigDecimal price;

	@ColumnWidth(30)
	@ExcelProperty("累计成交金额")
	@Schema(description = "累计成交金额")
	private BigDecimal dealAmount;

	@ColumnWidth(30)
	@ExcelProperty("累计成交数量")
	@Schema(description = "累计成交数量")
	private Integer dealGoodNum;

	@ColumnWidth(30)
	@ExcelProperty("累计访问次数")
	@Schema(description = "累计访问次数")
	private Integer viewNum;

	@ColumnWidth(30)
	@ExcelProperty("累计商品收藏量")
	@Schema(description = "累计商品收藏量")
	private Integer favoriteNum;

	public ProductDataShopSaleDTO() {
		this.dealAmount = BigDecimal.ZERO;
		this.dealGoodNum = 0;
		this.viewNum = 0;
		this.favoriteNum = 0;
	}

}
