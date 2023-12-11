/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

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
@Schema(description = "营销概况 - 优惠券")
public class ActivityCouponStatisticsDTO {
	private static final long serialVersionUID = 5324902196827904291L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	@ExcelIgnore
	private Long id;


	/**
	 * 店铺名称
	 */
	@ColumnWidth(20)
	@ExcelProperty("店铺名称")
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 优惠券标题
	 */
	@ColumnWidth(20)
	@ExcelProperty("优惠券标题")
	@Schema(description = "优惠券标题")
	private String title;

	/**
	 * 领取时间
	 */
	@ColumnWidth(45)
	@ExcelProperty("领取时间")
	@Schema(description = "领取时间")
	private String receiveTime;

	/**
	 * 领取方式
	 * {@link com.legendshop.activity.enums.CouponReceiveTypeEnum}
	 */
	@ColumnWidth(20)
	@ExcelProperty("领取方式")
	@Schema(description = "领取方式，0，免费领取，1，卡密领取，2，积分兑换")
	private String receiveType;

	/**
	 * 使用门槛，0为无门槛
	 */
	@ColumnWidth(17)
	@ExcelProperty("门槛")
	@Schema(description = "使用门槛，0为无门槛")
	private String minPoint;

	/**
	 * 面额
	 */
	@ColumnWidth(17)
	@ExcelProperty("面额")
	@Schema(description = "面额")
	private String amount;

	/**
	 * 领券用户数
	 */
	@ColumnWidth(17)
	@ExcelProperty("领券用户数")
	@Schema(description = "领券用户数")
	private Integer receiveCount;


	/**
	 * 已领券数
	 */
	@ColumnWidth(15)
	@ExcelProperty("已领券数")
	@Schema(description = "已领券数")
	private Integer alreadyCount;


	/**
	 * 已用券用户数
	 */
	@ColumnWidth(20)
	@ExcelProperty("已用券用户数")
	@Schema(description = "已用券用户数")
	private Integer alreadyUserCount;


	/**
	 * 已用券数
	 */
	@ColumnWidth(17)
	@ExcelProperty("已用券数")
	@Schema(description = "已用券数")
	private Integer useCount;
}
