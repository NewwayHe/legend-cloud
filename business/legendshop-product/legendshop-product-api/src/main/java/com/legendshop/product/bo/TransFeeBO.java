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
 * 运输费用(TransFee)BO
 *
 * @author legendshop
 * @since 2020-09-04 16:55:43
 */
@Data
public class TransFeeBO implements Serializable {

	private static final long serialVersionUID = 439447353159068614L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 模板ID
	 */
	private Long transId;

	/**
	 * 续件运费
	 */
	private BigDecimal addPrice;

	/**
	 * 首件运费
	 */
	private BigDecimal firstPrice;

	/**
	 * 续件
	 */
	private Double addNum;

	/**
	 * 首件
	 */
	private Double firstNum;

	/**
	 * 1:件数、2:重量、3:体积
	 */
	private String calFreightType;

	/**
	 * 状态
	 */
	private Integer status;

}
