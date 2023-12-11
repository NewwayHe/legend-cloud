/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "营销汇总列表")
public class ActivityCollectPage {

	@ColumnWidth(30)
	@ExcelProperty("活动类型")
	@Schema(description = "活动类型")
	private String activityType;

	@ColumnWidth(30)
	@ExcelProperty("累计发布活动数")
	@Schema(description = "累计发布活动数")
	private Integer usage;

	@ColumnWidth(30)
	@ExcelProperty("累计成交金额")
	@Schema(description = "累计成交金额")
	private BigDecimal amount;

	/**
	 * 活动类型
	 */
	@ExcelIgnore
	@ColumnWidth(30)
	@Schema(description = "活动类型")
	private Integer type;

	@ColumnWidth(30)
	@ExcelProperty("累计成交商品数量")
	@Schema(description = "累计成交数量")
	private Integer count;

	/**
	 * 累计成交用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计成交人数")
	@Schema(description = "累计成交用户数")
	private Integer dealUserNum;
	/**
	 * 新成交用户人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("新成交人数")
	@Schema(description = "新成交用户人数")
	private Integer newUserNum;

	/**
	 * 旧成交用户人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("旧成交人数")
	@Schema(description = "旧成交用户人数")
	private Integer oldUserNum;

	/**
	 * 累计访问次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计访问次数")
	@Schema(description = "累计访问次数")
	private Integer totalView;

	/**
	 * 累计访问人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计访问人数")
	@Schema(description = "累计访问人数")
	private Integer totalViewPeople;

	@ColumnWidth(30)
	@ExcelProperty("转化率")
	@Schema(description = "转化率")
	private BigDecimal inversionRate;


	public ActivityCollectPage() {
		this.usage = 0;
		this.amount = BigDecimal.ZERO;
		this.count = 0;
		this.dealUserNum = 0;
		this.newUserNum = 0;
		this.oldUserNum = 0;
		this.totalView = 0;
		this.totalViewPeople = 0;
		this.inversionRate = BigDecimal.ZERO;
	}
}
