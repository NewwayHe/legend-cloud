/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 店铺订单数DTO
 *
 * @author legendshop
 * @create: 2020-12-19 11:47
 */
@Data
public class ShopOrderCountDTO implements Serializable {

	private static final long serialVersionUID = -5375832125164345542L;

	private Long shopId;

	private Long count;
}
