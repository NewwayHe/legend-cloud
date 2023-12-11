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
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
public class OrderImportLogisticsExportDTO {

	/**
	 * 订单号
	 */
	@ColumnWidth(50)
	@ExcelProperty("订单号")
	private String number;

	/**
	 * 物流公司Id
	 */
	@ExcelIgnore
	@ColumnWidth(50)
	@ExcelProperty("物流公司Id")
	private Long logisticsCompanyId;

	/**
	 * 物流公司名称
	 */
	@ColumnWidth(50)
	@ExcelProperty("物流公司名称")
	private String logisticsCompany;

	/**
	 * 物流公司编码
	 */
	@ColumnWidth(50)
	@ExcelProperty("物流公司编码")
	private String companyCode;

	/**
	 * 物流单号
	 */
	@ColumnWidth(50)
	@ExcelProperty("物流单号")
	private String logisticsNumber;

	/**
	 * 推送结果
	 */
	@ColumnWidth(50)
	@ExcelProperty("推送结果")
	private Boolean result;

	/**
	 * 错误描述
	 */
	@ColumnWidth(50)
	@ExcelProperty("错误描述")
	private String failReason;

	/**
	 * 创建时间
	 */
	@ColumnWidth(50)
	@ExcelProperty("创建时间")
	private Date createTime;
}
