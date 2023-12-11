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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品快照(ProdSnapshot)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_snapshot")
public class ProductSnapshot implements GenericEntity<Long> {

	private static final long serialVersionUID = -86265350364828514L;

	/**
	 * 快照ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PRODUCT_SNAPSHOT_SEQ")
	private Long id;


	/**
	 * 商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 产品ID
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * skuId
	 */
	@Column(name = "sku_id")
	private Long skuId;


	/**
	 * 版本号
	 */
	@Column(name = "version")
	private Integer version;


	/**
	 * 商品名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 原价
	 */
	@Column(name = "original_price")
	private BigDecimal originalPrice;

	/**
	 * 销售价
	 */
	@Column(name = "price")
	private BigDecimal price;


	/**
	 * 简要描述,卖点等
	 */
	@Column(name = "brief")
	private String brief;


	/**
	 * 详细描述
	 */
	@Column(name = "content")
	private String content;


	/**
	 * 商品图片
	 */
	@Column(name = "pic")
	private String pic;


	/**
	 * 属性json
	 */
	@Column(name = "attribute")
	private String attribute;


	/**
	 * 参数json
	 */
	@Column(name = "parameter")
	private String parameter;

	/**
	 * 参数组json
	 */
	@Column(name = "param_group")
	private String paramGroup;

	/**
	 * 品牌名称
	 */
	@Column(name = "brand_name")
	private String brandName;


	/**
	 * 商品类型，E.普通商品，V:虚拟商品
	 */
	@Column(name = "product_type")
	private String productType;


	/**
	 * 记录时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 购买数量
	 */
	@Column(name = "product_count")
	private Integer productCount;

}
