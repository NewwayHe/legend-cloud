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
 * 类目关联管理跟品牌的关系表(ProductPropertyAggBrand)实体类
 *
 * @author legendshop
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "ls_product_property_agg_brand")
public class ProductPropertyAggBrand implements GenericEntity<Long> {

	private static final long serialVersionUID = -46617475376378771L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PROD_TYPE_BRAND_SEQ")
	private Long id;


	/**
	 * 聚合Id
	 */
	@Column(name = "agg_id")
	private Long aggId;


	/**
	 * 品牌Id
	 */
	@Column(name = "brand_id")
	private Long brandId;


	/**
	 * 顺序
	 */
	@Column(name = "seq")
	private Integer seq;

	public ProductPropertyAggBrand(Long aggId, Long brandId, Integer seq) {
		this.aggId = aggId;
		this.brandId = brandId;
		this.seq = seq;
	}
}
