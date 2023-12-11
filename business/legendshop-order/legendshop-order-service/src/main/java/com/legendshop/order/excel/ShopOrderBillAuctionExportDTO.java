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
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账单拍卖列表
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "账单拍卖列表")
public class ShopOrderBillAuctionExportDTO implements Serializable {

	private static final long serialVersionUID = -3992620050695981996L;

	@ExcelIgnore
	@Schema(description = "活动ID")
	private Long auctionId;

	@ColumnWidth(30)
	@ExcelProperty("活动名称")
	@Schema(description = "活动名称")
	private String auctionName;

	@ColumnWidth(20)
	@ExcelProperty("用户名")
	@Schema(description = "用户名")
	private String userName;

	@ColumnWidth(20)
	@ExcelProperty("保证金金额")
	@Schema(description = "保证金金额")
	private BigDecimal auctionAmount;

	@ColumnWidth(30)
	@ExcelProperty("支付时间")
	@Schema(description = "支付时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payTime;
}
