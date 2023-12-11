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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class InsertLogisticsDTO implements Serializable {

	@ExcelProperty(index = 0)
	@Schema(description = "发货订单号")
	private String orderNumber;


	@ExcelProperty(index = 1)
	@Schema(description = "收货人")
	private String receiver;

	@ExcelProperty(index = 2)
	@Schema(description = "收货人手机号")
	private String mobile;


	@ExcelProperty(index = 3)
	@Schema(description = "物流公司名称")
	private String logisticsCompany;


	@ExcelProperty(index = 4)
	@Schema(description = "物流单号")
	private String logisticsNumber;

}
