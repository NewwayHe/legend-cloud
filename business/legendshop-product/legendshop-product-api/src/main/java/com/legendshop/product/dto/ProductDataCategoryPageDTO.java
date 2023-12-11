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
import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "类目概况分页")
public class ProductDataCategoryPageDTO implements Serializable {

	/**
	 * 三级类目id
	 */
	@ExcelIgnore
	@Schema(description = "三级类目id")
	private Integer id;

	/**
	 * 一级类目名称
	 */
	@ColumnWidth(30)
	@ExcelProperty("一级类目名称")
	@Schema(description = "一级类目名称")
	private String firstName;

	/**
	 * 二级类目名称
	 */
	@ColumnWidth(30)
	@ExcelProperty("二级类目名称")
	@Schema(description = "二级类目名称")
	private String secondName;

	/**
	 * 三级类目名称
	 */
	@ColumnWidth(30)
	@ExcelProperty("三级类目名称")
	@Schema(description = "三级类目名称")
	private String thirdName;

	/**
	 * SKU数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("SKU数量")
	@Schema(description = "SKU数量")
	private Integer skuAmount;

	/**
	 * 访问人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("访问人数")
	@Schema(description = "访问人数")
	private Integer viewPeople;

	/**
	 * 访问次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("访问次数")
	@Schema(description = "访问次数")
	private Integer views;

	/**
	 * 成交金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("成交金额")
	@Schema(description = "成交金额")
	private BigDecimal turnover;

	/**
	 * 成交数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("成交数量")
	@Schema(description = "成交数量")
	private Integer amount;

	/**
	 * 访问购买率
	 */
	@ColumnWidth(30)
	@ExcelProperty("访问购买率")
	@Schema(description = "访问购买率")
	private BigDecimal buyRate;


}
