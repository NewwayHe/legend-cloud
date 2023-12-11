/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import cn.legendshop.jpaplus.persistence.Column;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
public class SkuAmountDTO implements Serializable {
	private Long skuId;
	private Long productId;

	/**
	 * 销售价
	 */
	@Column(name = "price")
	private BigDecimal price;

	/**
	 * 成本价
	 */
	@Column(name = "cost_price")
	private BigDecimal costPrice;
}
