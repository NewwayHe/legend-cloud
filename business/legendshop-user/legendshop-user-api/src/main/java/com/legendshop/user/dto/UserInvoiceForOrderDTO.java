/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @author legendshop
 */
@Data
public class UserInvoiceForOrderDTO {

	private Long userId;

	private List<String> invoiceTypeList;
}
