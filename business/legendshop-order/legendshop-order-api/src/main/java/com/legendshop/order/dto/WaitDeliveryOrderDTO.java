/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class WaitDeliveryOrderDTO implements Serializable {

	@ColumnWidth(30)
	@ExcelProperty("订单号")
	@Schema(description = "订单号")
	private String orderNumber;

	@ColumnWidth(30)
	@ExcelProperty("收货人")
	@Schema(description = "收货人")
	private String receiver;

	@ColumnWidth(30)
	@ExcelProperty("收货人手机号")
	@Schema(description = "收货人手机号")
	private String mobile;


	@ColumnWidth(30)
	@ExcelProperty("物流公司")
	@Schema(description = "物流公司")
	private String logisticsCompany;

	@ColumnWidth(30)
	@ExcelProperty("物流单号")
	@Schema(description = "物流单号")
	private String number;

}
