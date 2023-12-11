/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 广告报表导出接口
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "广告数据统计")
public class AdvertiseCountExportDTO {

	/**
	 * 新增投放人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增投放人数")
	@Schema(description = "新增投放人数")
	private BigDecimal newPutPeople;

	/**
	 * 投放人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("投放人数")
	@Schema(description = "投放人数")
	private BigDecimal putPeople;

	/**
	 * 新增投放次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增投放次数")
	@Schema(description = "新增投放次数")
	private BigDecimal newPutCount;

	/**
	 * 投放次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("投放次数")
	@Schema(description = "投放次数")
	private BigDecimal putCount;

	/**
	 * 新增点击人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增点击人数")
	@Schema(description = "新增点击人数")
	private BigDecimal newClickPeople;

	/**
	 * 点击人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("点击人数")
	@Schema(description = "点击人数")
	private BigDecimal clickPeople;

	/**
	 * 新增点击次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增点击次数")
	@Schema(description = "新增点击次数")
	private BigDecimal newClickCount;

	/**
	 * 点击次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("点击次数")
	@Schema(description = "点击次数")
	private BigDecimal clickCount;

	/**
	 * 时间
	 */
	@ColumnWidth(30)
	@ExcelProperty("时间")
	@Schema(description = "时间")
	private String time;

	/**
	 * 回报率
	 */
	@ColumnWidth(30)
	@ExcelProperty("回报率")
	@Schema(description = "回报率")
	private BigDecimal inversionRate;

}
