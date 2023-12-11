/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;

import cn.legendshop.jpaplus.support.PageParams;
import lombok.Data;

/**
 * 配送方式.
 *
 * @author legendshop
 */
@Data
public class TransFeeQuery extends PageParams {

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 运输ID
	 */
	private Long transportId;

	/**
	 * 运输模式
	 */
	private String freightMode;


	/**
	 * 状态
	 */
	private Integer status;

}
