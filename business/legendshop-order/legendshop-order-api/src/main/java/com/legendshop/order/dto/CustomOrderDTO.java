/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import com.legendshop.order.enums.OrderStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title QueryOrderResponse
 * @date 2022/5/23 17:56
 * @description：
 */
@Data
public class CustomOrderDTO implements Serializable {

	private static final long serialVersionUID = -7148869510319894630L;

	/**
	 * 订单ID
	 */
	private String orderId;

	/**
	 * 订单号
	 */
	private String orderNumber;

	/**
	 * 商品总价格
	 */
	private BigDecimal totalPrice;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 订单状态
	 * {@link OrderStatusEnum}
	 */
	private Integer status;

	/**
	 * 商品总数量
	 */
	private Integer totalBasketCount;

	/**
	 * 订单项
	 */
	private List<CustomOrderItemDTO> itemList;
}
