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

import java.math.BigDecimal;

/**
 * 搜索概况
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "搜索概况分页")
public class ProductDataSearchPicDTO {


	/**
	 * 关键词
	 */
	@ColumnWidth(30)
	@ExcelProperty("关键词")
	@Schema(description = "关键词")
	private String word;

	/**
	 * 搜索用户总数
	 */
	@ColumnWidth(30)
	@ExcelProperty("搜索用户总数")
	@Schema(description = "搜索用户总数")
	private Integer people;

	/**
	 * 搜索次数总数
	 */
	@ColumnWidth(30)
	@ExcelProperty("搜索次数总数")
	@Schema(description = "搜索次数总数")
	private Integer frequency;

	/**
	 * 小程序搜索用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("小程序搜索用户数")
	@Schema(description = "小程序搜索用户数")
	private Integer miniPeople;

	/**
	 * 小程序搜索次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("小程序搜索次数")
	@Schema(description = "小程序搜索次数")
	private Integer miniFrequency;

	/**
	 * h5搜索用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("h5搜索用户数")
	@Schema(description = "h5搜索用户数")
	private Integer h5People;

	/**
	 * h5搜索次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("h5搜索次数")
	@Schema(description = "h5搜索次数")
	private Integer h5Frequency;

	/**
	 * 同比
	 */
	@ColumnWidth(30)
	@ExcelProperty("同比")
	@Schema(description = "同比")
	private BigDecimal figureLastYear;

	/**
	 * 环比
	 */
	@ColumnWidth(30)
	@ExcelProperty("环比")
	@Schema(description = "环比")
	private BigDecimal figureLastMonth;

}
