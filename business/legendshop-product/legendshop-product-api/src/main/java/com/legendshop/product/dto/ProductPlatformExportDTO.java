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

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
public class ProductPlatformExportDTO implements Serializable {

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

	@ExcelProperty("成本价（元）")
	@ColumnWidth(50)
	@Schema(description = "成本价")
	private String costPrice;

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

	@ExcelProperty("销售库存")
	@ColumnWidth(30)
	private Integer stocks;

	@ExcelProperty("实际库存")
	@ColumnWidth(30)
	private Integer actualStocks;

	@ExcelProperty("分销佣金（元）")
	@ColumnWidth(30)
	@Schema(description = "分销佣金（元）")
	private String commission;

	@ExcelIgnore
	private Integer status;

	@ExcelProperty("商品状态")
	@ColumnWidth(15)
	private String statusStr;

	/**
	 * 备注
	 */
	@ExcelProperty("商家状态")
	@ColumnWidth(20)
	private String remarks;
}
