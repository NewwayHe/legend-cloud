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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * (ProdGroup)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_group")
public class ProductGroup extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 649148571206757413L;

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PROD_GROUP_SEQ")
	private Long id;


	/**
	 * 商品分组名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 分组类型 0:系统定义 1:自定义
	 */
	@Column(name = "type")
	private Integer type;


	/**
	 * 分组条件
	 */
	@Column(name = "conditional")
	private String conditional;


	/**
	 * 组内排序条件
	 */
	@Column(name = "sort")
	private String sort;


	/**
	 * 商品分组描述
	 */
	@Column(name = "description")
	private String description;


}
