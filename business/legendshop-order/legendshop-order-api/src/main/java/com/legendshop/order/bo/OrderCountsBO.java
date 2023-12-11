/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单状态数量DTO
 *
 * @author legendshop
 */
@Data
public class OrderCountsBO implements Serializable {

	private static final long serialVersionUID = 7421903970225895240L;

	private Integer status;

	private Integer subCounts;

}
