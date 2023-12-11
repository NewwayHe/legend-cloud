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
 * 商家的产品类目(ShopCat)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_cat")
public class ShopCategory extends BaseEntity implements GenericEntity<Long> {


	private static final long serialVersionUID = -1621189703795474430L;
	/**
	 * 产品类目ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_CAT_SEQ")
	private Long id;


	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 父节点
	 */
	@Column(name = "parent_id")
	private Long parentId;


	/**
	 * 产品类目名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 排序
	 */
	@Column(name = "seq")
	private Integer seq;


	/**
	 * 默认是1，表示正常状态,0为下线状态
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 分类层级
	 */
	@Column(name = "grade")
	private Integer grade;

}
