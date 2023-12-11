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
import com.legendshop.product.enums.ProductDelStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
public class ProductExportDTO implements Serializable {

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


	/**
	 * 商品状态：{@link com.legendshop.product.enums.ProductStatusEnum}
	 */
	@ExcelIgnore
	@Schema(description = "商品状态: -2：永久删除；-1：删除；0：下线；1：上线；2：违规；3：全部")
	private Integer status;

	/**
	 * 审核操作状态 {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@ExcelIgnore
	@Schema(description = "审核状态 -1：拒绝；0：待审核 ；1：通过")
	private Integer opStatus;

	/**
	 * 删除操作状态
	 * {@link ProductDelStatusEnum}
	 */
	@ExcelIgnore
	@Schema(description = "删除操作状态 -2：永久删除；-1：删除；1：正常；")
	private Integer delStatus;

	@ExcelIgnore
	@Schema(description = "草稿状态，-10: 商品未发布、-1: 拒绝、0: 待审核、1: 通过")
	private Integer draftStatus;


	@ExcelProperty("商品状态")
	@ColumnWidth(15)
	private String statusStr;


	@ExcelProperty("草稿状态")
	@ColumnWidth(15)
	private String draftStatusStr;

	/**
	 * 备注
	 */
	@ExcelProperty("备注")
	@ColumnWidth(15)
	private String remarks;
}
