/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品参数属性跟参数组的关系表(ProductPropertyParamGroup)
 *
 * @author legendshop
 */
@Data
@NoArgsConstructor
public class ProductPropertyParamGroupDTO implements GenericEntity<Long> {


	private static final long serialVersionUID = 2468657911065066799L;
	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 聚合Id
	 */
	private Long groupId;


	/**
	 * 参数属性ID
	 */
	private Long propId;


	/**
	 * 顺序
	 */
	private Integer seq;

	public ProductPropertyParamGroupDTO(Long groupId, Long propId, Integer seq) {
		this.groupId = groupId;
		this.propId = propId;
		this.seq = seq;
	}
}
