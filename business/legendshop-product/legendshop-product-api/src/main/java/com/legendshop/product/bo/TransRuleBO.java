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
import java.util.Date;

/**
 * 店铺运费规则(TransRule)BO
 *
 * @author legendshop
 * @since 2020-09-08 17:00:54
 */
@Data
public class TransRuleBO implements Serializable {

	private static final long serialVersionUID = 678423318049879855L;


	private Long id;


	private Long shopId;

	/**
	 * 1：叠加运算 2：按最高值计算
	 */
	private Integer type;


	private Date recDate;

}
