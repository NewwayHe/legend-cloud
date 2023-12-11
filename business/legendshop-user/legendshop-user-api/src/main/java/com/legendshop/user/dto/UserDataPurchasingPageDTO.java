/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.legendshop.common.core.annotation.DataSensitive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户购买力分页统计数据
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "用户购买力分页统计数据")
public class UserDataPurchasingPageDTO {

	/**
	 * 用户id
	 */
	@ExcelIgnore
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 用户昵称
	 */
	@ColumnWidth(30)
	@ExcelProperty("用户昵称")
	@Schema(description = "用户昵称")
	private String nickName;

	/**
	 * 手机号
	 */
	@ColumnWidth(30)
	@ExcelProperty("手机号")
	@Schema(description = "手机号")
	@DataSensitive(type = DataSensitive.SensitiveTypeEnum.MOBILE_PHONE)
	private String mobile;

	/**
	 * 下单金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("下单金额")
	@Schema(description = "下单金额")
	private BigDecimal totalAmount;

	/**
	 * 下单数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("下单数量")
	@Schema(description = "下单数量")
	private Integer totalQuantity;

	/**
	 * 支付金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("支付金额")
	@Schema(description = "支付金额")
	private BigDecimal payAmount;

	/**
	 * 支付数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("支付数量")
	@Schema(description = "支付数量")
	private Integer payQuantity;

	/**
	 * 成交金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("成交金额")
	@Schema(description = "成交金额")
	private BigDecimal dealAmount;

	/**
	 * 成交数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("成交数量")
	@Schema(description = "成交数量")
	private Integer dealQuantity;

	/**
	 * 累计下单金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计下单金额")
	@Schema(description = "累计下单金额")
	private BigDecimal cumulationTotalAmount;

	/**
	 * 累计下单数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计下单数量")
	@Schema(description = "累计下单数量")
	private Integer cumulationTotalQuantity;

	/**
	 * 累计支付金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计支付金额")
	@Schema(description = "累计支付金额")
	private BigDecimal cumulationPayAmount;

	/**
	 * 累计支付数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计支付数量")
	@Schema(description = "累计支付数量")
	private Integer cumulationPayQuantity;

	/**
	 * 累计成交金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计成交金额")
	@Schema(description = "累计成交金额")
	private BigDecimal cumulationDealAmount;

	/**
	 * 累计成交数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计成交数量")
	@Schema(description = "累计成交数量")
	private Integer cumulationDealQuantity;

	/**
	 * 客单价
	 */
	@ColumnWidth(30)
	@ExcelProperty("客单价")
	@Schema(description = "客单价")
	private BigDecimal customerPrice;

}
