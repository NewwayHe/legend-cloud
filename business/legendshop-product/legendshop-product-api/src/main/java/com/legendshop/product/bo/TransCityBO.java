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
 * 每个城市的运费设置(TransCity)BO
 *
 * @author legendshop
 * @since 2020-09-04 15:13:55
 */
@Data
public class TransCityBO implements Serializable {

	private static final long serialVersionUID = 580879468324335827L;


	private Long id;

	/**
	 * 父id
	 */
	private Long parentId;


	private Long cityId;

	/**
	 * 类型1:区域限售 2：运费计算 3：条件包邮
	 */
	private Integer type;

	/**
	 * 省份编码
	 */
	private Long provinceId;
}
