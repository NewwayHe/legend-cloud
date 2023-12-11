/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单中具体商品所需要的运费
 *
 * @author legendshop
 */
@Data
public class TransFeeCalProductResultBO implements Serializable {

	private Long productId;

	/**
	 * 运费分配商品占的金额
	 */
	private BigDecimal freight;
}
