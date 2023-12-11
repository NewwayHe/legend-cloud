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

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "商品概况分页")
public class ProductDataViewPageDTO {

	/**
	 * 商品id
	 */
	@ColumnWidth(30)
	@ExcelProperty("商品ID")
	@Schema(description = "商品id")
	private Integer goodId;

	/**
	 * 商品名
	 */
	@ColumnWidth(30)
	@ExcelProperty("商品名称")
	@Schema(description = "商品名")
	private String goodName;

	/**
	 * 商品访问用户总数
	 */
	@ColumnWidth(30)
	@ExcelProperty("全渠道商品访问量")
	@Schema(description = "商品访问用户总数")
	private Integer people;

	/**
	 * 商品访问次数总数
	 */
	@ColumnWidth(30)
	@ExcelProperty("全渠道商品访问数")
	@Schema(description = "商品访问次数总数")
	private Integer frequency;

	/**
	 * 小程序商品访问用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("小程序商品访问量")
	@Schema(description = "小程序商品访问用户数")
	private Integer miniPeople;

	/**
	 * 小程序商品访问次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("小程序商品访问数")
	@Schema(description = "小程序商品访问次数")
	private Integer miniFrequency;

	/**
	 * h5商品访问用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("H5商品访问量")
	@Schema(description = "h5商品访问用户数")
	private Integer h5People;

	/**
	 * h5商品访问次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("H5商品访问数")
	@Schema(description = "h5商品访问次数")
	private Integer h5Frequency;

	/**
	 * app商品访问用户数
	 */
	@ExcelIgnore
	@ColumnWidth(30)
	@ExcelProperty("app商品访问量")
	@Schema(description = "app商品访问用户数")
	private Integer appPeople;

	/**
	 * app商品访问次数
	 */
	@ExcelIgnore
	@ColumnWidth(30)
	@ExcelProperty("app商品访问数")
	@Schema(description = "app商品访问次数")
	private Integer appFrequency;

	/**
	 * mp商品访问用户数
	 */
	@ExcelIgnore
	@ColumnWidth(30)
	@ExcelProperty("mp商品访问量")
	@Schema(description = "mp商品访问用户数")
	private Integer mpPeople;

	/**
	 * mp商品访问次数
	 */
	@ExcelIgnore
	@ColumnWidth(30)
	@ExcelProperty("mp商品访问数")
	@Schema(description = "mp商品访问次数")
	private Integer mpFrequency;

}
