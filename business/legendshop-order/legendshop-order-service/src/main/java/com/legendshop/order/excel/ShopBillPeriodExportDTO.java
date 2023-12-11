/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.excel;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算档期(ShopBillPeriod)实体类
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "结算档期DTO")
public class ShopBillPeriodExportDTO implements Serializable {


	private static final long serialVersionUID = -4012098757570441527L;
	/**
	 * ID
	 */
	@ExcelIgnore
	private Long id;


	/**
	 * 档期标示，例如 201512-ID
	 */
	@ColumnWidth(30)
	@ExcelProperty("档期")
	@Schema(description = "档期表示 例如 201512-ID")
	private String flag;


	/**
	 * 应结金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("应结金额")
	@Schema(description = "应结金额")
	private BigDecimal resultTotalAmount;


	/**
	 * 这期订单的实际金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("这期订单的实际金额")
	@Schema(description = "这期订单的实际金额")
	private BigDecimal orderAmount;


	/**
	 * 平台佣金
	 */
	@ColumnWidth(30)
	@ExcelProperty("平台佣金")
	@Schema(description = "平台佣金")
	private BigDecimal commisTotals;


	/**
	 * 分销佣金
	 */
	@ColumnWidth(30)
	@ExcelProperty("分销佣金")
	@Schema(description = "分销佣金")
	private BigDecimal distCommisTotals;


	/**
	 * 退单金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("退单金额")
	@Schema(description = "退单金额")
	private BigDecimal orderReturnTotals;


	/**
	 * 使用的红包总额
	 */
	@ColumnWidth(30)
	@ExcelProperty("使用的红包总额")
	@Schema(description = "使用的红包总额")
	private BigDecimal redpackTotals;


	/**
	 * 创建时间
	 */
	@ColumnWidth(30)
	@ExcelProperty("创建时间")
	@Schema(description = "创建时间")
	private Date recDate;

}
