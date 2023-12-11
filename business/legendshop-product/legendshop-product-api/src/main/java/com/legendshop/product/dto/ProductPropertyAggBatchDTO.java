/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 类目关联批量绑定、删除集合类
 *
 * @author legendshop
 */
@Data
@Schema(description = "类目关联批量绑定、删除DTO")
public class ProductPropertyAggBatchDTO implements Serializable {

	private static final long serialVersionUID = 3361360803072977205L;

	/**
	 * 属性id
	 */
	@Schema(description = "属性id")
	private Long propId;

	/**
	 * 类目关联id列表
	 */
	@Schema(description = "类目关联id列表")
	private List<Long> aggIdlist;

	/**
	 * 参数组id
	 */
	@Schema(description = "参数组id")
	private Long paramGroupId;

	/**
	 * 品牌id
	 */
	@Schema(description = "品牌id")
	private Long brandId;

}
