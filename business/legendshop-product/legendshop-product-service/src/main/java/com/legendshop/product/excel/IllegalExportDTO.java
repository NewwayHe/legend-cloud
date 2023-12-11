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
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
public class IllegalExportDTO implements Serializable {

	private static final long serialVersionUID = 7864336116495640805L;

	/**
	 * 主键
	 */
	@ExcelIgnore
	private Long id;

	/**
	 * 商品名称
	 */
	@ColumnWidth(30)
	@ExcelProperty("商品名称")
	private String name;

	/**
	 * 库存数
	 */
	@ExcelIgnore
	private String stocks;

	/**
	 * 价格区间
	 */
	@ColumnWidth(30)
	@ExcelProperty("销售价（元）")
	private String price;

	/**
	 * 品牌名称
	 */
	@ColumnWidth(30)
	@ExcelProperty("品牌")
	private String brandName;

	/**
	 * 下架时间
	 */
	@ColumnWidth(30)
	@ExcelProperty("违规下架时间")
	private Date accusationTime;

	@ExcelIgnore
	private Integer opStatus;

	@ColumnWidth(30)
	@ExcelProperty("状态")
	private String opStatusDesc;

	/**
	 * 违规下架原因
	 */
	@ColumnWidth(30)
	@ExcelProperty("违规下架原因")
	private String accusationContent;

}
