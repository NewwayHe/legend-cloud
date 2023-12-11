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
 * 类目关联管理跟参数的关系表(ProductPropertyAggParam)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_property_agg_param")
@NoArgsConstructor
public class ProductPropertyAggParam implements GenericEntity<Long> {

	private static final long serialVersionUID = 181228717695638879L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PROD_TYPE_PARAM_SEQ")
	private Long id;


	/**
	 * 聚合Id
	 */
	@Column(name = "agg_id")
	private Long aggId;


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

	public ProductPropertyAggParam(Long aggId, Long propId, Integer seq) {
		this.aggId = aggId;
		this.propId = propId;
		this.seq = seq;
	}
}
