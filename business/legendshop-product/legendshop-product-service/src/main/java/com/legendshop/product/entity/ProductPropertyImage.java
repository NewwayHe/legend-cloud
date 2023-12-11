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

import java.util.Date;

/**
 * 属性图片
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_property_image")
public class ProductPropertyImage implements GenericEntity<Long> {

	private static final long serialVersionUID = -473366926789786034L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PRODUCT_PROPERTY_IMAGE_SEQ")
	private Long id;

	/**
	 * 商品ID
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 属性ID
	 */
	@Column(name = "prop_id")
	private Long propId;

	/**
	 * 属性值ID
	 */
	@Column(name = "value_id")
	private Long valueId;

	/**
	 * 属性值名称
	 */
	@Column(name = "value_name")
	private String valueName;

	/**
	 * 图片Url
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 顺序
	 */
	@Column(name = "seq")
	private Long seq;

	/**
	 * 创建时间
	 */
	@Column(name = "create_date")
	private Date createDate;

} 
