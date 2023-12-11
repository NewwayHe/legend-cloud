/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 包邮活动关联商品表(ShippingProd)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shipping_product")
public class ShippingProduct implements GenericEntity<Long> {

	private static final long serialVersionUID = 197336836508785997L;

	/**
	 * 包邮活动关联商品表Id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHIPPING_PROD_SEQ")
	private Long id;


	/**
	 * 商家Id
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 商品Id
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * 包邮活动Id
	 */
	@Column(name = "shipping_id")
	private Long shippingId;

}
