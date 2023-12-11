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

/**
 * (ProdGroupRelevance)实体类
 * 商品分组-商品关系表
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_group_relation")
public class ProductGroupRelation implements GenericEntity<Long> {

	private static final long serialVersionUID = -42693375901642967L;

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PROD_GROUP_RELEVANCE_SEQ")
	private Long id;


	/**
	 * 商品分组ID
	 */
	@Column(name = "group_id")
	private Long groupId;


	/**
	 * 商品ID
	 */
	@Column(name = "product_id")
	private Long productId;

}
