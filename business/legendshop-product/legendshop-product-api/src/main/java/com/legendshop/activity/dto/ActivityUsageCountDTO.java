/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import com.legendshop.activity.enums.MarketingTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityUsageCountDTO {

	@Schema(description = "日期")
	private Date date;

	@Schema(description = "折线：每日发布次数/ 表格：时间段总发布次数")
	private Integer usage;

	@Schema(description = "折线：每日金额/ 表格：时间段总金额")
	private BigDecimal amount;
	/**
	 * {@link MarketingTypeEnum}
	 */
	@Schema(description = "活动类型")
	private Integer type;

	@Schema(description = "累计成交数量")
	private Integer count;

	/**
	 * 累计成交用户数
	 */
	@Schema(description = "累计成交用户数")
	private Integer dealUserNum;

	/**
	 * 新成交用户人数
	 */
	@Schema(description = "新成交用户人数")
	private Integer newUserNum;

	/**
	 * 旧成交用户人数
	 */
	@Schema(description = "旧成交用户人数")
	private Integer oldUserNum;

	/**
	 * 累计访问次数
	 */
	@Schema(description = "累计访问次数")
	private Integer totalView;

	/**
	 * 累计访问人数
	 */
	@Schema(description = "累计访问人数")
	private Integer totalViewPeople;

	@Schema(description = "转化率")
	private Double inversionRate;

	public ActivityUsageCountDTO(Integer usage, BigDecimal amount) {
		this.usage = usage;
		this.amount = amount;
	}

	public ActivityUsageCountDTO(BigDecimal amount, Integer count, Integer dealUserNum, Integer newUserNum, Integer oldUserNum, Integer totalView, Integer totalViewPeople, Double inversionRate) {
		this.amount = amount;
		this.count = count;
		this.dealUserNum = dealUserNum;
		this.newUserNum = newUserNum;
		this.oldUserNum = oldUserNum;
		this.totalView = totalView;
		this.totalViewPeople = totalViewPeople;
		this.inversionRate = inversionRate;
	}

	public ActivityUsageCountDTO(Date date, Integer usage, Integer type) {
		this.date = date;
		this.usage = usage;
		this.type = type;
	}

	public ActivityUsageCountDTO(Date date, BigDecimal amount, Integer type) {
		this.date = date;
		this.amount = amount;
		this.type = type;
	}
}
