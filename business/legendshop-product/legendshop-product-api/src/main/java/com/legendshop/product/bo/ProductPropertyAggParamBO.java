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

/**
 * 类目关联管理跟参数的关系表(ProductPropertyAggParam)实体类
 *
 * @author legendshop
 */
@Data
public class ProductPropertyAggParamBO implements Serializable {


	private static final long serialVersionUID = 8599190728548667583L;
	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 聚合ID
	 */
	private Long aggId;


	/**
	 * 参数属性ID
	 */
	private Long propId;


	/**
	 * 顺序
	 */
	private Integer seq;

}
