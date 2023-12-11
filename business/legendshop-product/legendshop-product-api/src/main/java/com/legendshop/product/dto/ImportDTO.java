/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class ImportDTO implements Serializable {

	/**
	 * 商品名称
	 */
	@ColumnWidth(30)
	@ExcelProperty("商品名称")
	@Schema(description = "商品名称")
	private String productName;

	@ColumnWidth(30)
	@ExcelProperty("类目名称")
	@Schema(description = "类目名称")
	private String cateName;

	@ColumnWidth(30)
	@ExcelProperty("品牌")
	@Schema(description = "品牌")
	private String brand;

	@ColumnWidth(30)
	@ExcelProperty("商品图片")
	@Schema(description = "商品图片")
	private String pic;

	@ColumnWidth(30)
	@ExcelProperty("商品详情图片")
	@Schema(description = "商品详情图片")
	private String detailPic;

	@ColumnWidth(30)
	@ExcelProperty("商品详情文字")
	@Schema(description = "商品详情文字")
	private String detailText;

	@ColumnWidth(30)
	@ExcelProperty("快递类型")
	@Schema(description = "快递类型")
	private String deliveryType;


	@ColumnWidth(30)
	@ExcelProperty("运费模板")
	@Schema(description = "运费模板")
	private String transportName;

	@ColumnWidth(30)
	@ExcelProperty("商品描述")
	@Schema(description = "商品描述")
	private String brief;

	@ColumnWidth(30)
	@ExcelProperty("SKU名称")
	@Schema(description = "SKU名称")
	private String skuName;

	@ColumnWidth(30)
	@ExcelProperty("规格参数")
	@Schema(description = "规格参数")
	private String specification;

	@ColumnWidth(30)
	@ExcelProperty("sku规格图片")
	@Schema(description = "sku规格图片")
	private String specificationPic;

	@ColumnWidth(30)
	@ExcelProperty("成本价")
	@Schema(description = "成本价")
	private String costPrice;

	@ColumnWidth(30)
	@ExcelProperty("销售价格")
	@Schema(description = "销售价格")
	private String price;

	@ColumnWidth(30)
	@ExcelProperty("原价")
	@Schema(description = "原价")
	private String originalPrice;

	@ColumnWidth(30)
	@ExcelProperty("销售库存")
	@Schema(description = "销售库存")
	private String stocks;

	@ColumnWidth(30)
	@ExcelProperty("商家编码")
	@Schema(description = "商家编码")
	private String partyCode;

	@ColumnWidth(30)
	@ExcelProperty("商品条形码")
	@Schema(description = "商品条形码")
	private String modelId;

	@ColumnWidth(30)
	@ExcelProperty("参数属性")
	@Schema(description = "参数属性")
	private String properties;


}
