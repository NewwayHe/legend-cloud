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

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "店铺和平台优惠券营销用户数据统计")
public class MarketingUserStatisticsDTO implements Serializable {

	private static final long serialVersionUID = -96425956231931407L;

	/**
	 * 优惠券领取次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("优惠券领取次数")
	@Schema(description = "优惠券领取次数")
	private Long couponsReceivedCount;

	/**
	 * 优惠券用户
	 */
	@ColumnWidth(30)
	@ExcelProperty("优惠券领取用户")
	@Schema(description = "优惠券领取用户")
	private Long couponsReceivedUser;

	/**
	 * 下单老用户
	 */
	@ColumnWidth(30)
	@ExcelProperty("下单老用户")
	@Schema(description = "下单老用户")
	private Long orderOldUserCount;

	/**
	 * 下单老用户
	 */
	@ColumnWidth(30)
	@ExcelProperty("下单老用户占比")
	@Schema(description = "下单老用户占比")
	private BigDecimal orderOldUserRate;

	/**
	 * 下单新用户
	 */
	@ColumnWidth(30)
	@ExcelProperty("下单新用户")
	@Schema(description = "下单新用户")
	private Long orderNewUserCount;

	/**
	 * 下单老用户
	 */
	@ColumnWidth(30)
	@ExcelProperty("下单新用户占比")
	@Schema(description = "下单新用户占比")
	private BigDecimal orderNewUserRate;

	/**
	 * 累计下单成交数
	 */
	@ColumnWidth(30)
	@ExcelProperty("成交订单数")
	@Schema(description = "成交订单数")
	private Long payCount;

	/**
	 * 下单成交用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("成交用户数")
	@Schema(description = "成交用户数")
	private Long userPayCount;

	/**
	 * 下单用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("下单用户数")
	@Schema(description = "下单用户数")
	private Long userOrderCount;

	/**
	 * 下单数
	 */
	@ColumnWidth(30)
	@ExcelProperty("下单订单数")
	@Schema(description = "下单订单数")
	private Long orderCount;

	/**
	 * 转化率
	 */
	@ColumnWidth(30)
	@ExcelProperty("转化率")
	@Schema(description = "转化率")
	private BigDecimal inversionRate;

	/**
	 * 来源
	 */
	@ExcelIgnore
	@Schema(description = "来源")
	private String sourceList;

	public MarketingUserStatisticsDTO() {
		this.couponsReceivedCount = 0L;
		this.couponsReceivedUser = 0L;
		this.orderOldUserCount = 0L;
		this.orderNewUserCount = 0L;
		this.payCount = 0L;
		this.userPayCount = 0L;
		this.userOrderCount = 0L;
		this.orderCount = 0L;
		this.inversionRate = BigDecimal.ZERO;
		this.orderNewUserRate = BigDecimal.ZERO;
		this.orderOldUserRate = BigDecimal.ZERO;
	}
}
