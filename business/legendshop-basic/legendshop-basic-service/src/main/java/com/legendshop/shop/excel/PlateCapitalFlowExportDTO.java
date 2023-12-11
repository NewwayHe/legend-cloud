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
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.legendshop.shop.enums.DealTypeEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台资金流水导出DTO
 *
 * @author legendshop
 * @since 2020-09-18 17:26:12
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
public class PlateCapitalFlowExportDTO implements Serializable {


	private static final long serialVersionUID = -5454249857182589251L;
	/**
	 * {@link DealTypeEnum}
	 */
	@ExcelProperty("交易类型")
	@ColumnWidth(25)
	private String dealType;

	/**
	 * 收支类型 {@link com.legendshop.shop.enums.FlowTypeEnum}
	 */
	@ExcelProperty("收支类型")
	@ColumnWidth(25)
	private String flowType;


	/**
	 * 金额
	 */
	@ExcelProperty("金额")
	@ColumnWidth(25)
	@NumberFormat("#.##")
	private BigDecimal amount;


	/**
	 * 备注
	 */

	private String remark;

	/**
	 * 记录时间
	 */
	@ExcelProperty("记录时间")
	@ColumnWidth(25)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date recDate;

	/**
	 * 支付方式
	 */
	@ExcelProperty("支付方式")
	@ColumnWidth(25)
	private String payTypeName;

	/**
	 * 支付流水号
	 */
	@ExcelProperty("支付流水号")
	@ColumnWidth(25)
	private String paySerialNumber;

	/**
	 * 支付时间
	 */
	@ExcelProperty("支付时间")
	@ColumnWidth(25)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;

}
