/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.query;

import cn.legendshop.jpaplus.support.PageParams;
import lombok.Data;

import java.util.Date;

/**
 * 订单项查询的参数
 *
 * @author legendshop
 */
@Data
public class OrderItemQuery extends PageParams {

	private static final long serialVersionUID = 8434954261109197313L;

	private Long shopId;

	private String orderItemNumber;

	private String productName;

	private Integer status;

	private Date fromDate;

	private Date toDate;

	private Long distUserId;

	private Long userId;
}
