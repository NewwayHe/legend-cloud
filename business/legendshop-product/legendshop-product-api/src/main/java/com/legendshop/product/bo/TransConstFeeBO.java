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
import java.util.Date;

/**
 * 固定运费(TransConstFee)BO
 *
 * @author legendshop
 * @since 2020-09-07 14:43:50
 */
@Data
public class TransConstFeeBO implements Serializable {

	private static final long serialVersionUID = 323493324662727956L;


	private Long id;

	/**
	 * 模板id
	 */
	private Integer transId;

	/**
	 * 固定运费
	 */
	private BigDecimal constantPrice;


	private Date recDate;

}
