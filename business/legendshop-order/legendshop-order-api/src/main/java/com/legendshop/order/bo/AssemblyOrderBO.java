/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;

import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.OrderItemDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 组装订单BO
 *
 * @author legendshop
 */
@Data
public class AssemblyOrderBO implements Serializable {

	private static final long serialVersionUID = 5480176828892405091L;

	/**
	 * 订单
	 */
	private OrderDTO orderDTO;

	/**
	 * 订单项
	 */
	private List<OrderItemDTO> orderItemDTO;
}
