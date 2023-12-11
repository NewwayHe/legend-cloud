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
 * 属性值(ProdPropValue)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_property_value")
public class ProductPropertyValue extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 466955001076445111L;

	/**
	 * 属性值ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PRODUCT_PROPERTY_VALUE_SEQ")
	private Long id;


	/**
	 * 属性ID
	 */
	@Column(name = "prop_id")
	private Long propId;

	/**
	 * 店铺id
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 属性值名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 图片路径，新版不再使用，作为预留字段
	 */
	@Column(name = "pic")
	private String pic;


	/**
	 * 排序
	 */
	@Column(name = "sequence")
	private Integer sequence;

	/**
	 * 删除标识【true：已删除  false：正常使用】
	 */
	@Column(name = "delete_flag")
	private Boolean deleteFlag;

	/**
	 * 属性值别名
	 */
	@Transient
	private String alias;

	@Transient
	private String url;

}
