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
 * 条件包邮(TransFree)BO
 *
 * @author legendshop
 * @since 2020-09-04 16:55:46
 */
@Data
public class TransFreeBO implements Serializable {

	private static final long serialVersionUID = 818867540777093482L;


	private Long id;

	/**
	 * 模板id
	 */
	private Integer transId;

	/**
	 * 满件包邮
	 */
	private Integer num;

	/**
	 * 满多少金额包邮
	 */
	private BigDecimal price;

	/**
	 * 1：满件 2：满金额
	 */
	private Integer type;

}
