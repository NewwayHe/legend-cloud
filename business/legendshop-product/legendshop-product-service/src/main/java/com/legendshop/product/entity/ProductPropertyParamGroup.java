/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品参数属性跟参数组的关系表(ProductPropertyParamGroup)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_property_param_group")
@NoArgsConstructor
public class ProductPropertyParamGroup implements GenericEntity<Long> {


	private static final long serialVersionUID = 2468657911065066799L;
	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PROD_PROP_PARAM_GROUP_SEQ")
	private Long id;


	/**
	 * 聚合Id
	 */
	@Column(name = "group_id")
	private Long groupId;


	/**
	 * 参数属性ID
	 */
	@Column(name = "prop_id")
	private Long propId;


	/**
	 * 顺序
	 */
	@Column(name = "seq")
	private Integer seq;

	public ProductPropertyParamGroup(Long groupId, Long propId, Integer seq) {
		this.groupId = groupId;
		this.propId = propId;
		this.seq = seq;
	}
}
