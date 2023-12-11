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
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品批量导入报错详情
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
public class ProductImportErrorDetailDTO implements Serializable {

	@ColumnWidth(30)
	@ExcelProperty("商品名字")
	@Schema(description = "商品名字")
	private String productName;

	@ColumnWidth(30)
	@ExcelProperty("类目名字")
	@Schema(description = "类目名字")
	private String cateName;

	@ColumnWidth(30)
	@ExcelProperty("操作时间")
	@Schema(description = "操作时间")
	private Date createTime;

	@ColumnWidth(30)
	@ExcelProperty("商品主图")
	@Schema(description = "商品主图")
	private String pic;

	@ColumnWidth(30)
	@ExcelProperty("卖点")
	@Schema(description = "卖点")
	private String brief;

	@ColumnWidth(30)
	@ExcelProperty("快递类型")
	@Schema(description = "快递类型")
	private Integer deliveryType;

	@ColumnWidth(30)
	@ExcelProperty("sku名字")
	@Schema(description = "sku名字")
	private String skuName;

	@ColumnWidth(30)
	@ExcelProperty("库存数")
	@Schema(description = "库存数")
	private Integer stocks;

	@ColumnWidth(30)
	@ExcelProperty("属性")
	@Schema(description = "属性")
	private String properties;

	@ColumnWidth(30)
	@ExcelProperty("规格")
	@Schema(description = "规格")
	private String specification;

	@ColumnWidth(30)
	@ExcelProperty("sku规格图片")
	@Schema(description = "sku规格图片")
	private String specificationPic;

	@ColumnWidth(30)
	@ExcelProperty("成本价")
	@Schema(description = "成本价")
	private BigDecimal costPrice;

	@ColumnWidth(30)
	@ExcelProperty("销售价")
	@Schema(description = "销售价")
	private BigDecimal price;

	@ColumnWidth(30)
	@ExcelProperty("商家编码")
	@Schema(description = "商家编码")
	private String partyCode;

	@ColumnWidth(30)
	@ExcelProperty("条形码")
	@Schema(description = "条形码")
	private Integer modelId;

	@ColumnWidth(30)
	@ExcelProperty("商品图片详情")
	@Schema(description = "商品图片详情")
	private String detailPic;

	@ColumnWidth(30)
	@ExcelProperty("原价")
	@Schema(description = "原价")
	private String originalPrice;

	@ColumnWidth(30)
	@ExcelProperty("错误描述")
	@Schema(description = "错误描述")
	private String failReason;

}
