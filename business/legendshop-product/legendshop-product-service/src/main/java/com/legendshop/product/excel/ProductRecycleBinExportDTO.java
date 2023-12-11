/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.legendshop.product.enums.ProductDelStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
public class ProductRecycleBinExportDTO implements Serializable {

	private static final long serialVersionUID = -2114971316178101114L;

	/**
	 * 产品ID
	 */
	@ExcelIgnore
	@Schema(description = "产品ID")
	private Long id;


	@ExcelProperty("商品名称")
	@ColumnWidth(50)
	private String name;

	/**
	 * 品牌名称
	 **/
	@ColumnWidth(50)
	@ExcelProperty("品牌名称")
	private String brandName;

	/**
	 * sku数量
	 */
	@ExcelProperty("sku数量")
	@ColumnWidth(50)
	@Schema(description = "sku数量")
	private Integer skuCount;

	@ExcelProperty("销售价（元）")
	@ColumnWidth(30)
	private String price;

	/**
	 * 销量
	 */
	@ExcelProperty("销量")
	@ColumnWidth(15)
	@Schema(description = "销量")
	private Integer buys;

	@ExcelProperty("库存")
	@ColumnWidth(30)
	private Integer stocks;

	/**
	 * 删除操作状态
	 * {@link ProductDelStatusEnum}
	 */
	@ExcelIgnore
	@Schema(description = "删除操作状态 -2：永久删除；-1：删除；1：正常；")
	private Integer delStatus;


	@ExcelProperty("状态")
	@ColumnWidth(15)
	private String delStatusStr;


	/**
	 * 更新时间
	 */
	@ExcelProperty("更新时间")
	@ColumnWidth(15)
	@Schema(description = "更新时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
}
