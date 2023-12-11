/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单发票DTO
 *
 * @author legendshop
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
public class OrderInvoiceExportDTO implements Serializable {


	@ExcelProperty("订单号")
	@ColumnWidth(50)
	private String orderNumber;

	@ExcelProperty("订单金额")
	@ColumnWidth(50)
	@NumberFormat("#.##")
	private BigDecimal actualTotalPrice;

	/**
	 * {@link com.legendshop.user.enums.UserInvoiceTypeEnum}
	 */
	@ExcelProperty("发票类型")
	@ColumnWidth(50)
	private String type;


	/**
	 * {@link com.legendshop.user.enums.UserInvoiceTitleTypeEnum}
	 */
	@ExcelProperty("抬头类型")
	@ColumnWidth(50)
	private String titleType;


	/**
	 * 个人普票：发票抬头信息 公司普票：发票抬头信息 增值税专票：公司名称
	 */
	@ExcelProperty("抬头信息")
	@ColumnWidth(50)
	private String company;


	@ExcelProperty("发票内容 默认2：商品明细")
	@ColumnWidth(50)
	private Integer content;


	@ExcelProperty("纳税人号")
	@ColumnWidth(50)
	private String invoiceHumNumber;


	@ExcelProperty("注册地址（增值税发票）")
	@ColumnWidth(50)
	private String registerAddr;


	@ExcelProperty("注册电话（增值税发票）")
	@ColumnWidth(50)
	private String registerPhone;


	@ExcelProperty("开户银行（增值税发票）")
	@ColumnWidth(50)
	private String depositBank;


	@ExcelProperty("开户银行账号（增值税发票）")
	@ColumnWidth(50)
	private String bankAccountNum;


	@ExcelProperty("是否已开发票")
	@ColumnWidth(50)
	private String hasInvoiceFlag;


	@ExcelProperty("创建时间")
	@ColumnWidth(50)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

}
