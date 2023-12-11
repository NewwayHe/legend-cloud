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
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单品SKU表(Sku)实体类
 *
 * @author legendshop
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
public class StockExportDTO implements Serializable {

	private static final long serialVersionUID = -700395850441772904L;

	/**
	 * 单品ID
	 */
	@ExcelIgnore
	private Long id;

	/**
	 * 商品ID
	 */
	@ExcelIgnore
	private Long productId;

	/**
	 * 商品名称
	 */
	@ColumnWidth(30)
	@ExcelProperty("商品名称")
	private String name;

	/**
	 * 销售属性组合（中文）
	 */
	@ColumnWidth(30)
	@ExcelProperty("规格")
	private String cnProperties;

	/**
	 * 销售价
	 */
	@ColumnWidth(30)
	@ExcelProperty("销售价")
	private BigDecimal price;

	/**
	 * 已经销售数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("销量")
	private Integer buys;

	/**
	 * 实际库存
	 */
	@ColumnWidth(30)
	@ExcelProperty("实际库存")
	private Integer actualStocks;

	/**
	 * 虚拟库存（商品在付款减库存的状态下，该sku上未付款的订单数量）
	 */
	@ColumnWidth(30)
	@ExcelProperty("销售库存")
	private Integer stocks;


	/**
	 * 库存预警
	 */
	@ColumnWidth(30)
	@ExcelProperty("库存预警")
	private Integer stocksArm;

	/**
	 * sku状态。 1l:正常 ；0:删除
	 */
	@ColumnWidth(30)
	@ExcelProperty("状态")
	private String statusStr;

}
