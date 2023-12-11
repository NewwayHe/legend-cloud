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
import com.legendshop.common.core.dto.BaseDTO;
import lombok.Data;

/**
 * 类目关联管理表(ProductPropertyAgg)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_property_agg")
public class ProductPropertyAgg extends BaseDTO implements GenericEntity<Long> {

	private static final long serialVersionUID = -92565058204455331L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PROD_TYPE_SEQ")
	private Long id;


	/**
	 * 产品类型名称
	 */
	@Column(name = "name")
	private String name;

}
