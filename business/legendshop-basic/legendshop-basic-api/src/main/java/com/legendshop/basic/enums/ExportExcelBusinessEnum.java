/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ExportExcelBusinessEnum
 * @date 2022/4/26 11:43
 * @description：
 */
@Getter
@AllArgsConstructor
public enum ExportExcelBusinessEnum {

	/**
	 * 订单列表
	 * com.legendshop.order.controller.admin.AdminOrderController#orderExport(javax.servlet.http.HttpServletResponse, com.legendshop.order.query.OrderSearchQuery)
	 */
	ORDER_LIST("ORDER_LIST", "订单列表"),

	;

	private final String value;
	private final String desc;
}
